package com.example.navigationbottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.adaper.UsersAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.user.GetUsersResponse;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListActivity extends AppCompatActivity {
    private RecyclerView rvUsers;
    private UsersAdapter usersAdapter;
    private ArrayList<User> users;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);


        rvUsers = findViewById(R.id.rv_chatList);
        rvUsers.setHasFixedSize(true);

        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        rvUsers.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        users = new ArrayList<>();

        getAllUserTruIdNay();
    }

    private void getAllUserTruIdNay() {
        User user = UserPreferences.getUser(this);
        userApiService = new UserApiService(this);

        if (user != null) {
            userApiService.getAllUserTruIdNay(user.getId()).enqueue(new Callback<GetUsersResponse>() {
                @Override
                public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {
                    GetUsersResponse getUsersResponse = response.body();
                    if(getUsersResponse!=null){
                        Log.d("RequestDataChatList", new Gson().toJson(getUsersResponse));
                        if(getUsersResponse.getEc().equals("0")){
                            List<User> userResponseList = getUsersResponse.getUserResponseList();
                            for (User u : userResponseList) {
                                User getUser = new User();
                                getUser.setId(u.getId());
                                getUser.setFullname(u.getFullname());
                                getUser.setThumbnail(u.getThumbnail());
                                getUser.setPhone_number(u.getPhone_number());
                                getUser.setGender(u.getGender());
                                getUser.setAddress(u.getAddress());
                                getUser.setActive(u.getActive());
                                users.add(getUser);
                            }
                            usersAdapter = new UsersAdapter(users, ChatListActivity.this);
                            rvUsers.setAdapter(usersAdapter);

                        }else{
                            Log.e("UploadError", "Upload failed with status: " + response.code());
                            try {
                                Log.e("UploadError", "Response error body: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }else{
                        Toasty.error(ChatListActivity.this, "User invalid", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetUsersResponse> call, Throwable t) {
                    // Xử lý khi gặp lỗi
                    Toasty.error(ChatListActivity.this, "Error: " + t.getMessage(), Toasty.LENGTH_SHORT).show();
                }
            });
        } else {
            // Xử lý khi không tìm thấy người dùng đăng nhập
            Toasty.error(ChatListActivity.this, "User not logged in", Toasty.LENGTH_SHORT).show();
        }


    }
}