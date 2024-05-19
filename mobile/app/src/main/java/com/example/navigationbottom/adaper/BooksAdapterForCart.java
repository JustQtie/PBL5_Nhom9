package com.example.navigationbottom.adaper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailsCartPayActivity;
import com.example.navigationbottom.model.Book;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BooksAdapterForCart extends RecyclerView.Adapter<BooksAdapterForCart.BookViewHolder>{
    private Context mContext;
    public static ArrayList<Book> books;
    public BooksAdapterForCart(ArrayList<Book> books, Context mContext) {
        this.books = books;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BooksAdapterForCart.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksAdapterForCart.BookViewHolder holder, int position) {

        Book book = books.get(position);
        if(book == null){
            return;
        }

//        try{
//            Glide.with(holder.itemView.getContext())
//                    .load(book.getImg() != null ? book.getImg() : R.drawable.baseline_menu_book_24)
//                    .into(holder.ivItem);
//        }catch (Exception e){
//            Glide.with(holder.itemView.getContext())
//                    .load(R.drawable.baseline_menu_book_24)
//                    .into(holder.ivItem);
//        }
//
//
//
        Glide.with(holder.itemView.getContext())
                .load(R.drawable.baseline_menu_book_24)
                .into(holder.ivItem);
        holder.tvGia.setText(book.getPrice() + "VND");
        holder.tvNguoiBan.setText(book.getAuthor());
        holder.tvSoLuong.setText( "SL: " + book.getQuantity());
        holder.tvTieude.setText(book.getName());
//
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // chuyển màn hình và gửi dữ liệu đi cùng dưới dạng object
                Intent intent = new Intent(mContext, DetailsCartPayActivity.class);
//                intent.putExtra("book", book);
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
