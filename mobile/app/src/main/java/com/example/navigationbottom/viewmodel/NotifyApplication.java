package com.example.navigationbottom.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;

import com.example.navigationbottom.model.User;
import com.example.navigationbottom.utils.Subscriptions;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NotifyApplication extends Application {
    private static NotifyApplication notifyApplication;
    private StompClient stompClient;
    private Subscriptions subscriptions;
    private boolean isWebsocketInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();
        notifyApplication = this;
        initWebsocket();
    }

    public static NotifyApplication instance() {
        if(notifyApplication == null){
            return new NotifyApplication();
        }
        return notifyApplication;
    }

    @SuppressLint("CheckResult")
    private void initWebsocket() {
        if (!isWebsocketInitialized) {
            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://2065-171-225-184-143.ngrok-free.app/notify");
            stompClient.lifecycle().subscribe(lifecycleEvent -> {
                switch (lifecycleEvent.getType()) {
                    case OPENED:
                        System.out.println("Stomp connection opened");
                        break;
                    case ERROR:
                        System.err.println("Stomp connection error");
                        lifecycleEvent.getException().printStackTrace();
                        break;
                    case CLOSED:
                        System.out.println("Stomp connection closed");
                        break;
                }
            });
            stompClient.connect();
            subscriptions = new Subscriptions();
            isWebsocketInitialized = true;
        }
    }

    public StompClient getStompClient() {
        return stompClient;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }
}
