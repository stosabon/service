package com.myapp.android.service;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pro on 18.09.2017.
 */

public class Service extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
