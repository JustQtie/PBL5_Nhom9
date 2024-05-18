package com.example.navigationbottom.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.navigationbottom.model.User;

public class UserPreferences {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";

    // Thêm các khóa khác nếu cần

    public static void saveUser(Context context, User user) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(KEY_USER_ID, user.getId());
        editor.apply();
    }

    public static User getUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        long userId = sharedPreferences.getLong(KEY_USER_ID, -1);

        if (userId == -1) {
            return null; // User chưa được lưu
        }

        User user = new User();
        user.setId(userId);
        return user;
    }

    public static void clearUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
