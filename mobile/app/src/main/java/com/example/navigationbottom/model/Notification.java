package com.example.navigationbottom.model;

public class Notification {
    private String id;
    private String noiDung;
    private String img;

    public Notification(String noiDung) {
        this.noiDung = noiDung;
    }

    public Notification() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
