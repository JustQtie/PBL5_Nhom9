package com.example.navigationbottom.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailsHomeActivity;
import com.example.navigationbottom.activity.ItemHistoryActivity;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.Order;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.BookApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BooksAdapterForHistory extends RecyclerView.Adapter<BooksAdapterForHistory.BookViewHolder>{

    private Context mContext;
    public static ArrayList<Order> orders;

    public BookApiService bookApiService;

    private Book book;

    private ExecutorService executorService;
    private Handler mainHandler;

    public BooksAdapterForHistory(ArrayList<Order> orders, Context mContext) {
        this.orders = orders;
        this.mContext = mContext;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }



    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BooksAdapterForHistory.BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapterForHistory.BookViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Order order = orders.get(position);
        bookApiService = new BookApiService(mContext.getApplicationContext());
        if(order == null){
            return;
        }

        bookApiService.getBookById(order.getProduct_id()).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                book = response.body();
                if(book != null){
                    Log.d("RequestData1", new Gson().toJson(book));
                    fillBook(holder, position);
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toasty.error(mContext.getApplicationContext(), "Request failed: " + errorMessage, Toasty.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });



    }

    private void fillBook(@NonNull BooksAdapterForHistory.BookViewHolder holder, @SuppressLint("RecyclerView") int position){
        Callable<Category> callable = new Callable<Category>() {
            @Override
            public Category call() throws Exception {
                CategoryApiService categoryApiService = new CategoryApiService(mContext.getApplicationContext());
                Call<Category> call = categoryApiService.getCategoriesById(book.getCategory_id());
                Response<Category> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body();
                } else {
                    throw new Exception("Failed to get category with status: " + response.code());
                }
            }
        };

        Future<Category> future = executorService.submit(callable);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    final Category category = future.get();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvLoai.setText(category.getName());
                        }
                    });
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.tvLoai.setText("Unknown Category");
                        }
                    });
                }
            }
        });

        try {
            String imageUrl = ApiService.BASE_URL + "api/v1/products/images/" + book.getThumbnail();
            Log.d("url_img", imageUrl);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.baseline_menu_book_24) // Ảnh tạm thời khi đang tải
                        .error(R.drawable.baseline_menu_book_24) // Ảnh hiển thị khi có lỗi
                        .into(holder.ivItem);
            } else {
                Glide.with(holder.itemView.getContext())
                        .load(R.drawable.baseline_menu_book_24)
                        .into(holder.ivItem);
            }
        } catch (Exception e) {
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.baseline_menu_book_24)
                    .into(holder.ivItem);
        }

        holder.tvLoai.setText(book.getCategoryName());
        holder.tvGia.setText(String.valueOf(orders.get(position).getTotal_money()));
        holder.tvNguoiBan.setText(book.getAuthor());
        holder.tvSoLuong.setText(orders.get(position).getNumber_of_product()+"");
        holder.tvTieude.setText(book.getName());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nho chuyen id qua de goi API
                Intent intent = new Intent(mContext, ItemHistoryActivity.class);
                intent.putExtra("book", book);
                intent.putExtra("order", orders.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
     }

    @Override
    public int getItemCount() {
        if(orders != null){
            return orders.size();
        }
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView ivItem;
        private TextView tvTieude, tvNguoiBan, tvSoLuong, tvLoai, tvGia;
        private RelativeLayout layoutItem;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item_book);
            ivItem = itemView.findViewById(R.id.iv_itemBook);
            tvTieude = itemView.findViewById(R.id.tv_tieude_itemBook);
            tvNguoiBan = itemView.findViewById(R.id.tv_nguoiban_itemBook);
            tvSoLuong = itemView.findViewById(R.id.tv_soluong_itemBook);
            tvLoai = itemView.findViewById(R.id.tv_loai_itemBook);
            tvGia = itemView.findViewById(R.id.tv_gia_itemBook);
        }
    }
}
