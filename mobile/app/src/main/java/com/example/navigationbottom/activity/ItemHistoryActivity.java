package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.navigationbottom.R;
import com.smarteist.autoimageslider.SliderView;

public class ItemHistoryActivity extends AppCompatActivity {

    private TextView loai, tieude, gia, tacGia, nguoiBan, diaChiNhanHang, soLuong, phuongThucThanhToan, tongTienThanhToan, soDienThoaiNguoiBan;
    private ImageView back;
    private SliderView sliderAnh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_history);

        initComponent();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void initComponent() {
        loai = findViewById(R.id.tv_itemHistory_loai);
        tieude = findViewById(R.id.tv_tieuDe_itemHistory);
        gia = findViewById(R.id.tv_gia_itemHistory);
        tacGia = findViewById(R.id.tv_tacGia_itemHistory);
        nguoiBan = findViewById(R.id.tv_nguoiBan_itemHistory);
        diaChiNhanHang = findViewById(R.id.tv_diaChiNhanHang_itemHistory);
        soLuong = findViewById(R.id.tv_soLuong_itemHistory);
        phuongThucThanhToan = findViewById(R.id.tv_phuongThucThanhToan_itemHistory);
        tongTienThanhToan = findViewById(R.id.tv_tongTien_itemHistory);
        soDienThoaiNguoiBan = findViewById(R.id.tv_soDienThoaiNguoiBan_itemHistory);
        back = findViewById(R.id.iv_back_itemHistory);
        sliderAnh = findViewById(R.id.slider_itemHistory);

    }
}