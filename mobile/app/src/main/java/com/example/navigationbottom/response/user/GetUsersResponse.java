package com.example.navigationbottom.response.user;

import com.example.navigationbottom.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUsersResponse {
    private List<User> userResponseList;
    @SerializedName("EC")
    private String ec;

    public List<User> getUserResponseList() {
        return userResponseList;
    }

    public void setUserResponseList(List<User> userResponseList) {
        this.userResponseList = userResponseList;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }
}
