package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.BookImageResponse;
import com.example.navigationbottom.response.book.BookResponse;
import com.example.navigationbottom.response.category.GetCategoryResponse;
import com.example.navigationbottom.utils.Status;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtTieude, edtTacgia, edtGia, edtMota, edtSoLuong ;
    private AppCompatButton btnUpload;
    private FloatingActionButton btnCamera;

    private Uri imageUri;
    private Dialog dialog;

    private CategoryApiService categoryApiService;
    private static final int CAMERA_PERMISSION_CODE = 123;
    private static final int GALLERY_PERMISSION_CODE = 124;
    private static final int CAMERA_REQUEST_CODE = 125;
    private static final int GALLERY_REQUEST_CODE = 126;

    private static final int PICK_IMAGES_REQUEST = 1;
    List<Uri> imageUris = new ArrayList<>();

    BookApiService bookApiService;
    UserApiService userApiService;
    Long idBookPost;
    Long categoryId;
    String selectedStatus;
    private ProgressDialog progressDialog;
    private User user;

    String url1 = "https://www.geeksforgeeks.org/wp-content/uploads/gfg_200X200-1.png";
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        toolbar = findViewById(R.id.toolbarAddBookActivity );
        setSupportActionBar(toolbar);

        fillImageSlide();

    // Inside your AddBookActivity.java
        Spinner spinnerBookCategory = findViewById(R.id.spinner_book_category);
        Spinner spinnerBookStatus = findViewById(R.id.spinner_book_status);


        categoryApiService = new CategoryApiService(this);
        categoryApiService.getListCategory().enqueue(new Callback<GetCategoryResponse>() {
            @Override
            public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                GetCategoryResponse categoryResponse = response.body();
                if (categoryResponse != null && categoryResponse.getListCategory() != null) {
                    List<Category> categories = categoryResponse.getListCategory();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getName());
                    }

                    // Tạo ArrayAdapter với danh sách các tên danh mục
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(AddBookActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBookCategory.setAdapter(categoryAdapter);

                    spinnerBookCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Lấy đối tượng Category tương ứng với vị trí được chọn
                            Category selectedCategory = categories.get(position);

                            categoryId = selectedCategory.getId();
                            Log.d("Selected Category ID", "ID: " + categoryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Xử lý trường hợp không có gì được chọn nếu cần
                        }
                    });
                } else {
                    Toasty.error(AddBookActivity.this, "Category not found", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryResponse> call, Throwable t) {
                Toasty.error(AddBookActivity.this, "Failed to load categories", Toasty.LENGTH_SHORT).show();
            }
        });


        String[] bookStatuses = {Status.BEEN_USING_FOR_6_MONTHS, Status.BEEN_USING_FOR_1_YEARS, Status.BEEN_USING_FOR_3_5_YEARS, Status.BEEN_USING_FOR_5_YEARS};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookStatuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookStatus.setAdapter(statusAdapter);

        spinnerBookStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = bookStatuses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtTacgia = findViewById(R.id.edt_tacgia_AddBookActivity);
        edtTieude = findViewById(R.id.edt_Title_AddBookActivity);
        edtSoLuong = findViewById(R.id.number_so_luong);
        edtGia = findViewById(R.id.edt_gia_AddBookActivity);
        edtMota = findViewById(R.id.edt_mota_AddBookActivity);


        btnUpload = findViewById(R.id.btn_upload_AddBookActivity);
        btnCamera = findViewById(R.id.btn_camera_AddBookActivity);
        bookApiService = new BookApiService(this);
        userApiService = new UserApiService(this);
        user = new User();
        progressDialog = new ProgressDialog(AddBookActivity.this);
        progressDialog.setMessage("Posting ....");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(Gravity.CENTER);
            }
        });
        edtGia.addTextChangedListener(new TextWatcher() {
            private String currentText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString();

                // Nếu nội dung không thay đổi, không làm gì cả
                if (newText.equals(currentText)) {
                    return;
                }

                // Loại bỏ dấu phân cách hàng nghìn và ký tự không phải số
                String cleanString = newText.replaceAll("[^\\d]", "");

                // Nếu chuỗi rỗng, không làm gì cả
                if (cleanString.isEmpty()) {
                    return;
                }

                // Chuyển chuỗi thành số nguyên
                try {
                    long parsedValue = Long.parseLong(cleanString);

                    // Định dạng số thành chuỗi với dấu phân cách hàng nghìn
                    String formattedValue = String.format("%,d", parsedValue).replace(',', '.');

                    // Cập nhật nội dung EditText với định dạng mới và thêm " VND"
                    currentText = formattedValue;
                    edtGia.setText(currentText);
                    edtGia.setSelection(currentText.length());  // Đặt con trỏ ở cuối chuỗi
                } catch (NumberFormatException e) {
                    // Nếu người dùng nhập số không hợp lệ, báo lỗi
                    edtGia.setError("Định dạng giá tiền không hợp lệ");
                }
            }
        });

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                try {
                    String input = dest.toString() + source.toString();
                    int value = Integer.parseInt(input);
                    if (value >= 1) {
                        return null; // Accept the input
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return ""; // Reject the input
            }
        };


        edtSoLuong.setFilters(new InputFilter[]{filter});


        edtSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        int value = Integer.parseInt(input);
                        if (value < 1) {
                            edtSoLuong.setError("Please enter a number greater than 1");
                        } else {
                            edtSoLuong.setError(null); // Clear the error
                        }
                    }
                } catch (NumberFormatException e) {
                    edtSoLuong.setError("Invalid number");
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                if(imageUris == null){
                    progressDialog.dismiss();
                    Toasty.warning(AddBookActivity.this, "Please upload photos before posting books for sale", Toasty.LENGTH_SHORT).show();
                } else if (imageUris.size()>5) {
                    progressDialog.dismiss();
                    Toasty.warning(AddBookActivity.this, "Only a maximum of 5 photos can be selected", Toasty.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(edtTacgia.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtTieude.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtGia.getText().toString().trim())){
                    progressDialog.dismiss();
                    Toasty.warning(AddBookActivity.this, "Please fill in the blank fields", Toasty.LENGTH_SHORT).show();
                }else {
                    user = UserPreferences.getUser(getApplicationContext());
                    User exitsuser = UserPreferences.getUser(getApplicationContext());
                    userApiService.getUser(exitsuser.getId()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            user = response.body();
                            if(user.getEc().equals("0")){
                                if(user.getAddress() == null){
                                    progressDialog.dismiss();
                                    Toasty.info(AddBookActivity.this, "You need to add an address before posting books for sale!", Toasty.LENGTH_SHORT).show();
                                }else{
                                    Book book = new Book();
                                    book.setName(edtTieude.getText().toString().trim());
                                    book.setAuthor(edtTacgia.getText().toString().trim());
                                    book.setPrice(Float.parseFloat(edtGia.getText().toString()));
                                    book.setQuantity(Integer.parseInt(edtSoLuong.getText().toString()));
                                    book.setDescription(edtMota.getText().toString().trim());
                                    book.setCategory_id(categoryId);
                                    book.setStatus(selectedStatus);
                                    book.setUser_id(user.getId());
                                    bookApiService.postBook(book).enqueue(new Callback<BookResponse>() {
                                        @Override
                                        public void onResponse(Call<BookResponse> call, Response<BookResponse> response) {
                                            BookResponse bookResponse = response.body();
                                            if(bookResponse !=null){
                                                Log.d("RequestData1", new Gson().toJson(bookResponse));
                                                //Log.e("RequestData1", bookResponse.getUser().getAddress());
                                                if(bookResponse.getUser()!=null){
                                                        Log.d("RequestData1", "Success post my book");
                                                        idBookPost = bookResponse.getId();
                                                        bookApiService.uploadFileImage(idBookPost, prepareFileParts("files", imageUris)).enqueue(new Callback<BookImageResponse>() {
                                                            @Override
                                                            public void onResponse(Call<BookImageResponse> call, Response<BookImageResponse> response) {
                                                                BookImageResponse responseBody = response.body();
                                                                if(responseBody!=null){

                                                                    if(responseBody.getEc().equals("0")){
                                                                        progressDialog.dismiss();
                                                                        Log.d("RequestData1", new Gson().toJson(responseBody));
                                                                        Toasty.success(AddBookActivity.this, "Post my book success!!", Toasty.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(AddBookActivity.this, MainActivity.class);
                                                                        intent.putExtra("dataFromActivity", "fromAddBook");
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                                else {
                                                                    progressDialog.dismiss();
                                                                    Log.e("UploadError", "Upload failed with status: " + response.code());
                                                                    try {
                                                                        Log.e("UploadError", "Response error body: " + response.errorBody().string());
                                                                    } catch (IOException e) {
                                                                        throw new RuntimeException(e);
                                                                    }
                                                                    Toasty.error(AddBookActivity.this, "Post my book not success", Toasty.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BookImageResponse> call, Throwable t) {
                                                                progressDialog.dismiss();
                                                                String errorMessage = t.getMessage();
                                                                Toasty.error(AddBookActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                                                                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                                                            }
                                                        });
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Toasty.error(AddBookActivity.this, "Post my book not success", Toasty.LENGTH_SHORT).show();
                                                    }

                                                }else {
                                                    progressDialog.dismiss();
                                                    Toasty.error(AddBookActivity.this, "Post my book not success", Toasty.LENGTH_SHORT).show();
                                                }
                                        }
                                        @Override
                                        public void onFailure(Call<BookResponse> call, Throwable t) {
                                            progressDialog.dismiss();
                                            String errorMessage = t.getMessage();
                                            Toasty.error(AddBookActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                                            Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Error", "Not load user");
                        }
                    });
                }
            }
        });
    }

    private List<MultipartBody.Part> prepareFileParts(String partName, List<Uri> fileUris) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : fileUris) {
            File file = new File(getRealPathFromURI(uri));
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            parts.add(MultipartBody.Part.createFormData(partName, file.getName(), requestFile));
        }
        return parts;
    }



    private void reloadImageSlider(List<Uri> imageUris) {
        // Chuyển đổi danh sách Uri thành danh sách đường dẫn chuỗi
        List<String> imagePaths = convertUrisToPaths(imageUris);

        // Tạo một ArrayList chứa các SliderData từ danh sách đường dẫn chuỗi
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();
        for (String path : imagePaths) {
            sliderDataArrayList.add(new SliderData(path));
        }

        // Khởi tạo và cấu hình SliderAdapter với danh sách SliderData
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);
        SliderView sliderView = findViewById(R.id.slider);
        sliderView.setSliderAdapter(adapter);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

    }

    private void fillImageSlide(){
        // Chuyển đổi danh sách Uri thành danh sách đường dẫn chuỗi
        // we are creating array list for storing our image urls.
        ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);

        // adding the urls inside array list
        sliderDataArrayList.add(new SliderData(url1));

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
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
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Pictures"), PICK_IMAGES_REQUEST);
        dialog.dismiss();
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
            Toasty.error(this, "Quyền truy cập bị từ chối.", Toasty.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUris = new ArrayList<>();
            if (data.getClipData() != null) { // Chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                if (count > 5) {
                    // Hiển thị thông báo nếu người dùng chọn nhiều hơn 5 ảnh
                    Toasty.info(this, "You can select up to 5 images only", Toasty.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    // Gọi phương thức để upload ảnh
                    uploadImages(imageUris);
                }
            } else if (data.getData() != null) { // Chọn một ảnh
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                // Gọi phương thức để upload ảnh
                uploadImages(imageUris);
            }
        }
    }

    private void uploadImages(List<Uri> imageUris) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : imageUris) {
            File file = new File(getRealPathFromURI(uri));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
            parts.add(part);
        }
        reloadImageSlider(imageUris);
        // Gọi API upload với Retrofit
//        uploadToServer(parts);
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
    private List<String> convertUrisToPaths(List<Uri> uris) {
        List<String> paths = new ArrayList<>();
        for (Uri uri : uris) {
            String path = getRealPathFromURI(uri);
            if (path != null) {
                paths.add(path);
            }
        }
        return paths;
    }
}