package com.example.navigationbottom.fragment;

import android.annotation.SuppressLint;
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
import com.example.navigationbottom.activity.MainActivity;
import com.example.navigationbottom.adaper.NotificationAdapter;
import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.model.User;
import com.example.navigationbottom.response.notify.GetNotifyResponse;
import com.example.navigationbottom.viewmodel.NotifyApplication;
import com.example.navigationbottom.viewmodel.NotifyServiceApi;
import com.example.navigationbottom.viewmodel.UserPreferences;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {
    private RecyclerView rvNotification;
    private NotificationAdapter notificationsAdapter;
    private ArrayList<Notification> notifications;

    private NotifyServiceApi notifyServiceApi;
    private View mView;

    private NotifyApplication notifyApplication;

    public NotificationsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("CheckResult")
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


        notifyApplication = NotifyApplication.instance();
        notifyApplication.getStompClient().topic("/topic/notification/"+user.getId()).subscribe(stompMessage -> {
           String message = stompMessage.getPayload();
           Log.d("message received!", message);
            notifyServiceApi.getNotify(user.getId()).enqueue(new Callback<GetNotifyResponse>() {
                @Override
                public void onResponse(Call<GetNotifyResponse> call, Response<GetNotifyResponse> response) {
                    GetNotifyResponse notificationsList = response.body();
                    if(notificationsList.getEC().equals("0")){
                        Log.d("RequestData1", new Gson().toJson(notificationsList));
                        for(Notification notification : notificationsList.getNotifyResponseList()){
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
                public void onFailure(Call<GetNotifyResponse> call, Throwable t) {
                    String errorMessage = t.getMessage();
                    Toast.makeText(mView.getContext(), "Request failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("Hello", String.valueOf("Request failed: " + errorMessage));
                }
            });
        });
        return mView;
    }
}