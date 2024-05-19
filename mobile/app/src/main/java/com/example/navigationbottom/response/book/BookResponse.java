package com.example.navigationbottom.response.book;

import com.example.navigationbottom.model.Category;
import com.example.navigationbottom.model.User;
import com.google.gson.annotations.SerializedName;

public class BookResponse {

    @SerializedName("id")
    private Long id;
    @SerializedName("name")
    private String name;
    @SerializedName("author")
    private String author;
    @SerializedName("price")
    private Float price;
    @SerializedName("status")
    private String status;
    @SerializedName("point")
    private Float point;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("thumbnail")
    private String thumbnail;

    @SerializedName("description")
    private String description;

    @SerializedName("user")
    private User user;

    @SerializedName("category")
    private Category category;

    @SerializedName("EC")
    private String ec;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
