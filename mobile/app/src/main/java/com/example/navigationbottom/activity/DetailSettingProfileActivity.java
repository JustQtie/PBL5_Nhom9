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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.example.navigationbottom.response.book.BookImageResponse;
import com.example.navigationbottom.response.user.UserUpdateImageResponse;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    private Uri imageUriUpdate;

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
                capNhatThongTinNguoiDung();
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

        String name = edtHoVaTen.getText().toString();
        String phone = edtSoDienThoai.getText().toString();
        String address = edtDiaChi.getText().toString();
        boolean gioiTinh = spinnerGioiTinh.getSelectedItem().toString().equals("Nam");

        User userCapNhat = new User();
        userCapNhat.setId(userChuyenDen.getId());
        userCapNhat.setFullname(name);
        userCapNhat.setPhone_number(phone);
        userCapNhat.setAddress(address);
        userCapNhat.setGender(gioiTinh);



        if(TextUtils.isEmpty(name)){
            Toasty.warning(DetailSettingProfileActivity.this, "Họ và tên không được để trống!", Toasty.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phone)){
            Toasty.warning(DetailSettingProfileActivity.this, "Số điện thoại không được để trống!", Toasty.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPhoneNumber(phone)){
            Toasty.warning(DetailSettingProfileActivity.this, "Số điện thoại không hợp lệ!", Toasty.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)){
            Toasty.warning(DetailSettingProfileActivity.this, "Địa chỉ không được để trống!", Toasty.LENGTH_SHORT).show();
            return;
        }



        // Gọi API cập nhật thông tin người dùng
        if(isValidPhoneNumber(userCapNhat.getPhone_number())){
            userApiService.updateUser(userCapNhat.getId(), userCapNhat).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        // Cập nhật thành công
                        Toasty.success(DetailSettingProfileActivity.this, "Cập nhật thông tin thành công", Toasty.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        try {
                            String errorMessage = response.errorBody().string();
                            if(errorMessage.equals("Phone number already exists"))
                                Toasty.info(DetailSettingProfileActivity.this, "Số điện thoại này đã được sử dụng. Vui lòng nhập số điện thoại khác!", Toasty.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toasty.error(DetailSettingProfileActivity.this, "Cập nhật thông tin thất bại!", Toasty.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Xử lý lỗi khi gọi API thất bại
                    Toasty.warning(DetailSettingProfileActivity.this, "Lỗi kết nối", Toasty.LENGTH_SHORT).show();
                    finish();
                    Log.e("API Error", "onFailure: ", t);
                }
            });
        }else{
            Toasty.error(DetailSettingProfileActivity.this, "Cập nhật thông tin không thành công!", Toasty.LENGTH_SHORT).show();
        }



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

        // Gán ảnh vào ImageView
        try {
            String imageUrl = ApiService.BASE_URL + "api/v1/users/images/" + userChuyenDen.getThumbnail();

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_person_taikhoan) // Ảnh tạm thời khi đang tải
                        .error(R.drawable.baseline_person_taikhoan) // Ảnh hiển thị khi có lỗi
                        .into(imgAnhProfile);
            }
        } catch (Exception e) {
            Glide.with(this)
                    .load(R.drawable.profile)
                    .into(imgAnhProfile);
        }


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


        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgAnhProfile.setImageURI(selectedImage);
            imageUriUpdate = selectedImage;
            updateImageOnServer(imageUriUpdate);


        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imgAnhProfile.setImageURI(imageUri);
            imageUriUpdate = imageUri;
            updateImageOnServer(imageUriUpdate);
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
            Toasty.error(DetailSettingProfileActivity.this, "Access denied.", Toasty.LENGTH_SHORT).show();
        }

    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        String filePath = getRealPathFromURI(fileUri);
        Log.d("hovanthao", "1: File path: " + filePath);
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    private void updateImageOnServer(Uri imageUriUpdate) {
        MultipartBody.Part imagePart = prepareFilePart("file", imageUriUpdate);
        userApiService.updateUserImage(userChuyenDen.getId(), imagePart).enqueue(new Callback<UserUpdateImageResponse>() {
            @Override
            public void onResponse(Call<UserUpdateImageResponse> call, Response<UserUpdateImageResponse> response) {
                if (response.isSuccessful()) {
                    UserUpdateImageResponse responseBody = response.body();
                    if (responseBody != null) {
                        Log.d("hovanthao", "Response Body: " + responseBody.toString());
                        if ("0".equals(responseBody.getEc())) {
                            Toasty.success(DetailSettingProfileActivity.this, "Cập nhật ảnh người dùng thành công", Toasty.LENGTH_SHORT).show();
                        } else {
                            Toasty.error(DetailSettingProfileActivity.this, "Cập nhật ảnh người dùng thất bại", Toasty.LENGTH_SHORT).show();
                            Log.d("hovanthao", "Cập nhật ảnh người dùng thất bại, mã lỗi: " + responseBody.getEc());
                        }
                    } else {
                        Toasty.error(DetailSettingProfileActivity.this, "Response body is null", Toasty.LENGTH_SHORT).show();
                        Log.d("hovanthao", "Response body is null");
                    }
                } else {
                    Toasty.error(DetailSettingProfileActivity.this, "Cập nhật ảnh người dùng thất bại", Toasty.LENGTH_SHORT).show();
                    Log.d("hovanthao", "3 : Cập nhật ảnh người dùng thất bại, mã phản hồi: " + response.code());
                    try {
                        Log.d("hovanthao", "Response error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.d("hovanthao", "Error parsing response error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserUpdateImageResponse> call, Throwable t) {
                Log.e("hovanthao", "Xảy ra lỗi khi cập nhật ảnh người dùng", t);
                Toasty.error(DetailSettingProfileActivity.this, "Xảy ra lỗi khi cập nhật ảnh người dùng", Toasty.LENGTH_SHORT).show();
            }
        });
    }




}