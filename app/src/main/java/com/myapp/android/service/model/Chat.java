package com.myapp.android.service.model;

/**
 * Created by pro on 31.10.2017.
 */

public class Chat {
    /**
     * Uid пользователя.
     */
    private String mUid;
    /**
     * Имя пользователя
     */
    private String mName;
    /**
     * Фото пользователя.
     */
    private String mPhotoUrl;
    /**
     * Последнее сообщение.
     */
    private String mLastMessage;

    public Chat() {
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        mLastMessage = lastMessage;
    }

}
