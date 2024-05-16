package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.JsonObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText edt_username, edt_pass, edt_name, edt_phone, edt_email, edt_address;
    private Button btn_register;
    private TextView txt_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txt_login = findViewById(R.id.textView2);
        btn_register = findViewById(R.id.btn_register);
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
        edt_username = findViewById(R.id.edtlayout_taikhoan);
        edt_pass = findViewById(R.id.edtlayout_matkhau);
        edt_name = findViewById(R.id.edtlayout_name);
        edt_phone = findViewById(R.id.edtlayout_phone);
        edt_email = findViewById(R.id.edtlayout_email);
        edt_address = findViewById(R.id.edtlayout_address);

        String username = edt_username.getText().toString();
        String password = edt_pass.getText().toString();
        String name = edt_name.getText().toString();
        String phone = edt_phone.getText().toString();
        String email = edt_email.getText().toString();
        String address = edt_address.getText().toString();






    }

//    private boolean isValidPhoneNumber(String phoneNumber) {
//        // Định dạng số điện thoại: ^0\d{9}$
//        // ^0 : Bắt đầu bằng số 0
//        // \d{9} : Theo sau là chính xác 9 chữ số (\d là đại diện cho số)
//        // $ : Kết thúc chuỗi
//
//        String regex = "^0\\d{9}$";
//
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(phoneNumber);
//
//        return matcher.matches();
//    }
//
//    private boolean isValidEmail(String email) {
//
//        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(email);
//
//        return matcher.matches();
//    }
}