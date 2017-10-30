package com.myapp.android.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by pro on 18.09.2017.
 */

public class ContactFragment extends Fragment {

    private ContactAdapter mContactAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contact_fragment, container, false);


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        RecyclerView contactList = (RecyclerView) v.findViewById(R.id.recycler_list);
        //contactList.setHasFixedSize(true);
        //contactList.setLayoutManager(new LinearLayoutManager();

        mContactAdapter = new ContactAdapter(usersRef);
        contactList.setHasFixedSize(true);
        contactList.setLayoutManager(new LinearLayoutManager(getContext()));
        contactList.setAdapter(mContactAdapter);

        Log.d(EnterActivity.TAG, "Адаптер поставлен");
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mContactAdapter.cleanup();
    }
}
