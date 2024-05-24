package com.example.navigationbottom.response.notify;

import com.example.navigationbottom.model.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetNotifyResponse {
    private List<Notification> notifyResponseList;
    @SerializedName("EC")
    private String EC;

    public List<Notification> getNotifyResponseList() {
        return notifyResponseList;
    }

    public void setNotifyResponseList(List<Notification> notifyResponseList) {
        this.notifyResponseList = notifyResponseList;
    }

    public String getEC() {
        return EC;
    }

    public void setEC(String EC) {
        this.EC = EC;
    }
}
