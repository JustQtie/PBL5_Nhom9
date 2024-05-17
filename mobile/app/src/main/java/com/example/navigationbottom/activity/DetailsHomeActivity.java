package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.model.Book;
import com.google.android.material.imageview.ShapeableImageView;

public class DetailsHomeActivity extends AppCompatActivity {

    private TextView tvTieude, tvGia, tvNguoiban, tvMota, tvLoai;
    private AppCompatButton btnSave, btnReport, btnOrder;
    private ImageView btnBack, anhLon;
    private ShapeableImageView anhNho;

    String bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_home);

        //khoi tao
        initView();


        Book book = (Book) getIntent().getSerializableExtra("book");

        if (book != null) {
            // Use the book object
            tvTieude.setText(book.getTieuDe());
            tvNguoiban.setText(book.getTacGia());
            tvLoai.setText(book.getLoai());
            tvMota.setText(book.getMoTa());
            tvGia.setText(book.getGia() + "VND");
            Glide.with(this)
                    .load(R.drawable.baseline_menu_book_24)
                    .into(anhLon);
            Glide.with(this)
                    .load(R.drawable.baseline_menu_book_24)
                    .into(anhNho);
        }

        // su ly
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    private void initView() {
        tvTieude = findViewById(R.id.tv_activitydetail_tieude);
        tvGia = findViewById(R.id.tv_activitydetail_Gia);
        tvNguoiban = findViewById(R.id.tv_activitydetail_ngBan);
        tvMota = findViewById(R.id.tv_activitydetail_Mota);
        tvLoai = findViewById(R.id.tv_activitydetail_loai);
        anhLon = findViewById(R.id.iv_anhLon_mapAc);
        anhNho = findViewById(R.id.si_anhNho_mapAc);
        btnSave = findViewById(R.id.btn_activitydetail_Save);
        btnReport = findViewById(R.id.btn_activitydetail_Report);
        btnOrder = findViewById(R.id.btn_activitydetail_Order);
        btnBack = findViewById(R.id.iv_back_mapAc);
    }
}