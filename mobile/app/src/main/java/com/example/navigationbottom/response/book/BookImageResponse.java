package com.example.navigationbottom.response.book;

import com.google.gson.annotations.SerializedName;

public class BookImageResponse {
    @SerializedName("EC")
    private String ec;

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }
}
