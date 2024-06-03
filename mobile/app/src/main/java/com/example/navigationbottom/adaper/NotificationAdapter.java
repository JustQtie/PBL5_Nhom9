package com.example.navigationbottom.adaper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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
import com.example.navigationbottom.activity.EditBookActivity;
import com.example.navigationbottom.activity.NotificationConfirmActivity;
import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.utils.NotifyStatus;
import com.example.navigationbottom.viewmodel.NotifyApplication;
import com.example.navigationbottom.viewmodel.NotifyServiceApi;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

import java.util.ArrayList;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{


    private Context mContext;
    public static ArrayList<Notification> notifications;

    private NotifyServiceApi notifyServiceApi;

    private Notification notification;

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
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        notification = notifications.get(position);
        if(notification == null){
            return;
        }

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.baseline_person_taikhoan)
                .into(holder.ivItem);

        notifyServiceApi = new NotifyServiceApi(mContext);

        holder.tvNoidung.setText(notification.getContent());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotificationConfirmActivity.class);
                intent.putExtra("notifications", notifications.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    private void showCustomAlertDialog(int gravity, Notification notification) {
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


        if(notification.getStatus().equals(NotifyStatus.NOT_RESPONDED)){
            appCompatButtonDongY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notification.setStatus(NotifyStatus.AGREE);
                    NotifyApplication notifyApplication = NotifyApplication.instance();
                    notifyApplication.getStompClient().send("/app/res_notify", new Gson().toJson(notification)).subscribe();
                    dialog.dismiss();
                }
            });

            appCompatButtonTuChoi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notification.setStatus(NotifyStatus.CANCEL);
                    NotifyApplication notifyApplication = NotifyApplication.instance();
                    notifyApplication.getStompClient().send("/app/res_notify", new Gson().toJson(notification)).subscribe();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
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
