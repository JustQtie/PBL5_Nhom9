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
import com.example.navigationbottom.adaper.BooksAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.User;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView rvBooks;
    private BooksAdapter booksAdapter;
    private ArrayList<Book> books;
    private View mView;
    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_home, container, false);

        rvBooks = mView.findViewById(R.id.rv_Home);
        rvBooks.setHasFixedSize(true);

        rvBooks.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvBooks.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        books = new ArrayList<>();
        books.add(new Book("1", "Lập Trình Java", "Nguyễn Văn A", "Programming", "10", "Sách hướng dẫn lập trình Java cơ bản", "50000", ""));
        books.add(new Book("2", "Học Python", "Trần Văn B", "Programming", "15", "Sách hướng dẫn lập trình Python", "60000", ""));
        books.add(new Book("3", "C++ Cơ Bản", "Lê Thị C", "Programming", "20", "Sách cơ bản về lập trình C++", "70000", ""));
        books.add(new Book("4", "Thiết Kế Web", "Phạm Văn D", "Web Development", "25", "Hướng dẫn thiết kế web từ đầu", "55000", ""));
        books.add(new Book("5", "JavaScript Nâng Cao", "Nguyễn Thị E", "Programming", "18", "Sách nâng cao về JavaScript", "65000", ""));
        books.add(new Book("6", "Học SQL", "Trần Thị F", "Database", "22", "Hướng dẫn học SQL cơ bản và nâng cao", "60000", ""));
        books.add(new Book("7", "Dữ Liệu Lớn", "Lê Văn G", "Data Science", "30", "Giới thiệu về Big Data", "80000", ""));
        books.add(new Book("8", "Học AI", "Phạm Thị H", "Artificial Intelligence", "12", "Sách về trí tuệ nhân tạo", "75000", ""));
        books.add(new Book("9", "Machine Learning", "Nguyễn Văn I", "Data Science", "14", "Sách về học máy", "85000", ""));
        books.add(new Book("10", "Khoa Học Dữ Liệu", "Trần Văn K", "Data Science", "16", "Nhập môn khoa học dữ liệu", "90000", ""));
        getAllBook();


        return mView;
    }

    private void getAllBook() {

        booksAdapter = new BooksAdapter(books, getActivity());
        rvBooks.setAdapter(booksAdapter);
    }

}