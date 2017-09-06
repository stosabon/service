package com.myapp.android.service;

import android.widget.ImageView;

/**
 * Created by pro on 29.08.2017.
 */

public class User {
    /**
     * Имя пользователя.
     */
    private String mName;

    public User() {
    }

    public User(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

}
