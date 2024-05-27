package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForHistory;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.model.Book;

import java.util.ArrayList;

public class DetailHistoryProfileActivity extends AppCompatActivity {

    private RecyclerView rvBooks;
    private BooksAdapterForHistory booksAdapter;
    private ArrayList<Book> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history_profile);

        rvBooks = findViewById(R.id.rv_history_profile);
        rvBooks.setHasFixedSize(true);
        rvBooks.setLayoutManager(new LinearLayoutManager(this));
        rvBooks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        books = new ArrayList<>();

        loadHistory();
    }

    private void loadHistory() {
        books.add(new Book(1L, "Book 1", "Author 1", 9.99f, "thumbnail1.jpg", 4.5f, "Available", "Description 1", 10));
        books.add(new Book(2L, "Book 2", "Author 2", 19.99f, "thumbnail2.jpg", 4.7f, "Available", "Description 2", 5));
        books.add(new Book(3L, "Book 3", "Author 3", 29.99f, "thumbnail3.jpg", 4.8f, "Available", "Description 3", 15));
        books.add(new Book(4L, "Book 4", "Author 4", 39.99f, "thumbnail4.jpg", 4.9f, "Available", "Description 4", 8));

        // Thiết lập adapter với danh sách sách
        booksAdapter = new BooksAdapterForHistory(books, this);
        rvBooks.setAdapter(booksAdapter);
    }
}