package com.myapp.android.service.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.android.service.R;
import com.myapp.android.service.adapter.ChatAdapter;
import com.myapp.android.service.ui.activity.ContactActivity;

/**
 * Created by pro on 31.10.2017.
 */

public class ChatFragment extends Fragment {

    private TextView mTextEmptyChatList;

    private ChatAdapter mChatAdapter;
    private DatabaseReference mChatsRef;
    private ValueEventListener mChatsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatsRef = getChatsRefOfCurrentUser();
        mChatAdapter = new ChatAdapter(mChatsRef);
    }

    @Override
    public void onStart() {
        super.onStart();
        ValueEventListener chatsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mTextEmptyChatList.setVisibility(View.GONE);
                } else {
                    mTextEmptyChatList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mChatsRef.addValueEventListener(chatsListener);
        // Keep copy of chats listener to remove it when app stops
        mChatsListener = chatsListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView chatList = (RecyclerView) view.findViewById(R.id.recycler_chat_list);
        chatList.setHasFixedSize(true);
        //chatList.addItemDecoration(new DividerItemDecoration(getActivity(),
                //DividerItemDecoration.VERTICAL_LIST));
        chatList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatList.setAdapter(mChatAdapter);

        mTextEmptyChatList = (TextView) view.findViewById(R.id.text_empty_chat_list);

        FloatingActionButton fabCreateChat = (FloatingActionButton)
                view.findViewById(R.id.fab_create_chat);
        fabCreateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContact();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mChatsListener != null) {
            mChatsRef.removeEventListener(mChatsListener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatAdapter.cleanup();
    }

    private DatabaseReference getChatsRefOfCurrentUser() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(user.getUid());
    }

    private void showContact() {
        startActivity(new Intent(getActivity(), ContactActivity.class));
    }

}
