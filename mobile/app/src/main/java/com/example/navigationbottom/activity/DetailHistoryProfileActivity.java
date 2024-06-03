package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForCart;
import com.example.navigationbottom.adaper.BooksAdapterForHistory;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.order.GetOrderResponse;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailHistoryProfileActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BooksAdapterForHistory booksAdapter;
    private TextView soTienDaChiTieu;


    private float totalMoney;
    private ArrayList<Order> orders;

    private Order order;

    private OrderApiService orderApiService;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_profile);

        order =  (Order) getIntent().getSerializableExtra("order");
        user = UserPreferences.getUser(this);
        orderApiService = new OrderApiService(this);

        rvBooks = findViewById(R.id.rv_history_profile);
        soTienDaChiTieu = findViewById(R.id.tvSoTienDaChiTieu_HistoryProfileActivity);


        rvBooks.setHasFixedSize(true);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        orders = new ArrayList<>();
        loadHistory();
    }

    private void loadHistory() {

        orderApiService.getOrdersByUserPaid(user.getId()).enqueue(new Callback<GetOrderResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<GetOrderResponse> call, Response<GetOrderResponse> response) {
                GetOrderResponse getOrderResponse = response.body();
                if(getOrderResponse!=null){
                    Log.d("RequestData1", new Gson().toJson(getOrderResponse));
                    if(getOrderResponse.getEc().equals("0")){
                        List<Order> orderResponseList = getOrderResponse.getOrderResponseList();
                        for (Order order : orderResponseList) {
                            Order getOrder = new Order();
                            getOrder.setId(order.getId());
                            getOrder.setUser_id(order.getUser_id());
                            getOrder.setProduct_id(order.getProduct_id());
                            getOrder.setStatus(order.getStatus());
                            getOrder.setPayment_method(order.getPayment_method());
                            getOrder.setShipping_address(order.getShipping_address());
                            getOrder.setNumber_of_product(order.getNumber_of_product());
                            getOrder.setTotal_money(order.getTotal_money());
                            totalMoney+=order.getTotal_money();
                            orders.add(getOrder);
                        }
                        soTienDaChiTieu.setText((int)totalMoney*1000 + " VND");
                        booksAdapter = new BooksAdapterForHistory(orders, getApplicationContext());
                        rvBooks.setAdapter(booksAdapter);
                    }else{
                        Log.e("UploadError", "Upload failed with status: " + response.code());
                        try {
                            Log.e("UploadError", "Response error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }else{
                    Toasty.error(getApplicationContext(), "Order invalid", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOrderResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(getApplicationContext(), "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
    }
}