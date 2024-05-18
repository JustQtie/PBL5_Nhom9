package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.category.GetCategoryResponse;
import com.example.navigationbottom.utils.Status;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.BookImageApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtTieude, edtTacgia, edtSoLuong, edtGia, edtMota;

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
    BookImageApiService bookImageApiService;
    Long bookIdNhan;
    Long categoryId;
    String selectedStatus;
    private ProgressDialog progressDialog;
    private User user;
    private Book book;

    List<String> imageUrls = new ArrayList<>();

    String imageUrl;
    private ExecutorService executorService;
    private Handler mainHandler;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        toolbar = findViewById(R.id.toolbarEditBookActivity);
        setSupportActionBar(toolbar);

        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());

        edtTacgia = findViewById(R.id.edt_tacgia_EditBookActivity);
        edtTieude = findViewById(R.id.edt_Title_EditBookActivity);
        edtSoLuong = findViewById(R.id.edt_Soluong_EditBookActivity);
        edtGia = findViewById(R.id.edt_gia_EditBookActivity);
        edtMota = findViewById(R.id.edt_mota_EditBookActivity);
        btnUpload = findViewById(R.id.btn_upload_EditBookActivity);
        btnCamera = findViewById(R.id.btn_camera_EditBookActivity);


        if (getIntent() != null && getIntent().hasExtra("book")) {
            bookIdNhan = getIntent().getLongExtra("book", -1);  // Mặc định -1 nếu không có dữ liệu
        }

        Callable<Book> callable = new Callable<Book>() {
            @Override
            public Book call() throws Exception {
                BookApiService bookApiService = new BookApiService(getApplicationContext());
                Call<Book> call = bookApiService.getBookById(bookIdNhan);
                Response<Book> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("RequestData1", new Gson().toJson(response));
                    return response.body();
                } else {
                    Log.d("RequestData1", "Failed to get book with status: " + response.code());
                    throw new Exception("Failed to get book with status: " + response.code());
                }
            }
        };

        Future<Book> future = executorService.submit(callable);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final Book bookResponse = future.get();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            book.setName(bookResponse.getName());
                            book.setAuthor(bookResponse.getAuthor());
                            book.setPoint(bookResponse.getPoint());
                            book.setDescription(bookResponse.getDescription());
                            book.setStatus(bookResponse.getStatus());
                            book.setQuantity(bookResponse.getQuantity());
                            book.setThumbnail(bookResponse.getThumbnail());
                            book.setPrice(bookResponse.getPrice());
                            book.setUser_id(bookResponse.getUser_id());
                            book.setCategory_id(bookResponse.getCategory_id());
                            fillImageSlide();
                            AddSpinner();
                            setEdText();
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }
        });
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
    private void setEdText(){
        edtGia.setText(book.getPrice().toString());
        edtTieude.setText(book.getName());
        edtSoLuong.setText(book.getQuantity());
        edtTacgia.setText(book.getAuthor());
        edtMota.setText(book.getDescription());
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
    }
    private void AddSpinner(){
        // Inside your AddBookActivity.java
        Spinner spinnerBookCategory = findViewById(R.id.spinner_book_category_EditBookActivity);
        Spinner spinnerBookStatus = findViewById(R.id.spinner_book_status_EditBookActivity);

        categoryApiService = new CategoryApiService(this);
        categoryApiService.getListCategory().enqueue(new Callback<GetCategoryResponse>() {
            @Override
            public void onResponse(Call<GetCategoryResponse> call, Response<GetCategoryResponse> response) {
                GetCategoryResponse categoryResponse = response.body();
                if (categoryResponse != null && categoryResponse.getListCategory() != null) {
                    int defaulPo = 0;
                    int i = 0;
                    List<Category> categories = categoryResponse.getListCategory();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        i++;
                        if(book.getCategory_id() == category.getId()){
                            defaulPo = i;
                        }
                        categoryNames.add(category.getName());
                    }

                    // Tạo ArrayAdapter với danh sách các tên danh mục
                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(EditBookActivity.this, android.R.layout.simple_spinner_item, categoryNames);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerBookCategory.setAdapter(categoryAdapter);
                    spinnerBookCategory.setSelection(defaulPo);
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
                    Toast.makeText(EditBookActivity.this, "Category not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetCategoryResponse> call, Throwable t) {
                Toast.makeText(EditBookActivity.this, "Failed to load categories", Toast.LENGTH_SHORT).show();
            }
        });
        String[] bookStatuses = {Status.BEEN_USING_FOR_6_MONTHS, Status.BEEN_USING_FOR_3_5_YEARS, Status.BEEN_USING_FOR_5_YEARS, Status.BEEN_USING_FOR_3_5_YEARS};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookStatuses);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBookStatus.setAdapter(statusAdapter);

        String status = book.getStatus();
        int defaultPosition = 0;
        for (int i = 0; i < bookStatuses.length; i++) {
            if (bookStatuses[i].equals(status)) {
                defaultPosition = i;
                break;
            }
        }
        spinnerBookStatus.setSelection(defaultPosition);

        spinnerBookStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = bookStatuses[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        bookImageApiService = new BookImageApiService(this);
        bookImageApiService.getThumbnailsByProductId(book.getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    imageUrls = response.body();
                    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

                    // initializing the slider view.
                    SliderView sliderView = findViewById(R.id.slider);

                    // adding the urls inside array list
                    for (String path : imageUrls) {
                        path = ApiService.BASE_URL + "api/v1/products/images/" + path;
                        sliderDataArrayList.add(new SliderData(path));
                    }

                    // passing this array list inside our adapter class.
                    SliderAdapter adapter = new SliderAdapter(getApplicationContext(), sliderDataArrayList);

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
                }else {
                    Log.e("Hello", String.valueOf("Response not success"));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(EditBookActivity.this, "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
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
                if (ContextCompat.checkSelfPermission(EditBookActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
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
                if (ContextCompat.checkSelfPermission(EditBookActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) { // Chọn nhiều ảnh
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    imageUris.add(imageUri);
                }
            } else if (data.getData() != null) { // Chọn một ảnh
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
            }
            // Gọi phương thức để upload ảnh
            uploadImages(imageUris);
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
        //uploadToServer(parts);
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