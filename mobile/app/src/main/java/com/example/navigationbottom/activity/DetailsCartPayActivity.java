package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.navigationbottom.R;

public class DetailsCartPayActivity extends AppCompatActivity {

    private TextView tvTenNguoiDatHang, tvSoDienThoaiNguoiDatHang, tvDiaChiNguoiDatHang, tvTieuDe, tvTacGia, tvLoai, tvGia, tvStatus;
    private EditText edtSoLuong;
    private AppCompatButton btnTaoGiaoDich;
    private Spinner spinnerPhuongThucThanhToan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_cart_pay);

        khoitao();

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.thanhToan_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuongThucThanhToan.setAdapter(adapter);



        // tao giao dich
        btnTaoGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
    }

    private void khoitao() {
        tvTenNguoiDatHang = findViewById(R.id.tv_tenNguoiDatHang_detail_cart_pay_activity);
        tvSoDienThoaiNguoiDatHang = findViewById(R.id.tv_soDienThoaiNguoiDatHang_detail_cart_pay_activity);
        tvDiaChiNguoiDatHang = findViewById(R.id.tv_diaChiNguoiDatHang_detail_cart_pay_activity);
        tvTieuDe = findViewById(R.id.tv_tieuDe_detail_cart_pay_activity);
        tvTacGia = findViewById(R.id.tv_tacGia_detail_cart_pay_activity);
        tvLoai = findViewById(R.id.tv_loai_detail_cart_pay_activity);
        tvGia = findViewById(R.id.tv_gia_detail_cart_pay_activity);
        tvStatus = findViewById(R.id.tv_status_detail_cart_pay_activity);
        edtSoLuong = findViewById(R.id.edt_soLuong_detail_cart_pay_activity);
        btnTaoGiaoDich = findViewById(R.id.btn_taogiaodich_cart_pay_activity);
        spinnerPhuongThucThanhToan = findViewById(R.id.spinner_PhuongThucthanhtoan_detail_cart_pay_activity);


    }
}