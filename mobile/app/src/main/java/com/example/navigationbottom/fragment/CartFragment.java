package com.example.navigationbottom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForCart;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.model.Book;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    private RecyclerView rvBooks;
    private BooksAdapterForCart booksAdapter;
    private ArrayList<Book> books;
    private View mView;
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

        books = new ArrayList<>();
        books.add(new Book("1", "Lập Trình Java", "Nguyễn Văn A", "Programming", "1", "Sách hướng dẫn lập trình Java cơ bản", "50000", ""));
        books.add(new Book("2", "Học Python", "Trần Văn B", "Programming", "1", "Sách hướng dẫn lập trình Python", "60000", ""));
        getBooks();

        return mView;
    }

    private void getBooks() {
        booksAdapter = new BooksAdapterForCart(books, getActivity());
        rvBooks.setAdapter(booksAdapter);
    }


}