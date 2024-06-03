package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.utils.NotifyStatus;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.BookImageApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.NotifyApplication;
import com.example.navigationbottom.viewmodel.NotifyServiceApi;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationConfirmActivity extends AppCompatActivity {
    private TextView txtTieuDe, txtStatus, txtGia, txtMota, txtSoLuong, txtAuthor, txtDiaChiNguoiMua, txtSoDienThoaiNguoiMua, txtTenNguoiDung, txtLoai, txtThanhToan, txtTongTien;

    private AppCompatButton btnXacNhan, btnHuy;

    private Notification notification;

    private UserApiService userApiService;

    private OrderApiService orderApiService;
    private BookApiService bookApiService;

    private CategoryApiService categoryApiService;

    private NotifyServiceApi notifyServiceApi;

    List<String> imageUrlString = new ArrayList<>();

    private BookImageApiService bookImageApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_confirm);
        init();

        notification = (Notification) getIntent().getSerializableExtra("notifications");
        orderApiService = new OrderApiService(this);
        userApiService = new UserApiService(this);
        bookApiService = new BookApiService(this);
        categoryApiService = new CategoryApiService(this);
        bookImageApiService = new BookImageApiService(this);
        notifyServiceApi = new NotifyServiceApi(this);

        orderApiService.getOrder(notification.getOrder_id()).enqueue(new Callback<Order>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order order = response.body();
                if(order.getEC().equals("0")){
                    txtThanhToan.setText(order.getPayment_method());
                    txtSoLuong.setText(order.getNumber_of_product()+"");
                    txtTongTien.setText(order.getTotal_money()+"");
                    userApiService.getUser(order.getUser_id()).enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            if(user != null){
                                txtTenNguoiDung.setText(user.getFullname());
                                txtSoDienThoaiNguoiMua.setText(user.getPhone_number());
                                txtDiaChiNguoiMua.setText(user.getAddress());
                                bookApiService.getBookById(order.getProduct_id()).enqueue(new Callback<Book>() {
                                    @Override
                                    public void onResponse(Call<Book> call, Response<Book> response) {
                                        Book book = response.body();
                                        if(book.getEc().equals("0")){
                                            txtTieuDe.setText(book.getName());
                                            txtAuthor.setText(book.getAuthor());
                                            txtGia.setText(book.getPrice() + "");
                                            txtMota.setText(book.getDescription());
                                            txtStatus.setText(book.getStatus());
                                            fillImageSlide(book.getId());
                                            categoryApiService.getCategoriesById(book.getCategory_id()).enqueue(new Callback<Category>() {
                                                @Override
                                                public void onResponse(Call<Category> call, Response<Category> response) {
                                                    Category category = response.body();
                                                    if(category != null){
                                                        txtLoai.setText(category.getName());
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<Category> call, Throwable t) {
                                                    Log.e("Error", "Not load category");
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Book> call, Throwable t) {
                                        Log.e("Error", "Not load book");
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Error", "Not load user");
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("Error", "Not load order");
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.setStatus(NotifyStatus.AGREE);
                NotifyApplication notifyApplication = NotifyApplication.instance();
                notifyApplication.getStompClient().send("/app/res_notify", new Gson().toJson(notification)).subscribe();
                deleteNotify(notification.getId());
                Intent intent = new Intent(NotificationConfirmActivity.this, MainActivity.class);
                intent.putExtra("dataFromActivity", "fromNotifyConfirm");
                startActivity(intent);
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.setStatus(NotifyStatus.CANCEL);
                NotifyApplication notifyApplication = NotifyApplication.instance();
                notifyApplication.getStompClient().send("/app/res_notify", new Gson().toJson(notification)).subscribe();
                deleteNotify(notification.getId());
                Intent intent = new Intent(NotificationConfirmActivity.this, MainActivity.class);
                intent.putExtra("dataFromActivity", "fromNotifyConfirm");
                startActivity(intent);
            }
        });

    }

    public void deleteNotify(Long id){
        notifyServiceApi.deleteNotify(id).enqueue(new Callback<Notification>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Notification notification1 = response.body();
                if(notification1.getEc().equals("0")){
                    Log.d("RequestData1", new Gson().toJson(notification1));
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }

    private void fillImageSlide(Long bookId){
        // Chuyển đổi danh sách Uri thành danh sách đường dẫn chuỗi
        bookImageApiService = new BookImageApiService(this);
        bookImageApiService.getThumbnailsByProductId(bookId).enqueue(new Callback<List<String>>() {
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
                Toasty.error(NotificationConfirmActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }



    private void init() {
        txtTieuDe = findViewById(R.id.tv_tieuDe_notification_confirm);
        txtStatus = findViewById(R.id.tv_status_detail_notification_confirm);
        txtGia = findViewById(R.id.tv_gia_notification_confirm);
        txtSoLuong = findViewById(R.id.tv_soLuong_notification_confirm);
        txtAuthor = findViewById(R.id.tv_tacGia_notification_confirm);
        txtDiaChiNguoiMua = findViewById(R.id.tv_diaChiNguoiDatHang_notification_confirm);
        txtSoDienThoaiNguoiMua = findViewById(R.id.tv_soDienThoaiNguoiDatHang_notification_confirm);
        txtTenNguoiDung = findViewById(R.id.tv_tenNguoiDatHang_notification_confirm);
        txtLoai = findViewById(R.id.tv_loai_notification_confirm);
        txtThanhToan = findViewById(R.id.tv_PhuongThucthanhtoan_notification_confirm);
        txtTongTien = findViewById(R.id.tv_tongTien_notification_confirm);
        txtMota = findViewById(R.id.tv_moTa_notification_confirm);
        btnXacNhan = findViewById(R.id.btn_xacNhan_notification_confirm);
        btnHuy = findViewById(R.id.btn_huy_notification_confirm);
    }


}