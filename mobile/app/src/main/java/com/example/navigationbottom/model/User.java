package com.example.navigationbottom.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private Long id;
    private String password;
    private String retype_password;
    private String fullname;
    private String phone_number;
    private String address;
    private String active;
    private Date date_of_birth;
    private String gender;
    private String thumbnail;

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

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Date getDateOfBirth() {
        return date_of_birth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.date_of_birth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
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

    public User(Long id, String password, String fullname, String phoneNumber, String address, String active, Date dateOfBirth, String gender, String thumbnail) {
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
}
