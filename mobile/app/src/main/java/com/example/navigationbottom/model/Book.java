package com.example.navigationbottom.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Book implements Serializable {
    private Long id;
    private String name;
    private String author;

    private Float price;
    private String thumbnail;

    private Float point;
    private String status;
    private String description;

    private int quantity;

    private long user_id;

    private long category_id;
    private List<Integer> created_at;  // Thay đổi từ Object sang List<Integer>
    private List<Integer> updated_at;

    @SerializedName("EC")
    private String ec;

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public List<Integer> getCreated_at() {
        return created_at;
    }

    public void setCreated_at(List<Integer> created_at) {
        this.created_at = created_at;
    }

    public List<Integer> getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(List<Integer> updated_at) {
        this.updated_at = updated_at;
    }

    public Book() {

    }

    public Book(Long id, String name, String author, Float price, String thumbnail, Float point, String status, String description, int quantity) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
        this.thumbnail = thumbnail;
        this.point = point;
        this.status = status;
        this.description = description;
        this.quantity = quantity;
    }
}
