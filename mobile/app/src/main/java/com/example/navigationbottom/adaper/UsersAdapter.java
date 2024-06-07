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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;

import com.example.navigationbottom.activity.ChatItemActivity;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    public static ArrayList<User> users;
    private Context mContext;

    public UsersAdapter(ArrayList<User> users, Context mContext) {
        this.users = users;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_item, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        if(user == null){
            return;
        }

        String imageUrl = ApiService.BASE_URL + "api/v1/users/images/" + user.getThumbnail();
        try{
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .error(R.drawable.ic_action_name) // Ảnh hiển thị khi có lỗi
                    .into(holder.ivItem);
        }catch (Exception e){
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.ic_action_name)
                    .into(holder.ivItem);
        }
//
        holder.tvName.setText(user.getFullname());
        holder.tvPhone.setText(user.getPhone_number());

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatItemActivity.class);
                intent.putExtra("hisUid", user.getId());
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        if(users != null){
            return users.size();
        }
        return 0;
    }




    public class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivItem;
        private TextView tvName, tvPhone;
        private RelativeLayout layoutItem;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutItem = itemView.findViewById(R.id.layout_item);
            ivItem = itemView.findViewById(R.id.iv_itemUser);
            tvPhone = itemView.findViewById(R.id.tv_phoneItemUser);
            tvName = itemView.findViewById(R.id.tv_nameItemUser);
        }
    }
}
