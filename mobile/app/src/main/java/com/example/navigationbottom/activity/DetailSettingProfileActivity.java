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

    private EditText edtNgaySinh;
    private Spinner spinnerGioiTinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_profile);



        edtNgaySinh = findViewById(R.id.edt_NgaySinh_setting_ActivityProfile);
        spinnerGioiTinh = findViewById(R.id.spinner_Gioitinh_setting_ActivityProfile);
        edtNgaySinh = findViewById(R.id.edt_NgaySinh_setting_ActivityProfile);
        edtSoDienThoai = findViewById(R.id.edt_sodienthoai_setting_ActivityProfile);
        edtDiaChi = findViewById(R.id.edt_diachi_setting_ActivityProfile);
        imgAnhProfile = findViewById(R.id.si_anh_setting_ActivityProfile);
        btnSave = findViewById(R.id.btn_save_activityDetailprofile);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGioiTinh.setAdapter(adapter);

        // Setup DatePicker for EditText
        edtNgaySinh.setOnClickListener(view -> showDatePickerDialog());


    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    edtNgaySinh.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
}