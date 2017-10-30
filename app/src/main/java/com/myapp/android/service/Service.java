package com.myapp.android.service;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.myapp.android.service.EnterActivity.TAG;
import static com.myapp.android.service.EnterActivity.mAuth;

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
