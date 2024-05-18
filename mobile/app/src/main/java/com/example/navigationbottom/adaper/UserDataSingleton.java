package com.example.navigationbottom.adaper;

import com.example.navigationbottom.model.User;

public class UserDataSingleton {
    private static UserDataSingleton instance;
    private User user;

    private UserDataSingleton() {

    }

    public static UserDataSingleton getInstance() {
        if (instance == null) {
            instance = new UserDataSingleton();
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
