package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.RegisterResponse;
import com.example.navigationbottom.viewmodel.SessionManager;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_username, edt_pass, edt_pass_confi, edt_phone;
    private Button btn_register;
    private TextView txt_login;

    private UserApiService userApiService;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_login = findViewById(R.id.tv_chuyenlogin);
        btn_register = findViewById(R.id.btn_sigup);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Sign up ....");
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

    }

    private void SignUp() {
        progressDialog.show();
        edt_username = findViewById(R.id.edtlayout_taikhoan);
        edt_pass = findViewById(R.id.edtlayout_password);
        edt_pass_confi = findViewById(R.id.edtlayout_confirmPassword);
        edt_phone = findViewById(R.id.edtlayout_phonenumber);

        String username = edt_username.getText().toString();
        String password = edt_pass.getText().toString();
        String phone = edt_phone.getText().toString();
        String pass_confim = edt_pass_confi.getText().toString();

        if (TextUtils.isEmpty(username.trim()) || TextUtils.isEmpty(password.trim()) || TextUtils.isEmpty(phone.trim()) || TextUtils.isEmpty(phone.trim()) || TextUtils.isEmpty(pass_confim.trim())) {
            Toast.makeText(SignUpActivity.this, "Please enter complete information!", Toast.LENGTH_SHORT).show();
        } else if (isValidPhoneNumber(phone.trim()) == false) {
            Toast.makeText(SignUpActivity.this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
        }else{
            userApiService = new UserApiService(this);
            User user = new User();
            user.setFullname(username);
            user.setPhoneNumber(phone);
            user.setPassword(password);
            user.setRetype_password(pass_confim);

            userApiService.signUpUser(user).enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse != null) {
                        progressDialog.dismiss();
                        if (registerResponse.getEc().equals("0")) {
                            Log.d("RequestData1", new Gson().toJson(registerResponse));

                            Toast.makeText(SignUpActivity.this, "Login success!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up not success", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, "Create user fails", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    String errorMessage = t.getMessage();
                    Toast.makeText(SignUpActivity.this, "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                }
            });


        }

    }
    private boolean isValidPhoneNumber (String phoneNumber){
        // Định dạng số điện thoại: ^0\d{9}$
        // ^0 : Bắt đầu bằng số 0
        // \d{9} : Theo sau là chính xác 9 chữ số (\d là đại diện cho số)
        // $ : Kết thúc chuỗi

        String regex = "^0\\d{9}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

}