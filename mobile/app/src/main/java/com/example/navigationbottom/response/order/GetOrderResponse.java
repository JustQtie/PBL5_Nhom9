package com.example.navigationbottom.response.order;

import com.example.navigationbottom.model.Book;
import com.example.navigationbottom.model.Order;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrderResponse {
    private List<Order> orderResponseList;
    @SerializedName("EC")
    private String ec;

    public List<Order> getOrderResponseList() {
        return orderResponseList;
    }

    public void setOrderResponseList(List<Order> orderResponseList) {
        this.orderResponseList = orderResponseList;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }
}
