package com.example.navigationbottom.adaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.model.ModelChat;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.MyHolder>{

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<ModelChat> chatList;
    private String imageUrl;


    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new MyHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        String message = chatList.get(position).getMessage();
        String timeStamp = chatList.get(position).getTimestamp();

        String dateTime = formatDateTime(timeStamp);
        holder.timeTv.setText(dateTime);

        holder.messageTv.setText(message);


        try {
            Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_action_name) // Ảnh tạm thời khi đang tải
                        .error(R.drawable.ic_action_name) // Ảnh hiển thị khi có lỗi
                        .into(holder.profileIv);

        } catch (Exception e) {
            Glide.with(context)
                    .load(R.drawable.ic_action_name)
                    .into(holder.profileIv);
        }



    }

    private String formatDateTime(String timestamp) {
        if (timestamp == null || timestamp.isEmpty()) {
            return "time";
        }

        try {
            long timeMillis = Long.parseLong(timestamp);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm aa", Locale.getDefault());

            return sdf.format(new Date(timeMillis));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return "time 2";
        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        User user = UserPreferences.getUser(context);
        if(chatList.get(position).getSender().equals(user.getId())){
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ShapeableImageView profileIv;
        TextView messageTv, timeTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv = itemView.findViewById(R.id.sv_user_row_chat_left);
            messageTv = itemView.findViewById(R.id.tv_mess_row_chat_left);
            timeTv = itemView.findViewById(R.id.tv_time_row_chat_left);
            messageLayout = itemView.findViewById(R.id.message_layout);
        }
    }

}
