
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.AddBookActivity;
import com.example.navigationbottom.activity.MainActivity;
import com.example.navigationbottom.adaper.BooksAdapter;
import com.example.navigationbottom.adaper.BooksForSellAdapter;
import com.example.navigationbottom.model.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SellFragment extends Fragment {

    private RecyclerView rvBooks;
    private BooksForSellAdapter booksAdapter;
    private ArrayList<Book> books;
    private FloatingActionButton btn_fab;
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
        progressDialog = new ProgressDialog(getActivity());

        rvBooks.setHasFixedSize(true);

        rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvBooks.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        books = new ArrayList<>();
        books.add(new Book("1", "Lập Trình Java", "Hồ Văn Thảo", "Programming", "10", "Sách hướng dẫn lập trình Java cơ bản", "50000", ""));
        books.add(new Book("2", "Học Python", "Hồ Văn Thảo", "Programming", "15", "Sách hướng dẫn lập trình Python", "60000", ""));
        books.add(new Book("3", "C++ Cơ Bản", "Hồ Văn Thảo", "Programming", "20", "Sách cơ bản về lập trình C++", "70000", ""));
        getAllBook();

        return mView;
    }

    private void getAllBook() {
        booksAdapter = new BooksForSellAdapter(books, getActivity());
        rvBooks.setAdapter(booksAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setFragmentToolbar(view);

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