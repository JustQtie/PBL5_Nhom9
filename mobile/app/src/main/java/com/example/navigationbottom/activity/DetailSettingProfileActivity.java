package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailSettingProfileActivity extends AppCompatActivity {

    private EditText edtHoVaTen,edtSoDienThoai, edtDiaChi;
    private FloatingActionButton btnCamera;
    private ShapeableImageView imgAnhProfile;
    private AppCompatButton btnSave;
    private User userChuyenDen;
    ArrayAdapter<CharSequence> adapter;

    private Spinner spinnerGioiTinh;

    private UserApiService userApiService;

    private static final int CAMERA_PERMISSION_CODE = 123;
    private static final int GALLERY_PERMISSION_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 125;
    private static final int GALLERY_REQUEST_CODE = 126;
    private Uri imageUri;
    private Dialog dialog;

    private byte[] imageUrl_tempSave;
    private User user_ReceivedFromSettingFragment;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_setting_profile);


        Intent intent = getIntent();
        userChuyenDen = (User) intent.getSerializableExtra("userChuyenDi");


        khoiTao();

        ganDuLieuChuyenSang();

        userApiService = new UserApiService(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    capNhatThongTinNguoiDung();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(Gravity.CENTER);
            }
        });

    }

    private void showCustomAlertDialog(int gravity) {

        dialog = new Dialog(DetailSettingProfileActivity.this);
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
                if (ContextCompat.checkSelfPermission(DetailSettingProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                if (ContextCompat.checkSelfPermission(DetailSettingProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    moThuVien();
                } else {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                }
            }
        });

        dialog.show();
    }



    private void capNhatThongTinNguoiDung() {
        // Lấy dữ liệu từ các trường nhập liệu

//        // Tạo đối tượng User để gửi yêu cầu cập nhật
//        User userCapNhat = new User();
//        userCapNhat.setId(userChuyenDen.getId());
//        userCapNhat.setFullname(hoVaTen);
//        userCapNhat.setPhone_number(soDienThoai);
//        userCapNhat.setAddress(diaChi);
//        userCapNhat.setGender(gioiTinh);

        String name = edtHoVaTen.getText().toString();
        String phone = edtSoDienThoai.getText().toString();
        String address = edtDiaChi.getText().toString();
        boolean gioiTinh = spinnerGioiTinh.getSelectedItem().toString().equals("Nam");
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageUrl_tempSave, 0, imageUrl_tempSave.length);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, byteArrayOutputStream);
        byte[] compressedBytes = byteArrayOutputStream.toByteArray();


//        User.AvatarData avatar = new User.AvatarData("Buffer", compressedBytes);
//        User user = new User(user_ReceivedFromSettingFragment.getId(), name, phone, email, address, avatar);


        // Gọi API cập nhật thông tin người dùng
//        userApiService.updateUser(userCapNhat.getId(), userCapNhat).enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    // Cập nhật thành công
//                    Toast.makeText(DetailSettingProfileActivity.this, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
//                    finish();
//                } else {
//                    // Xử lý lỗi nếu có
//                    Toast.makeText(DetailSettingProfileActivity.this, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show();
//                    finish();
//                    Log.e("API Error", "Response Code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                // Xử lý lỗi khi gọi API thất bại
//                Toast.makeText(DetailSettingProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
//                finish();
//                Log.e("API Error", "onFailure: ", t);
//            }
//        });
    }


    private void ganDuLieuChuyenSang() {
        // Gán dữ liệu lên giao diện
        edtHoVaTen.setText(userChuyenDen.getFullname());
        edtSoDienThoai.setText(userChuyenDen.getPhone_number());
        edtDiaChi.setText(userChuyenDen.getAddress());


        // Gán giá trị cho Spinner
        if (userChuyenDen.getGender()) {
            spinnerGioiTinh.setSelection(adapter.getPosition("Nam"));
        } else {
            spinnerGioiTinh.setSelection(adapter.getPosition("Nữ"));
        }

//        // Gán ảnh vào ImageView
//        try {
//            String imageUrl = ApiService.BASE_URL + "api/v1/users/images/" + userChuyenDen.getThumbnail();
//
//            if (imageUrl != null && !imageUrl.isEmpty()) {
//                Glide.with(this)
//                        .load(imageUrl)
//                        .placeholder(R.drawable.ic_round_person_24) // Ảnh tạm thời khi đang tải
//                        .error(R.drawable.ic_round_person_24) // Ảnh hiển thị khi có lỗi
//                        .into(imgAnhProfile);
//            } else {
//                Glide.with(this)
//                        .load(R.drawable.ic_round_person_24)
//                        .into(imgAnhProfile);
//            }
//        } catch (Exception e) {
//            Glide.with(this)
//                    .load(R.drawable.ic_round_person_24)
//                    .into(imgAnhProfile);
//        }


    }

    private void khoiTao() {
        // khởi tạo
        spinnerGioiTinh = findViewById(R.id.spinner_Gioitinh_setting_ActivityProfile);
        edtHoVaTen = findViewById(R.id.edt_fullname_setting_ActivityProfile);
        edtSoDienThoai = findViewById(R.id.edt_sodienthoai_setting_ActivityProfile);
        edtDiaChi = findViewById(R.id.edt_diachi_setting_ActivityProfile);
        imgAnhProfile = findViewById(R.id.si_anh_setting_ActivityProfile);
        btnSave = findViewById(R.id.btn_save_activityDetailprofile);
        btnCamera = findViewById(R.id.btn_camera_setting_ActivityProfile);

        // Setup Spinner
        adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGioiTinh.setAdapter(adapter);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Định dạng số điện thoại: ^0\d{9}$
        // ^0 : Bắt đầu bằng số 0
        // \d{9} : Theo sau là chính xác 9 chữ số (\d là đại diện cho số)
        // $ : Kết thúc chuỗi

        String regex = "^0\\d{9}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
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
        Context context = this;

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgAnhProfile.setImageURI(selectedImage);
            uriImage = selectedImage;
            imageUrl_tempSave = convertUriToByteArray(context, selectedImage);

        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imgAnhProfile.setImageURI(imageUri);
            uriImage = imageUri;
            imageUrl_tempSave = convertUriToByteArray(context, imageUri);
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
            Toast.makeText(DetailSettingProfileActivity.this, "Access denied.", Toast.LENGTH_SHORT).show();
        }

    }



    public static byte[] convertUriToByteArray(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Đọc dữ liệu từ InputStream thành mảng byte
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;

                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }

                inputStream.close();
                byteArrayOutputStream.close();

                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}