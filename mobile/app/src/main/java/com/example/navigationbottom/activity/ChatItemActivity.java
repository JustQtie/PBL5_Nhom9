package com.example.navigationbottom.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.AdapterChat;
import com.example.navigationbottom.model.ModelChat;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.ApiService;
import com.example.navigationbottom.viewmodel.UserApiService;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatItemActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvChatlist;
    private ImageButton btnSend;
    private TextView tvName, tvStatus;
    private EditText edtSendMess;
    private ShapeableImageView svImageUser;
    private Long hisUid, myId;
    private String hisImg;
    private List<ModelChat> chatList;
    private AdapterChat adapterChat;
    private UserApiService userApiService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_item);

        toolbar = findViewById(R.id.toolbarChatItemActivity);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        init();

    }

    private void init(){
        rvChatlist = findViewById(R.id.rv_chatlist_chatActivity);
        svImageUser = findViewById(R.id.iv_avata_user_chatActivity);
        btnSend = findViewById(R.id.btn_sendmess_chatActivity);
        tvName = findViewById(R.id.tv_hisname_chatActivity);
        tvStatus = findViewById(R.id.tv_trangthai_chatActivity);
        edtSendMess = findViewById(R.id.edt_sendmess_chatActivity);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);


        rvChatlist.setHasFixedSize(true);
        rvChatlist.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        hisUid = intent.getLongExtra("hisUid", -1); // Giá trị mặc định là -1 nếu không có dữ liệu

        if (hisUid == -1) {
            Toasty.error(this, "ID người nhận không tồn tại", Toasty.LENGTH_SHORT).show();
            finish(); // Kết thúc hoạt động nếu không có ID
            return;
        }


        hienThiThongTinNguoiNhan();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtSendMess.getText().toString().trim();
                if(TextUtils.isEmpty(message)){
                    Toasty.warning(ChatItemActivity.this, "Cannot send the empty message...", Toasty.LENGTH_SHORT).show();
                }else{
                    sendMessage(message);
                }
            }
        });



        readMessages();

    }

    private void hienThiThongTinNguoiNhan() {
        // Khởi tạo UserApiService
        userApiService = new UserApiService(this);

//        User user = UserPreferences.getUser(this);
//        myId = user.getId();

        if (hisUid != null) {
            userApiService.getUser(hisUid).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User currentUser = response.body();
                        if (currentUser != null) {
                            // Hiển thị thông tin người dùng trên giao diện

                            displayUserProfile(currentUser);
                        } else {
                            // Xử lý khi không có dữ liệu người dùng trả về
                            Toasty.error(ChatItemActivity.this, "No user data found", Toasty.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý khi response không thành công
                        Toasty.error(ChatItemActivity.this, "Failed to get user profile", Toasty.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Xử lý khi gặp lỗi
                    Toasty.error(ChatItemActivity.this, "Error: " + t.getMessage(), Toasty.LENGTH_SHORT).show();
                }
            });
        } else {
            // Xử lý khi không tìm thấy người dùng đăng nhập
            Toasty.error(ChatItemActivity.this, "User not logged in", Toasty.LENGTH_SHORT).show();
        }
    }

    private void displayUserProfile(User currentUser) {
        tvName.setText(currentUser.getFullname());
        tvStatus.setText("online");
        try {
            String imageUrl = ApiService.BASE_URL + "api/v1/users/images/" + currentUser.getThumbnail();
            hisImg = imageUrl;
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_action_name) // Ảnh tạm thời khi đang tải
                        .error(R.drawable.ic_action_name) // Ảnh hiển thị khi có lỗi
                        .into(svImageUser);
            } else {
                Glide.with(this)
                        .load(R.drawable.ic_action_name)
                        .into(svImageUser);
            }
        } catch (Exception e) {
            Glide.with(this)
                    .load(R.drawable.ic_action_name)
                    .into(svImageUser);
        }
    }


    private void readMessages() {

        chatList = new ArrayList<>();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ModelChat chat = ds.getValue(ModelChat.class);
                    if(chat.getReceiver().equals(myId) && chat.getSender().equals(hisUid) ||
                            chat.getReceiver().equals(hisUid) && chat.getSender().equals(myId)
                    ){
                        chatList.add(chat);
                    }

                    adapterChat = new AdapterChat(ChatItemActivity.this, chatList, hisImg);
                    adapterChat.notifyDataSetChanged();
                    rvChatlist.setAdapter(adapterChat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        String  timestamp = String.valueOf(System.currentTimeMillis());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", myId);
        hashMap.put("receiver", hisUid);
        hashMap.put("message", message);
        hashMap.put("timestamp", timestamp);

        databaseReference.child("Chats").push().setValue(hashMap);
        edtSendMess.setText("");

    }

}