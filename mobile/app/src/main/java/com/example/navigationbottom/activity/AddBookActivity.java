package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

public class AddBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtTieude, edtTacgia, edtSoLuong, edtGia;
    private ShapeableImageView ivPost;
    private AppCompatButton btnUpload;
    private FloatingActionButton btnCamera;
    String name, email, uid, dp;
    private Uri imageUri;
    private Dialog dialog;
    private static final int CAMERA_PERMISSION_CODE = 123;
    private static final int GALLERY_PERMISSION_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 125;
    private static final int GALLERY_REQUEST_CODE = 126;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        toolbar = findViewById(R.id.toolbarAddBookActivity );
        setSupportActionBar(toolbar);

    // Inside your AddBookActivity.java
        Spinner spinnerBookCategory = findViewById(R.id.spinner_book_category);
        Spinner spinnerBookStatus = findViewById(R.id.spinner_book_status);

    // Example data for the spinners
        String[] bookCategories = {"Giáo trình", "Sách", "Truyện"};
        String[] bookStatuses = {"Còn mới", "Củ", "Nguyên tem"};

    // Creating ArrayAdapter for Book Category
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookCategory.setAdapter(categoryAdapter);

    // Creating ArrayAdapter for Book Status
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookStatuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookStatus.setAdapter(statusAdapter);

        edtTacgia = findViewById(R.id.edt_tacgia_AddBookActivity);
        edtTieude = findViewById(R.id.edt_Title_AddBookActivity);
        edtSoLuong = findViewById(R.id.edt_Soluong_AddBookActivity);
        edtGia = findViewById(R.id.edt_gia_AddBookActivity);
        ivPost = findViewById(R.id.iv_AddBookActivity);
        btnUpload = findViewById(R.id.btn_upload_AddBookActivity);
        btnCamera = findViewById(R.id.btn_camera_AddBookActivity);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(Gravity.CENTER);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }



    private void showCustomAlertDialog(int gravity) {

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_layout);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        AppCompatButton appCompatButtonCamera = dialog.findViewById(R.id.btn_diaCamera);
        AppCompatButton appCompatButtonGallery = dialog.findViewById(R.id.btn_diaGallery);

        appCompatButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chụp từ camera
                if (ContextCompat.checkSelfPermission(AddBookActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    dialog.dismiss();
                    chupTuCamera();
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                }
            }
        });

        appCompatButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chọn từ thư viện ảnh
                if (ContextCompat.checkSelfPermission(AddBookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    moThuVien();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                }
            }
        });

        dialog.show();
    }


    private void chupTuCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
        imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void moThuVien() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivPost.setImageURI(selectedImage);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ivPost.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == CAMERA_PERMISSION_CODE) {
                chupTuCamera();
            }
        }
        else if (requestCode == GALLERY_PERMISSION_CODE) {
            moThuVien();
        }
        else {
            Toast.makeText(this, "Quyền truy cập bị từ chối.", Toast.LENGTH_SHORT).show();
        }
    }
}