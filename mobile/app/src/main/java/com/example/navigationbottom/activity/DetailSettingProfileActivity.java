package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.navigationbottom.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Calendar;

public class DetailSettingProfileActivity extends AppCompatActivity {

    private EditText edtHoVaTen,edtSoDienThoai, edtDiaChi;
    private ShapeableImageView imgAnhProfile;
    private AppCompatButton btnSave;


    private Spinner spinnerGioiTinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_profile);




        spinnerGioiTinh = findViewById(R.id.spinner_Gioitinh_setting_ActivityProfile);

        edtSoDienThoai = findViewById(R.id.edt_sodienthoai_setting_ActivityProfile);
        edtDiaChi = findViewById(R.id.edt_diachi_setting_ActivityProfile);
        imgAnhProfile = findViewById(R.id.si_anh_setting_ActivityProfile);
        btnSave = findViewById(R.id.btn_save_activityDetailprofile);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGioiTinh.setAdapter(adapter);




    }


}