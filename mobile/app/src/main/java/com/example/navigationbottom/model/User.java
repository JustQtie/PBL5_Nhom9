package com.example.navigationbottom.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Long id;
    private String password;
    private String retype_password;
    private String fullname;
    private String phone_number;
    private String address;
    private Boolean active;
    private Date date_of_birth;
    private Boolean gender;
    private String thumbnail;

    @SerializedName("EC")
    private String ec;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.date_of_birth = dateOfBirth;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public User() {
    }

    public String getRetype_password() {
        return retype_password;
    }

    public void setRetype_password(String retype_password) {
        this.retype_password = retype_password;
    }

    public User(Long id, String password, String fullname, String phoneNumber, String address, Boolean active, Date dateOfBirth, Boolean gender, String thumbnail) {
        this.id = id;
        this.password = password;
        this.fullname = fullname;
        this.phone_number = phoneNumber;
        this.address = address;
        this.active = active;
        this.date_of_birth = dateOfBirth;
        this.gender = gender;
        this.thumbnail = thumbnail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", retype_password='" + retype_password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", address='" + address + '\'' +
                ", active=" + active +
                ", date_of_birth=" + date_of_birth +
                ", gender=" + gender +
                ", thumbnail='" + thumbnail + '\'' +
                ", ec='" + ec + '\'' +
                '}';
    }
}
