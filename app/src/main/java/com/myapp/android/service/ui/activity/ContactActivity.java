package com.myapp.android.service.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.myapp.android.service.R;
import com.myapp.android.service.adapter.ContactAdapter;

/**
 * Created by pro on 31.10.2017.
 */

public class ContactActivity extends AppCompatActivity{

    private ContactAdapter mContactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        //setSupportActionBar(toolbar);

        //if (getSupportActionBar() != null) {
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //}

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        RecyclerView contactList = (RecyclerView) findViewById(R.id.recycler_list);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(new LinearLayoutManager(this));

        mContactAdapter = new ContactAdapter(usersRef);
        contactList.setAdapter(mContactAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContactAdapter.cleanup();
    }

}
