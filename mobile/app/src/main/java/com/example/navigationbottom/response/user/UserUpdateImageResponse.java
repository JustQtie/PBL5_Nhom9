package com.example.navigationbottom.response.user;

import com.google.gson.annotations.SerializedName;

public class UserUpdateImageResponse {
    @SerializedName("EC")
    private String ec;

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    @Override
    public String toString() {
        return "UserUpdateImageResponse{" +
                "ec='" + ec + '\'' +
                '}';
    }
}
