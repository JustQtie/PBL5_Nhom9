package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.utils.OrderStatus;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookImageApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.smarteist.autoimageslider.SliderView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsHomeActivity extends AppCompatActivity {


    private TextView tvTieude, tvGia, tvNguoiban, tvMota, tvLoai, tvStatus, tvSoluong, tvAuthor;
    private AppCompatButton btnReport, btnOrder, btnSave;
    private ImageView btnBack;


    private ProgressDialog progressDialog;

    private UserApiService userApiService;

    private BookImageApiService bookImageApiService;

    private CategoryApiService categoryApiService;

    private OrderApiService orderApiService;

    private Book book;

    List<String> imageUrlString = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_home);

        //khoi tao
        initView();

        progressDialog = new ProgressDialog(DetailsHomeActivity.this);

        book = (Book) getIntent().getSerializableExtra("book");

        fillImageSlide();

        orderApiService = new OrderApiService(this);
        userApiService = new UserApiService(this);
        categoryApiService = new CategoryApiService(this);
        userApiService.getUser(book.getUser_id()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user.getEc().equals("0")){
                    tvNguoiban.setText(user.getFullname() + " - " + user.getPhone_number());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Not load user");
            }
        });


        if (book != null) {
            tvTieude.setText(book.getName());

            tvMota.setText(book.getDescription());
            tvGia.setText(book.getPrice() + "VND");
            tvStatus.setText(book.getStatus());
            tvSoluong.setText(String.valueOf(book.getQuantity()));
            tvAuthor.setText(book.getAuthor());

            categoryApiService.getCategoriesById(book.getCategory_id()).enqueue(new Callback<Category>() {
                @Override
                public void onResponse(Call<Category> call, Response<Category> response) {
                    Category category = response.body();
                    if(category != null){
                        tvLoai.setText(category.getName());
                    }
                }
                @Override
                public void onFailure(Call<Category> call, Throwable t) {
                    Log.e("Error", "Not load category");
                }
            });
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Save...");
                progressDialog.show();
                Order order = new Order();
                order.setProduct_id(book.getId());
                order.setStatus(OrderStatus.SAVING);
                createOrder(order);
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Order...");
                progressDialog.show();
                Order order = new Order();
                order.setProduct_id(book.getId());
                order.setStatus(OrderStatus.PENDING);
                createOrder(order);
            }
        });
    }

    private void createOrder(Order order){
        User user1 = UserPreferences.getUser(getApplicationContext());
        userApiService.getUser(user1.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if(user.getEc().equals("0")){
                    if(user.getAddress() == null){
                        progressDialog.dismiss();
                        Toasty.warning(DetailsHomeActivity.this, "You need to add an address before order", Toasty.LENGTH_SHORT).show();
                    }else{
                        order.setUser_id(user.getId());
                        order.setShipping_address(user.getAddress());
                        if(book.getQuantity() > 0){
                            orderApiService.createOrder(order).enqueue(new Callback<Order>() {
                                @Override
                                public void onResponse(Call<Order> call, Response<Order> response) {
                                    Order order = response.body();
                                    if(order.getEC().equals("0")){
                                        if(order.getStatus().equals(OrderStatus.SAVING)){
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(DetailsHomeActivity.this, MainActivity.class);
                                            intent.putExtra("dataFromActivity", "fromDetailHome");
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(DetailsHomeActivity.this, DetailsCartPayActivity.class);
                                            intent.putExtra("book", book);
                                            intent.putExtra("order", order.getId());
                                            startActivity(intent);
                                            finish();
                                        }

                                    }else{
                                        progressDialog.dismiss();
                                        Log.e("UploadError", "Upload failed with status: " + response.code());
                                        try {
                                            Log.e("UploadError", "Response error body: " + response.errorBody().string());
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        Toasty.error(DetailsHomeActivity.this, "Create order fails", Toasty.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Order> call, Throwable t) {
                                    progressDialog.dismiss();
                                    String errorMessage = t.getMessage();
                                    Toasty.error(DetailsHomeActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                                    Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                                }
                            });
                        }else{
                            Toasty.warning(DetailsHomeActivity.this,"Hiện tại sách giáo trình này đang hết hàng", Toasty.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Not load user");
            }
        });
    }

    private void initView() {
        tvTieude = findViewById(R.id.tv_activitydetail_tieude);
        tvGia = findViewById(R.id.tv_activitydetail_Gia);
        tvNguoiban = findViewById(R.id.tv_activitydetail_ngBan);
        tvMota = findViewById(R.id.tv_activitydetail_Mota);
        tvLoai = findViewById(R.id.tv_activitydetail_loai);

        tvSoluong = findViewById(R.id.tv_activitydetail_soluong);
        tvStatus = findViewById(R.id.tv_activitydetail_trangthai);
        tvAuthor = findViewById(R.id.tv_activitydetail_tacGia);

        btnSave = findViewById(R.id.btn_activitydetail_save);
        btnReport = findViewById(R.id.btn_activitydetail_Report);
        btnOrder = findViewById(R.id.btn_activitydetail_Order);
        btnBack = findViewById(R.id.iv_back_mapAc);
    }

    private void fillImageSlide(){
        // Chuyển đổi danh sách Uri thành danh sách đường dẫn chuỗi
        bookImageApiService = new BookImageApiService(this);
        bookImageApiService.getThumbnailsByProductId(book.getId()).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if(response.isSuccessful()){
                    imageUrlString = response.body();
                    ArrayList<SliderData> sliderDataArrayList = new ArrayList<>();

                    // initializing the slider view.
                    SliderView sliderView = findViewById(R.id.slider);

                    // adding the urls inside array list
                    for (String path : imageUrlString) {
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
                Toasty.error(DetailsHomeActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }
}