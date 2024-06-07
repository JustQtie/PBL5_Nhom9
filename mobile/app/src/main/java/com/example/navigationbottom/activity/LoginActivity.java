package com.example.navigationbottom.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.user.LoginResponse;
import com.example.navigationbottom.viewmodel.SessionManager;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText edtTaiKhoan, edtMauKhau;
    private Button btnLogin;

    private TextView txtSignUp;

    private UserApiService userApiService;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_logout);
        txtSignUp = findViewById(R.id.textView2);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Login ....");
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickLogin();
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });
    }

    private void SignUp() {
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }


    private void clickLogin() {
        progressDialog.show();
        edtTaiKhoan = findViewById(R.id.edt_taikhoan_login);
        edtMauKhau = findViewById(R.id.edt_matkhau_login);

        User user = new User();
        user.setPhone_number(edtTaiKhoan.getText().toString().trim());
        user.setPassword(edtMauKhau.getText().toString().trim());

        userApiService = new UserApiService(this);
        userApiService.postUserLogin(user).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse response1 = response.body();

                if(response1 != null){
                    progressDialog.dismiss();
                    if(response1.getEc().equals("0")) {
                        if (response1.getUser().getActive() && response1.getUser().getId() != 1L) {
                            UserDataSingleton.getInstance().setUser(response1.getUser());
                            UserPreferences.saveUser(LoginActivity.this, response1.getUser());
                            SessionManager.getInstance(getApplicationContext()).saveToken(response1.getToken());
                            Toasty.success(LoginActivity.this, "Đăng nhập thành công.", Toasty.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else if (response1.getUser().getId() == 1L) {
                            Toasty.warning(LoginActivity.this, "Admin không có quyền truy cập. Vui lòng tạo tài khoản người dùng.", Toasty.LENGTH_SHORT).show();
                        }
                    }else if(response1.getEc().equals("-2")){
                        Toasty.warning(LoginActivity.this, "Tài khoản của bạn đã bị cấm hoạt động.", Toasty.LENGTH_SHORT).show();
                    }else {
                        Toasty.error(LoginActivity.this, "Xin vui lòng kiểm tra lại tài khoản mật khẩu!", Toasty.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    Toasty.error(LoginActivity.this, "Xin vui lòng kiểm tra lại tài khoản mật khẩu!", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(LoginActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });

    }
}