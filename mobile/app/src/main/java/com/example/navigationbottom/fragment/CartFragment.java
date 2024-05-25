package com.example.navigationbottom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForCart;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.GetBookResponse;
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


public class CartFragment extends Fragment {
    private RecyclerView rvBooks;
    private BooksAdapterForCart booksAdapter;

    private ArrayList<Order> orders;
    private View mView;

    private OrderApiService orderApiService;

    public CartFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_cart, container, false);

        rvBooks = mView.findViewById(R.id.rv_cart);
        rvBooks.setHasFixedSize(true);

        rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvBooks.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        orderApiService = new OrderApiService(getContext());
        orders = new ArrayList<>();
        User user = UserPreferences.getUser(getContext());

        orderApiService.getOrdersByUserNotPaid(user.getId()).enqueue(new Callback<GetOrderResponse>() {
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
                            orders.add(getOrder);
                        }
                        booksAdapter = new BooksAdapterForCart(orders, getActivity());
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
                    Toasty.error(getContext(), "Book invalid", Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetOrderResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(getContext(), "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });


        return mView;
    }


}