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
    private List<Uri> imageUris;

    BookApiService bookApiService;
    UserApiService userApiService;
    Long idBookPost;
    Long categoryId;
    String selectedStatus;
    private ProgressDialog progressDialog;
    private User user;

    String url1 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAA81BMVEX///8ZVoLzgSD+/v7Dw8O+vr4aVoH///2Ysb8AUHzW5u/78+MAQ3AGUoIZWINpiaDtewP2/vzb5+oASHRykaj1gR7vp23pdwr/69T1fRPO4OVcfZzsrnkATXwASXnxeADo8PUATX8AQXQoWn5DbItQdpP9+O3vnFrulEvvgiLy9/mWlpbAy9MALmMxZIevwc0nXYlnjKqOp7q7ztr67uX78uyDnrbycgD3jDb0pm72uI71zK4YUHb42L/3wp30r4D3kkX/6c0/bZOhs8hZf5b207H44tL2rXn759jrl03zyJz32bfX3uCmusfuuozmfBOUprL94f88AAALZElEQVR4nO2djV/aOhfHU3JvQoFWyotatC+AoO12QaljblOnTDenY/f+/3/Nc05aEBAGbKKEJ7/5cYptkm/PyUlaOUfy5q/N1hvyF9ls/Q2EFP+NfdCJF3713XofTBPCzZUilF+KUH4pQvmlCOWXIpRfilB+KUL5pQjllyKUX4pQfilC+aUI5ZcilF+KUH4pQvmlCOWXIpRfilB+KUL5pQjllyKUX4pQfilC+aUI5ZcilF+KUH6thNB/Cw1zSvhSZ3HCKeUcTqbPOZ5VEPLMP622xTldjhCuCCeZUu3meYfz/IQ4zJzhal/C5Zut7NV0YwubeD6tgJAAoakxnfXBjotZEg+jvLLn6iZDwmcczuoIQe5xGz1v/ng5fhROXJ1pTB5Cppmm5h5Ukpd+dQJehLBTdeAUJpcNQXqxFIoIOdOSXOQrRS3gE5KMEP6rsgjXgZmAyB9u5bQEUDZCoVyJzgw34ieR7gCZnDaMDem0MnzGIk4h3HZqIwfLSagZxWjGqDkP91y2hoQcd1jDL2kcLpJXnhAy+MZNw1aMTzYChvVbuqmZ0wnFYpp8iIkMXS0P/3uEIjqe3p2evnuHhDh4PoiXU2zI6prbjdM6RwnhnELdwDViJqG4atg+f3d6endnzZ7Rz0yI159vv/c8L58Pzo4+fPz0tckfG5mwIQCYrNidcD7cDGTOMYayGYSEWM27i8vPh1dBPg99ee+zdNaEfm5CdBe+b5dTZVAqVbah++DDpztiCV8cJ4RR4/rvdsbXDLhG/rkBeGz8WCREzyTNi89HAGbbNnYB/ZTz2d9w0z8iTA0VpGAA3vvg865FnxLGqqXH+oEggy46qZiQNm8PkS41JiBcPgo9EyFaEjjL9s61NZ2Q1VkuGmuDNqYADmy47ZUDaDCIG14DQmCMP+/skhk2hJBa9wmN9zfYYbqosRk25GTbDsqpMbxXJ0zNJWTM2RJROO63UjSnmDCxIRA+bXztCSGg5qJkTYGIeKCzaYgSEwq1rPiOkZK2O/0I2QmL/XiM3KrPOEJyQmbWMVBCuJllQtkJwYgR8nFyMOsA6QmrJRLvejaW0DjHpxok7WwsoZarIOGNsamEjOmwO+WWvrmEpo6bskxu8pZiYwjh9YbFeZRj2qYSMtjWwGo4M9BIT4iMISdpXZvpxxtAaCnCVyMUD9XEUzb4wqK/Scj+lJDHz8E4XfDXjMsRYpt+1O3d4P75dQhJ5fzkS8aKH6I+MyEe5Ld7jqs7N3iv/lqEruG4x90KWdBjlyHkmZJexc2IsYfb59ciLOJhunvQD5+bMNwqJg/H1oDQ1IyqGc0b8pKEhZyZ7ETWgBAHopcWGfgyhMOnf+tAiMMokQVGLjvhfClCRagIFeGfEqpYqggVoSJUhIpwPqFaLRShIlSEivDPCVUsVYSKUBEqQkU4n1CtFopQESpCRfjnhK8SSynP5Ga9a20Rwpu1tyHlYWPWu7jnEjLDbccZJWtMyDm30jMR5xA6BwVMZlxbwkDMQ3wTSKWuT02JmUPoluK3XsVZQWtIWI4jDYj7DWdZQqaBh4qxCC9dR0IY0852M26X025tatbPbELjvBJn5xP6cJaa4qbrEEtTMLBbblki07Of00zTMCeyLacR4qVgesvn4k15VvbQK08x4RrYMDak9yFLRNoIj1yDTeYYzrAh06qNEPN+4ercpkTW6NoSghmD2+Tv1EaOZk6iTLWhqek3aHhYJ7LbXky3jvMwMWKqnN9vgrNxLJcwmXYwjRCc1NmKs9Oti8AOZly5tSEUnnq1S0UKV1Sc2N5MtSFzTjBXiIfNnzADU7MQ14VQeFc+/wnfgwqIYo/KfknIqnuiRhSFEJOaPgXXijDB9LYtCz0vyrHRnPtphPoBVlzi1vWUVX4O4SusFomCcjl/1BQxFXZw5488UwiNuo9lKPj9zmRq88RVe1EbQlTYL4tKA55nx2UHxqYP5prbZ3fizdilKlaOmE6IP9ALokqNmIJPXAHrGcTdlINg9wUJ47pB796dfr24/HYU4ACeJJfD+B4sXMMbBhuabNKGppnDwie0ue3ZwfgMLItiDfmrw4+fLu5O34lyWS9qQ5GbgGXnLMqzF5+PbM+eRIQNDkYbv/5Y3eOJDd00btWbh14wEWLKeS84vBQFN2DhsUSlE3D6lyZ8rFdGsw/fAvTYsVF69xbH3PuhEccJmanfoJWb373hDEbOoOzljy7vBm9VFx0l5aaAMGlsxYRxiS5xLyfOhm/AI7/ul0ctCaPN32MxkPTwwo8Tmlodc0qzR14SRmE2wxeed/Qpi6YbZI0I/4wLxFRyWuIRK42l1PcLIN8Pw+HPRY4H5ReH70fvfGzvHkZKG/p0L3UjOCd7NHIGrDPB5Z1F49YSK9Iw7rDgh+ilj4Tz9VuEsII5bixH144bJ512xbcGpfNI9mNq5N4ArAhzyNe0uoinI4SmKOlCafPIHsRNjC2HF4/1fMJMlO7u9Y6H/bkjeX0rJRyRyQzDcVznuNGJfBo7b/PyDJ01cbz8AybgF+Nlf5SQaa0Q9q/fBwtrULbz21+Toknhj36pp7tVR9dn5Ju+FKEhfAYGbFRdp9eNQjF3mrdX3oAwlb8GqhudjRKiBbViBg7+MAgyyLcrSnvywpc9rerMTBZ+WcJxOa5+0xaQzftUPgms9tkupX6cgv9IyMxqCQ786eH+AOOn92FXXJ1Kp+dW59C9HiHKre5FFBfxj54oflQO7O9NSjru2DwEq5+H3LrNIx5WKLr6imVB/M5xzWEmaG5fr0WINVmMGuv4RNwr2FgoCLbhsGa3jLF5yNw2p9c7MaAXfMIHUNFerSrKSi3S1QpXiwVkVotbGQitD2e40sECfo8PNUbnoWYcQBi9EuuELW6Zw37LXcA5xwjna1WE+GSpuJeB6bift3Gi/XtNKVZSeLRhsUI4PjQEA15dWMRKM3eZ9ldJaCblAhxcpBx23Or1Go1er9cy9WoNXkE7mHisUdwqEHJ9hbHEPmryDK4Y5wmhfkKsWw9jkbffJLRdF4/JsWEjblg7hjYbB9gwM8TSKy4QGxYrWBmhWABdvdXopts/YFdjDc6mYegX3rY7Jz1NrMwwFMPthqT5De+LvJ+EnDhMSwi1YoFkMYTaZxeERAcuzF+4kXKqsLB2+1EGdkt0rOGo39nqmY4Dewwz3piuhBAfqbhOq9Sv+IP9FI833+KRBU+q6oaFqNNgEPIZc+ptwm/hhri8c00LWJw2JtRhpdgG43qHTeJvuRozdNdtdfs/fNEsT0QHXxDx2wJaiNKN82QtWQEh091ar/M2HLKJJ/DJKEhcKHDwGUfTPmG1qlFr+AS21ilYMkhJNxJCN0MewEe9S0r6mNn72PBgLzqsXRtfvCTRWoykf+LUdM0pLVL5ewnCyj9aKfLjdPFFjhfHZdINp+h+ofyjl/IusfJO7KX6FoE4ap9dE79Rc81SFJKkjubcZuP+K52D3NaTapNTtASh/xatYw2LXM5TfH8Mm+4ve2jGW88+OyUn+jnM27Rey5BLzzvMkn7tuFuJbzPpQin2j26L1dvnr4jL1lT4jeKpOJZ0I6K7ZxBsKjnhpdUbCDMQQ/3SyY+kOj3nC44iqWS6aPcv8pcD8IL/KMBt4E6WHOhYRKkWkY9w4xhG8VO2VXb+AoRxjMBw2/ywT6Ic2rBOs/8+WMMSs6vs/oX/+sPPXUtEmjS536VLuNof6IUJeci7Ief/+c2mKM37Ah2/MCG4qh9y+oNbw/LHK+9S/Y0S6aUI5ZcilF+KUH4pQvmlCOWXIpRfilB+KUL5pQjllyKUX4pQfilC+aUI5ZcilF+KUH4pQvmlCOWXIpRfilB+KUL5pQjllyKUX4pQfilC+aUI5df/DeEmSxBuspDwzd+brTf/Aw89m/wI8H+KAAAAAElFTkSuQmCC";
    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);


        toolbar = findViewById(R.id.toolbarAddBookActivity );
        setSupportActionBar(toolbar);

        fillImageSlide();

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
                            Category selectedCategory = categories.get(position);
                            categoryId = selectedCategory.getId();
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
                    Toasty.info(AddBookActivity.this, "Vui lòng upload hình ảnh trước khi đăng tải sách!", Toasty.LENGTH_SHORT).show();
                } else if (imageUris.size()>5) {
                    progressDialog.dismiss();
                    Toasty.warning(AddBookActivity.this, "Chỉ được chọn tối đa 5 hình ảnh!", Toasty.LENGTH_SHORT).show();
                } else if(TextUtils.isEmpty(edtTacgia.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtTieude.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtGia.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtSoLuong.getText().toString().trim())){
                    progressDialog.dismiss();
                    Toasty.info(AddBookActivity.this, "Vui lòng điền đầy đủ các trường thông tin!", Toasty.LENGTH_SHORT).show();
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
                                    Toasty.info(AddBookActivity.this, "Bạn chưa cập nhật địa chỉ của mình. Vui lòng cập nhật địa chỉ trước khi đăng bán sản phẩm!", Toasty.LENGTH_SHORT).show();
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
                                                if(bookResponse.getUser()!=null){
                                                        idBookPost = bookResponse.getId();
                                                        bookApiService.uploadFileImage(idBookPost, prepareFileParts("files", imageUris)).enqueue(new Callback<BookImageResponse>() {
                                                            @Override
                                                            public void onResponse(Call<BookImageResponse> call, Response<BookImageResponse> response) {
                                                                BookImageResponse responseBody = response.body();
                                                                if(responseBody!=null){

                                                                    if(responseBody.getEc().equals("0")){
                                                                        progressDialog.dismiss();
                                                                        Toasty.success(AddBookActivity.this, "Đã đăng sách thành công!", Toasty.LENGTH_SHORT).show();
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
                                                                    Toasty.error(AddBookActivity.this, "Đăng bán sách không thành công!", Toasty.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<BookImageResponse> call, Throwable t) {
                                                                progressDialog.dismiss();
                                                                String errorMessage = t.getMessage();
                                                                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                                                            }
                                                        });
                                                    }else{
                                                        progressDialog.dismiss();
                                                        Toasty.error(AddBookActivity.this, "Đăng bán sách không thành công!", Toasty.LENGTH_SHORT).show();
                                                    }

                                                }else {
                                                    progressDialog.dismiss();
                                                    Toasty.error(AddBookActivity.this, "Đăng bán sách không thành công!", Toasty.LENGTH_SHORT).show();
                                                }
                                        }
                                        @Override
                                        public void onFailure(Call<BookResponse> call, Throwable t) {
                                            progressDialog.dismiss();
                                            String errorMessage = t.getMessage();
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
                    Toasty.info(this, "Bạn chỉ được chọn tối đa 5 ảnh!", Toasty.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                    }
                    // Gọi phương thức để upload ảnh
                    reloadImageSlider(imageUris);
                }
            } else if (data.getData() != null) { // Chọn một ảnh
                Uri imageUri = data.getData();
                imageUris.add(imageUri);
                // Gọi phương thức để upload ảnh
                reloadImageSlider(imageUris);
            }
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