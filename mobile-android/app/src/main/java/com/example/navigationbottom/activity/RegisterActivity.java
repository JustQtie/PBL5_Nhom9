package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText edtEmail, edtPassWord, edtName, edtPhone, edtAddress;
    private AppCompatButton btnRegister;
    private TextView tvLogin;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        edtEmail = findViewById(R.id.edt_email_register);
        edtPassWord = findViewById(R.id.edt_matkhau_register);
        edtName = findViewById(R.id.edt_name_register);
        edtAddress = findViewById(R.id.edt_address_register);
        edtPhone = findViewById(R.id.edt_phone_register);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register User...");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String pass = edtPassWord.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    edtEmail.setError("Invalid Email");
                    edtEmail.setFocusable(true);
                }
                else if(pass.length()<6){
                    edtPassWord.setError("Pass length at least 6 characters");
                    edtPassWord.setFocusable(true);
                }
                else{
                    registerUser(email, pass, edtPhone.getText().toString().trim(), edtName.getText().toString().trim(), edtAddress.getText().toString().trim());
                }

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void registerUser(String email, String pass, String phone, String name, String address) {
        progressDialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}