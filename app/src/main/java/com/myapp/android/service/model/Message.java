package com.myapp.android.service.model;

/**
 * Created by pro on 31.10.2017.
 */

public class Message {
    /**
     * Текст сообщения.
     */
    private String mMessage;

    /**
     * Uid получателя.
     */
    private String mUid;

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public Message() {
    }

    public Message(String message, String uid) {
        mMessage = message;
        mUid = uid;
    }


    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

}
