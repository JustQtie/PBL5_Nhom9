package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.navigationbottom.R;

public class NotificationConfirmActivity extends AppCompatActivity {
    private TextView txtTieuDe, txtStatus, txtGia, txtMota, txtSoLuong, txtAuthor, txtDiaChiNguoiMua, txtSoDienThoaiNguoiMua, txtTenNguoiDung, txtLoai, txtThanhToan, txtTongTien;

    private AppCompatButton btnXacNhan, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_confirm);


        init();

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void init() {
        txtTieuDe = findViewById(R.id.tv_tieuDe_notification_confirm);
        txtStatus = findViewById(R.id.tv_status_detail_notification_confirm);
        txtGia = findViewById(R.id.tv_gia_notification_confirm);
        txtSoLuong = findViewById(R.id.tv_soLuong_notification_confirm);
        txtAuthor = findViewById(R.id.tv_tacGia_notification_confirm);
        txtDiaChiNguoiMua = findViewById(R.id.tv_diaChiNguoiDatHang_notification_confirm);
        txtSoDienThoaiNguoiMua = findViewById(R.id.tv_soDienThoaiNguoiDatHang_notification_confirm);
        txtTenNguoiDung = findViewById(R.id.tv_tenNguoiDatHang_notification_confirm);
        txtLoai = findViewById(R.id.tv_loai_notification_confirm);
        txtThanhToan = findViewById(R.id.tv_PhuongThucthanhtoan_notification_confirm);
        txtTongTien = findViewById(R.id.tv_tongTien_notification_confirm);
        txtMota = findViewById(R.id.tv_moTa_notification_confirm);
        btnXacNhan = findViewById(R.id.btn_xacNhan_notification_confirm);
        btnHuy = findViewById(R.id.btn_huy_notification_confirm);
    }


}