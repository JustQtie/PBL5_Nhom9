package com.example.navigationbottom.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.navigationbottom.model.Order;
import com.google.gson.Gson;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
public class WebSocketManager {

    private static WebSocketManager instance;
    private static final String WEBSOCKET_URL = "wss://5bd3-27-69-244-129.ngrok-free.app/chat";
    private StompClient stompClient;

    public String message;

    public WebSocketManager() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_URL);
    }

    public static synchronized WebSocketManager getInstance() {
        if (instance == null) {
            instance = new WebSocketManager();
        }
        return instance;
    }
    public void connect() {
        stompClient.connect();
    }

    public void disconnect() {
        stompClient.disconnect();
    }

    @SuppressLint("CheckResult")
    public void subscribeToNotifications() {
        stompClient.topic("/topic/notifications").subscribe(topicMessage -> {
            message = topicMessage.getPayload();
            // Xử lý thông báo nhận được
            Log.d("WebSocket", "Received: " + message);
        });
    }

    public void sendOrder(Order order) {
        stompClient.send("/app/order", new Gson().toJson(order)).subscribe();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
