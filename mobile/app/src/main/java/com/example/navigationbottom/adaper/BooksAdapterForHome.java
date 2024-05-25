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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailsHomeActivity;
import com.example.navigationbottom.model.Book;

import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.CategoryApiService;
import com.google.android.material.imageview.ShapeableImageView;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import retrofit2.Call;
import retrofit2.Response;


public class BooksAdapterForHome extends RecyclerView.Adapter<BooksAdapterForHome.BookViewHolder> implements Filterable {
    private Context mContext;
    public static ArrayList<Book> books;
    public static ArrayList<Book> booksCopy;
    private ExecutorService executorService;
    private Handler mainHandler;
    public BooksAdapterForHome(ArrayList<Book> books, Context mContext) {
        this.books = books;
        this.booksCopy = new ArrayList<>(books);
        this.mContext = mContext;
        this.executorService = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
        loadCategoryNames();
    }

    private void loadCategoryNames() {
        for (Book book : books) {
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
                        book.setCategoryName(category.getName());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        book.setCategoryName("Unknown Category");
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public BooksAdapterForHome.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapterForHome.BookViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Book book = books.get(position);
        if(book == null){
            return;
        }



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
        holder.tvGia.setText(book.getPrice() + " VND");
        holder.tvNguoiBan.setText(book.getAuthor());
        holder.tvSoLuong.setText(book.getQuantity()+"");
        holder.tvTieude.setText(book.getName());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nho chuyen id qua de goi API
                Intent intent = new Intent(mContext, DetailsHomeActivity.class);
                intent.putExtra("book", books.get(position));
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        if(books != null){
            return books.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString().toLowerCase();
                List<Book> filteredBooks = new ArrayList<>();


                if (input.isEmpty()) {
                    filteredBooks.addAll(booksCopy);
                } else {
                    for (Book bb : booksCopy) {

                        String normalizedNoiDungName = removeAccents(bb.getName().toLowerCase());
                        String normalizedNoiDungAuthor = removeAccents(bb.getAuthor().toLowerCase());
                        String normalizedNoiDungCategory = removeAccents(bb.getCategoryName().toLowerCase());
                        String normalizedSearchText = removeAccents(input).toLowerCase();

                        if (normalizedNoiDungName.contains(normalizedSearchText) ||
                                normalizedNoiDungAuthor.contains(normalizedSearchText) ||
                                normalizedNoiDungCategory.contains(normalizedSearchText)) {
                            filteredBooks.add(bb);
                        }
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredBooks;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                books.clear();
                books.addAll((List<Book>) results.values);
                notifyDataSetChanged();
            }
        };
    }


    // Hàm để loại bỏ dấu tiếng Việt
    public String removeAccents(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalized;
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
