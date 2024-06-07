
package com.example.navigationbottom.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import com.example.navigationbottom.activity.AddBookActivity;
import com.example.navigationbottom.activity.MainActivity;
import com.example.navigationbottom.adaper.BooksForSellAdapter;
import com.example.navigationbottom.adaper.UserDataSingleton;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellFragment extends Fragment {

    private RecyclerView rvBooks;
    private BooksForSellAdapter booksAdapter;
    private ArrayList<Book> books;
    private FloatingActionButton btn_fab;
    private BookApiService bookApiService;
    private User user;
    private ProgressDialog progressDialog;
    private View mView;
    public SellFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sell, container, false);

        btn_fab = mView.findViewById(R.id.btn_fab_Sell);
        rvBooks = mView.findViewById(R.id.rv_sell_fragment);


        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragmentToolbar(view);


        progressDialog = new ProgressDialog(getActivity());

        rvBooks.setHasFixedSize(true);

        rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvBooks.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        user = UserPreferences.getUser(getContext());

//        booksAdapter = new BooksForSellAdapter(books, getActivity());
//        rvBooks.setAdapter(booksAdapter);

        books = new ArrayList<>();
        bookApiService = new BookApiService(getContext());
        bookApiService.getBookByUserId(user.getId()).enqueue(new Callback<GetBookResponse>() {
            @Override
            public void onResponse(Call<GetBookResponse> call, Response<GetBookResponse> response) {
                GetBookResponse getBookResponse = response.body();
                if(getBookResponse!=null){
                    Log.d("RequestData1", new Gson().toJson(getBookResponse));
                    if(getBookResponse.getEc().equals("0")){
                        List<Book> productResponseList = getBookResponse.getProductResponseList();

                        // Kiểm tra dữ liệu
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
                        booksAdapter = new BooksForSellAdapter(books, getActivity());
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
            public void onFailure(Call<GetBookResponse> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(getContext(), "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });



        btn_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddBookActivity.class));
            }
        });
    }




    private void setFragmentToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar_Sell);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
    }

}