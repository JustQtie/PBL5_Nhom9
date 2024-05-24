package com.example.navigationbottom.adaper;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.DetailsHomeActivity;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Notification;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{


    private Context mContext;
    public static ArrayList<Notification> notifications;

    public NotificationAdapter(ArrayList<Notification> notifications, Context mContext) {
        this.notifications = notifications;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        if(notification == null){
            return;
        }

//        try{
//            Glide.with(holder.itemView.getContext())
//                    .load(notification.getImg() != null ? notification.getImg() : R.drawable.baseline_person_taikhoan)
//                    .into(holder.ivItem);
//        }catch (Exception e){
//            Glide.with(holder.itemView.getContext())
//                    .load(R.drawable.baseline_person_taikhoan)
//                    .into(holder.ivItem);
//        }

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.baseline_person_taikhoan)
                .into(holder.ivItem);



        holder.tvNoidung.setText(notification.getContent());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomAlertDialog(Gravity.CENTER, notification.getId());
            }
        });
    }

    private void showCustomAlertDialog(int gravity, Long i) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notification);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);


        AppCompatButton appCompatButtonDongY = dialog.findViewById(R.id.btn_digDongY_notification);
        AppCompatButton appCompatButtonTuChoi = dialog.findViewById(R.id.btn_digTuchoi_notification);

        appCompatButtonDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click đồng ý", Toast.LENGTH_SHORT).show();
            }
        });

        appCompatButtonTuChoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click từ chối", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    @Override
    public int getItemCount() {
        if(notifications != null){
            return notifications.size();
        }
        return 0;
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private ShapeableImageView ivItem;
        private TextView tvNoidung;
        private RelativeLayout layoutItem;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.notification_layout);
            ivItem = itemView.findViewById(R.id.img_user_notification_item);
            tvNoidung = itemView.findViewById(R.id.tv_notification_item);
        }
    }
}
