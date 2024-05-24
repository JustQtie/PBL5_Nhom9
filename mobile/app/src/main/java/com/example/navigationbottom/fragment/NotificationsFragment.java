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

import com.example.navigationbottom.R;
import com.example.navigationbottom.adaper.BooksAdapterForHome;
import com.example.navigationbottom.adaper.NotificationAdapter;
import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Notification;
import com.example.navigationbottom.viewmodel.WebSocketManager;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {
    private RecyclerView rvNotification;
    private NotificationAdapter notificationsAdapter;
    private ArrayList<Notification> notifications;
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

        webSocketManager = WebSocketManager.getInstance();
        webSocketManager.subscribeToNotifications();

        notifications = new ArrayList<>();
//        Log.d("message received!", webSocketManager.getMessage());

        getNotificationAll();

        return mView;
    }

    private void getNotificationAll() {
        notificationsAdapter = new NotificationAdapter(notifications, getActivity());
        rvNotification.setAdapter(notificationsAdapter);
    }
}