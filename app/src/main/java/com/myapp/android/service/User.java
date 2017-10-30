package com.myapp.android.service;

import android.net.Uri;
import android.widget.ImageView;

/**
 * Created by pro on 29.08.2017.
 */

public class User {
    /**
     * Имя пользователя.
     */
    private String mName;
    /**
     * Uid пользователя.
     */
    private String mUid;
    /**
     * Email пользователя.
     */
    private String mEmail;
    /**
     * Фото пользователя.
     */
    private String mPhotoUrl;

    public User() {
    }

    public User(String name, String uid, String email) {
        mName = name;
        mUid = uid;
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }
}
