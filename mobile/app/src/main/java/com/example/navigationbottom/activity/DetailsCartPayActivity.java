package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.utils.OrderStatus;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.example.navigationbottom.viewmodel.WebSocketManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsCartPayActivity extends AppCompatActivity {

    private TextView tvTenNguoiDatHang, tvSoDienThoaiNguoiDatHang, tvDiaChiNguoiDatHang, tvTieuDe, tvTacGia, tvLoai, tvGia, tvStatus, tvSoLuong, tvTongTien;
    private EditText edtSoLuong;
    private AppCompatButton btnTaoGiaoDich;
    private Spinner spinnerPhuongThucThanhToan;
    private ProgressDialog progressDialog;

    private UserApiService userApiService;

    private BookApiService bookApiService;

    private CategoryApiService categoryApiService;

    private OrderApiService orderApiService;

    private Book book;

    private Long orderId;

    private User user;

    String selectedItem;

    private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_cart_pay);

        khoitao();

        webSocketManager = WebSocketManager.getInstance();
        progressDialog = new ProgressDialog(DetailsCartPayActivity.this);

        book = (Book) getIntent().getSerializableExtra("book");
        orderId =  getIntent().getLongExtra("order", -1);
        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.thanhToan_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPhuongThucThanhToan.setAdapter(adapter);

        spinnerPhuongThucThanhToan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy giá trị được chọn từ Spinner
                selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Bạn đang chọn phương thức thanh toán là " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        userApiService = new UserApiService(this);
        bookApiService = new BookApiService(this);
        categoryApiService = new CategoryApiService(this);
        orderApiService = new OrderApiService(this);

        User exitsuser = UserPreferences.getUser(this);
        user = new User();
        userApiService.getUser(exitsuser.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                if(user.getEc().equals("0")){
                    tvTenNguoiDatHang.setText(user.getFullname());
                    tvSoDienThoaiNguoiDatHang.setText(user.getPhone_number());
                    tvDiaChiNguoiDatHang.setText(user.getAddress());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Error", "Not load user");
            }
        });

        if (book != null) {
            tvTieuDe.setText(book.getName());
            tvGia.setText(book.getPrice().toString() + " VND");
            tvStatus.setText(book.getStatus());
            tvSoLuong.setText(String.valueOf(book.getQuantity()));
            tvTacGia.setText(book.getAuthor());

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
                            calculateTotal();
                        }
                    } catch (NumberFormatException e) {
                        edtSoLuong.setError("Invalid number");
                    }
                }
            });
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

        btnTaoGiaoDich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Order...");
                progressDialog.show();
                if(Integer.parseInt(edtSoLuong.getText().toString()) > book.getQuantity()){
                    progressDialog.dismiss();
                    Toast.makeText(DetailsCartPayActivity.this, "The current number of books is not enough", Toast.LENGTH_SHORT).show();
                }else{
                    Order requestOrder = new Order();
                    requestOrder.setProduct_id(book.getId());
                    requestOrder.setUser_id(user.getId());
                    requestOrder.setStatus(OrderStatus.PENDING);
                    requestOrder.setShipping_address(user.getAddress());
                    requestOrder.setNumber_of_product(Integer.parseInt((edtSoLuong.getText().toString())));
                    requestOrder.setPayment_method(selectedItem);
                    requestOrder.setTotal_money(getMoney(tvTongTien.getText().toString()));
                    orderApiService.updateOrder(orderId, requestOrder).enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order order = response.body();
                            if(order != null){
                                progressDialog.dismiss();
                                webSocketManager.sendOrder(order, "/app/order");
                                Log.d("RequestData1", new Gson().toJson(order));
                                Intent intent = new Intent(DetailsCartPayActivity.this, MainActivity.class);
                                intent.putExtra("dataFromActivity", "fromDetailCart");
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            progressDialog.dismiss();
                            String errorMessage = t.getMessage();
                            Toast.makeText(DetailsCartPayActivity.this, "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                        }
                    });
                }

            }
        });
    }

    private float getMoney(String money){
        String numberStr = money.replaceAll("[^\\d.]", "");

        try {
            // Chuyển đổi chuỗi thành số thực (double)
            float number = Float.parseFloat(numberStr);
            return number;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private void calculateTotal() {
        String soLuongStr = edtSoLuong.getText().toString();
        int soLuong = 0;

        if (!soLuongStr.isEmpty()) {
            soLuong = Integer.parseInt(soLuongStr);
        }

        float tongTien = soLuong * getMoney(tvGia.getText().toString());
        tvTongTien.setText(tongTien + " VND");
    }

    private void khoitao() {
        tvTenNguoiDatHang = findViewById(R.id.tv_tenNguoiDatHang_detail_cart_pay_activity);
        tvSoDienThoaiNguoiDatHang = findViewById(R.id.tv_soDienThoaiNguoiDatHang_detail_cart_pay_activity);
        tvDiaChiNguoiDatHang = findViewById(R.id.tv_diaChiNguoiDatHang_detail_cart_pay_activity);
        tvTieuDe = findViewById(R.id.tv_tieuDe_detail_cart_pay_activity);
        tvTacGia = findViewById(R.id.tv_tacGia_detail_cart_pay_activity);
        tvLoai = findViewById(R.id.tv_loai_detail_cart_pay_activity);
        tvGia = findViewById(R.id.tv_gia_detail_cart_pay_activity);
        tvStatus = findViewById(R.id.tv_status_detail_cart_pay_activity);
        tvSoLuong = findViewById(R.id.tv_soluong_detail_cart_pay_activity);
        tvTongTien = findViewById(R.id.tv_tongTien_detail_cart_pay_activity);
        edtSoLuong = findViewById(R.id.edt_soLuong_detail_cart_pay_activity);
        btnTaoGiaoDich = findViewById(R.id.btn_taogiaodich_cart_pay_activity);
        spinnerPhuongThucThanhToan = findViewById(R.id.spinner_PhuongThucthanhtoan_detail_cart_pay_activity);
    }
}