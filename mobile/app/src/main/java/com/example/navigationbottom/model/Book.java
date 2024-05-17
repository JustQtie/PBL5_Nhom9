package com.example.navigationbottom.model;

import java.io.Serializable;

public class Book implements Serializable {
    private String id;
    private String tieuDe;
    private String tacGia;
    private String loai;
    private String soLuong;
    private String moTa;
    private String gia;
    private String img;

    public Book() {

    }

    public Book(String id, String tieuDe, String tacGia, String loai, String soLuong, String moTa, String gia, String img) {
        this.id = id;
        this.tieuDe = tieuDe;
        this.tacGia = tacGia;
        this.loai = loai;
        this.soLuong = soLuong;
        this.moTa = moTa;
        this.gia = gia;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
