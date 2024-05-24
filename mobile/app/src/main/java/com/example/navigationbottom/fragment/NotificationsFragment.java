package com.example.navigationbottom.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.navigationbottom.R;
import com.example.navigationbottom.activity.AddBookActivity;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.adaper.NotificationAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.viewmodel.NotifyServiceApi;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.example.navigationbottom.viewmodel.WebSocketManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private RecyclerView rvNotification;
    private NotificationAdapter notificationsAdapter;
    private ArrayList<Notification> notifications;

    private NotifyServiceApi notifyServiceApi;
    private View mView;

    private WebSocketManager webSocketManager;
    public NotificationsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_notifications, container, false);

        rvNotification = mView.findViewById(R.id.rv_notification);
        rvNotification.setHasFixedSize(true);

        rvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvNotification.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        notifyServiceApi = new NotifyServiceApi(getContext());

        User user = UserPreferences.getUser(getContext());

        notifications = new ArrayList<>();


        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.setMessageListener(new WebSocketManager.MessageListener() {
            @Override
            public void onMessageReceived(String message) {
                Log.d("message received!", message);
                Notification notification = new Notification();
                notification.setContent(message);
                notification.setUser_id(user.getId());
                notifyServiceApi.createNotify(notification).enqueue(new Callback<Notification>() {
                    @Override
                    public void onResponse(Call<Notification> call, Response<Notification> response) {
                        Notification notification1 = response.body();
                        if(notification1.getEc().equals("0")){
                            Log.d("RequestData1", new Gson().toJson(notification1));
                        }
                    }

                    @Override
                    public void onFailure(Call<Notification> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        Toast.makeText(mView.getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                    }
                });
            }
        });
        webSocketManager.subscribeToNotifications();

        notifyServiceApi.getNotify(user.getId()).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                List<Notification> notificationsList = response.body();
                if(notificationsList != null){
                    for(Notification notification : notificationsList){
                        Notification notification1 = new Notification();
                        notification1.setId(notification.getId());
                        notification1.setUser_id(notification.getUser_id());
                        notification1.setContent(notification.getContent());
                        notifications.add(notification1);
                    }
                    notificationsAdapter = new NotificationAdapter(notifications, getActivity());
                    rvNotification.setAdapter(notificationsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                String errorMessage = t.getMessage();
                Toast.makeText(mView.getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
            }
        });
        return mView;
    }
}