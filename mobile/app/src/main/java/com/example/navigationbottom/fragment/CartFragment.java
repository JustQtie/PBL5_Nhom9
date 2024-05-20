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
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.viewmodel.OrderApiService;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CartFragment extends Fragment {
    private RecyclerView rvBooks;
    private BooksAdapterForCart booksAdapter;
    private ArrayList<Book> books;
    private View mView;

    private OrderApiService orderApiService;
    public CartFragment() {

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
        books = new ArrayList<>();

        orderApiService.getBookSaving().enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                GetBookResponse getBookResponse = response.body();
                if(getBookResponse!=null){
                    Log.d("RequestData1", new Gson().toJson(getBookResponse));
                    if(getBookResponse.getEc().equals("0")){
                        List<Book> productResponseList = getBookResponse.getProductResponseList();
                        for (Book book : productResponseList) {
                            Book getBook = new Book();
                            getBook.setId(book.getId());
                            getBook.setName(book.getName());
                            getBook.setAuthor(book.getAuthor());
                            getBook.setPoint(book.getPoint());
                            getBook.setDescription(book.getDescription());
                            getBook.setStatus(book.getStatus());
                            getBook.setQuantity(book.getQuantity());
                            getBook.setThumbnail(book.getThumbnail());
                            getBook.setPrice(book.getPrice());
                            getBook.setUser_id(book.getUser_id());
                            getBook.setCategory_id(book.getCategory_id());
                            books.add(getBook);
                        }
                        booksAdapter = new BooksAdapterForCart(books, getActivity());
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
                    Toast.makeText(getContext(), "Book invalid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });


        return mView;
    }


}