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
        books.add(new Book(1L, "Lập Trình Java", "Nguyễn Văn A", 50000f, "java_thumbnail.png", 4.5f, "Available", "Sách hướng dẫn lập trình Java cơ bản", 1));
        books.add(new Book(2L, "Học Python", "Trần Văn B", 60000f, "python_thumbnail.png", 4.7f, "Available", "Sách hướng dẫn lập trình Python", 1));
        books.add(new Book(3L, "Lập Trình C++", "Lê Thị C", 45000f, "cpp_thumbnail.png", 4.3f, "Available", "Sách hướng dẫn lập trình C++", 1));
        books.add(new Book(4L, "Thiết Kế Web", "Phạm Văn D", 55000f, "web_thumbnail.png", 4.6f, "Available", "Sách hướng dẫn thiết kế website", 1));
        getBooks();

        return mView;
    }

    private void getBooks() {
        booksAdapter = new BooksAdapterForCart(books, getActivity());
        rvBooks.setAdapter(booksAdapter);
    }


}