package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
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
import com.example.navigationbottom.model.ModelChat;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.book.GetBookResponse;
import com.example.navigationbottom.response.user.GetUsersResponse;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
    private Long myId;

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
        myId = user.getId();
        userApiService = new UserApiService(this);

        if (user != null) {
            userApiService.getAllUserTruIdNay(user.getId()).enqueue(new Callback<GetUsersResponse>() {
                @Override
                public void onResponse(Call<GetUsersResponse> call, Response<GetUsersResponse> response) {
                    GetUsersResponse getUsersResponse = response.body();
                    if (getUsersResponse != null) {
                        Log.d("RequestDataChatList", new Gson().toJson(getUsersResponse));
                        if (getUsersResponse.getEc().equals("0")) {
                            List<User> userResponseList = getUsersResponse.getUserResponseList();
                            filterChatUsers(userResponseList, user.getId());
                        } else {
                            Log.e("UploadError", "Upload failed with status: " + response.code());
                            try {
                                Log.e("UploadError", "Response error body: " + response.errorBody().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    } else {
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


    private void filterChatUsers(List<User> userResponseList, Long myId) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashSet<Long> chatUserIds = new HashSet<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if (chat != null && (chat.getSender().equals(myId) || chat.getReceiver().equals(myId))) {
                        chatUserIds.add(chat.getSender());
                        chatUserIds.add(chat.getReceiver());
                    }
                }

                // Loại bỏ chính mình khỏi danh sách
                chatUserIds.remove(myId);

                // Lọc danh sách người dùng dựa trên danh sách ID đã chat
                List<User> filteredUsers = new ArrayList<>();
                for (User user : userResponseList) {
                    if (chatUserIds.contains(user.getId())) {
                        filteredUsers.add(user);
                    }
                }

                usersAdapter = new UsersAdapter(new ArrayList<>(filteredUsers), ChatListActivity.this);
                rvUsers.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(ChatListActivity.this, "Error: " + error.getMessage(), Toasty.LENGTH_SHORT).show();
            }
        });
    }

}



