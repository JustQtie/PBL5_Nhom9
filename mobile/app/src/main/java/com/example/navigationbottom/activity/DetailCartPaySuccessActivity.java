package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.SliderAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.CreateOrder;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.SliderData;
import com.example.navigationbottom.response.book.BookResponse;
import com.example.navigationbottom.utils.OrderStatus;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.BookImageApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.NotifyApplication;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;



public class DetailCartPaySuccessActivity extends AppCompatActivity {

    private TextView txtTieuDe, txtStatus, txtGia, txtMota, txtSoLuong, txtAuthor, txtDiaChi, txtLoai, txtThanhToan, txtTongTien;

    private AppCompatButton btnTaoThanhToan, btnZalo;

    private BookApiService bookApiService;

    private CategoryApiService categoryApiService;

    private OrderApiService orderApiService;

    private BookImageApiService bookImageApiService;

    private Book book;

    private Order order;
    private Long orderId;

    private ProgressDialog progressDialog;

    List<String> imageUrlString = new ArrayList<>();

    String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_cart_pay_success);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        progressDialog = new ProgressDialog(DetailCartPaySuccessActivity.this);
        init();

        progressDialog = new ProgressDialog(DetailCartPaySuccessActivity.this);
        book = (Book) getIntent().getSerializableExtra("book");
        orderId =  getIntent().getLongExtra("order", -1);

        bookApiService = new BookApiService(this);
        categoryApiService = new CategoryApiService(this);
        orderApiService = new OrderApiService(this);

        fillImageSlide();

        if (book != null && orderId != -1) {
            txtTieuDe.setText(book.getName());
            txtGia.setText(book.getPrice().toString() + " VND");
            amount = String.valueOf(Float.parseFloat(book.getPrice().toString()) * 1000);
            txtStatus.setText(book.getStatus());
            txtAuthor.setText(book.getAuthor());
            txtMota.setText(book.getDescription());

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

            orderApiService.getOrder(orderId).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    order = response.body();
                    if(order != null){
                        if(order.getEC().equals("0")){
                            txtDiaChi.setText(order.getShipping_address());
                            txtSoLuong.setText(String.valueOf(order.getNumber_of_product()));
                            txtThanhToan.setText(order.getPayment_method());
                            txtTongTien.setText(String.valueOf(order.getTotal_money()));
                        }
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.e("Error", "Not load order");
                }
            });
        }

        btnTaoThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnZalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("ZaloPay...");
                progressDialog.show();
                requestZalo();
            }
        });

    }

    public void init(){
        txtTieuDe = findViewById(R.id.tv_tieuDe_cart_pay_success_activity);
        txtSoLuong = findViewById(R.id.tv_soLuong_detail_cart_pay_success_activity);
        txtStatus = findViewById(R.id.tv_status_detail_cart_pay_success_activity);
        txtGia = findViewById(R.id.tv_gia_detail_cart_pay_success_activity);
        txtMota = findViewById(R.id.tv_moTa_detail_cart_pay_success_activity);
        txtAuthor = findViewById(R.id.tv_tacGia_detail_cart_pay_success_activity);
        txtDiaChi = findViewById(R.id.tv_diaChiGiao_detail_cart_pay_success_activity);
        txtLoai = findViewById(R.id.tv_loai_cart_pay_success_activity);
        txtThanhToan = findViewById(R.id.tv_PhuongThucthanhtoan_detail_cart_pay_success_activity);
        txtTongTien = findViewById(R.id.tv_tongTien_detail_cart_pay_success_activity);
        btnTaoThanhToan = findViewById(R.id.btn_thanhtoan_cart_pay_success_activity);
        btnZalo = findViewById(R.id.btn_thanhtoan_zalopay);
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
                Toasty.error(DetailCartPaySuccessActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }

    private void requestZalo(){
        CreateOrder orderApi = new CreateOrder();
        try {
            amount = amount.replaceAll("\\.0$", "");
            Log.e("amount", amount);
            JSONObject data = orderApi.createOrder(amount);
            String code = data.getString("return_code");
            if (code.equals("1")) {
                String token  = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(DetailCartPaySuccessActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        progressDialog.dismiss();
                        Toasty.success(DetailCartPaySuccessActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                        CompletedOrder();
                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        Toasty.warning(DetailCartPaySuccessActivity.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        Toasty.error(DetailCartPaySuccessActivity.this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }

    private void CompletedOrder(){
        order.setStatus(OrderStatus.PAID);
        orderApiService.updateOrder(orderId, order).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Order order = response.body();
                if(order != null){
                    NotifyApplication notifyApplication = NotifyApplication.instance();
                    notifyApplication.getStompClient().send("/app/success_order_notify", new Gson().toJson(order)).subscribe();
                    Log.d("RequestData1", new Gson().toJson(order));
                    Intent intent = new Intent(DetailCartPaySuccessActivity.this, DetailHistoryProfileActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(DetailCartPaySuccessActivity.this, "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }
}