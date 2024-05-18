package com.example.navigationbottom.response.book;

import com.example.navigationbottom.model.Book;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBookResponse {
    private List<Book> productResponseList;
    @SerializedName("EC")
    private String ec;

    public List<Book> getProductResponseList() {
        return productResponseList;
    }

    public void setProductResponseList(List<Book> productResponseList) {
        this.productResponseList = productResponseList;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }
}
