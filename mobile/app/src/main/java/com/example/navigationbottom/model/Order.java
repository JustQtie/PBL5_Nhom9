package com.example.navigationbottom.model;

import java.io.Serializable;

public class Order implements Serializable {
    private Long id;
    private Long user_id;
    private String status;

    private long product_id;

    private int number_of_product;
    private float total_money;
    private String shipping_address;
    private String payment_method;

    private String EC;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotal_money() {
        return total_money;
    }

    public void setTotal_money(float total_money) {
        this.total_money = total_money;
    }

    public String getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(String shipping_address) {
        this.shipping_address = shipping_address;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(long product_id) {
        this.product_id = product_id;
    }

    public int getNumber_of_product() {
        return number_of_product;
    }

    public void setNumber_of_product(int number_of_product) {
        this.number_of_product = number_of_product;
    }

    public String getEC() {
        return EC;
    }

    public void setEC(String EC) {
        this.EC = EC;
    }
}
