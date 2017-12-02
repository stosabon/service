package com.myapp.android.service.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.myapp.android.service.R;
import com.myapp.android.service.adapter.MessageAdapter;
import com.myapp.android.service.model.Chat;
import com.myapp.android.service.model.Message;
import com.myapp.android.service.model.User;
import com.myapp.android.service.ui.activity.EnterActivity;

/**
 * Created by pro on 31.10.2017.
 */

public class MessageFragment extends Fragment {

    /**
     * Recycler для сообщений
     */
    private RecyclerView mMessageList;
    /**
     * Адаптер.
     */
    private MessageAdapter mMessageAdapter;
    /**
     * Ссылка на бд сообщений отправителя.
     */
    private DatabaseReference mMessagesRefOfSender;
    /**
     * Ссылка на бд сообщений получателя.
     */
    private DatabaseReference mMessagesRefOfReceiver;
    /**
     * Слушатель от другого пользователя.
     */
    private ReceiverListener mListener;

    private String name;
    private String email;

    /**
     * Установка слушателя.
     * @param activity - activity
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ReceiverListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + ReceiverListener.class.getSimpleName());
        }
    }

    /**
     * Ставим адаптер.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessagesRefOfSender = getMessagesRefOfSender();
        mMessagesRefOfReceiver = getMessagesRefOfReceiver();
        mMessageAdapter = new MessageAdapter(mMessagesRefOfSender);
    }

    /**
     * Ставим view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message, container, false);
    }

    /**
     * Выводим сообщения.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);

        mMessageList = (RecyclerView) view.findViewById(R.id.recycler_list);
        mMessageList.setHasFixedSize(true);
        mMessageList.setLayoutManager(layoutManager);
        mMessageList.setAdapter(mMessageAdapter);

        final EditText editMessage = (EditText) view.findViewById(R.id.edit_message);

        ImageButton buttonSendMessage = (ImageButton) view.findViewById(R.id.button_send_message);
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editMessage.getText().toString().trim();
                if (message.equals("")) {
                    Toast.makeText(getActivity(),
                            R.string.error_empty_message,
                            Toast.LENGTH_SHORT)
                            .show();
                } else {
                    sendMessage(new Message(message, mListener.getReceiverUid()));
                    scrollToBottom();
                    editMessage.setText("");

                    Chat chatSender = new Chat();
                    chatSender.setUid(mListener.getReceiverUid());
                    chatSender.setName(mListener.getReceiverName());
                    chatSender.setPhotoUrl(mListener.getReceiverPhotoUrl());
                    chatSender.setLastMessage(message);

                    Chat chatReceiver = new Chat();
                    User user = getUserFromDatabase();
                    chatReceiver.setUid(user.getUid());
                    chatReceiver.setName(user.getName());
                    //TODO Сделать считывание фото.
                    chatReceiver.setPhotoUrl(mListener.getReceiverPhotoUrl());
                    chatReceiver.setLastMessage(message);
                    createChats(chatSender, chatReceiver);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMessageAdapter.cleanup();
        mMessagesRefOfSender = null;
    }

    /**
     * Получаем сообщения диалога для отправителя.
     * @return
     */
    private DatabaseReference getMessagesRefOfSender() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child(user.getUid())
                .child(mListener.getReceiverUid());
    }

    /**
     * Получаем сообщения диалога для получателя.
     * @return
     */
    private DatabaseReference getMessagesRefOfReceiver() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child(mListener.getReceiverUid())
                .child(user.getUid());
    }
    /**
     * Отправка сообщения.
     * @param message
     */
    private void sendMessage(Message message) {
        mMessagesRefOfSender.push().setValue(message);
        mMessagesRefOfReceiver.push().setValue(message);
    }

    /**
     * Скролл до конца.
     */
    private void scrollToBottom() {
        mMessageList.smoothScrollToPosition(mMessageAdapter.getItemCount());
    }

    /**
     * Создание чатов получателя и отправителя.
     * @param sender
     * @param receiver
     */
    private void createChats(Chat sender, Chat receiver) {
        DatabaseReference chatRefOfSender = getChatRefOfSender();
        DatabaseReference chatRefOfReceiver = getChatRefOfReceiver();
        chatRefOfSender.setValue(sender);
        chatRefOfReceiver.setValue(receiver);
    }

    /**
     * Получаем чат отправителя.
     * @return
     */
    private DatabaseReference getChatRefOfSender() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(user.getUid())
                .child(mListener.getReceiverUid());
    }
    /**
     * Получаем чат получателя.
     * @return
     */
    private DatabaseReference getChatRefOfReceiver() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        return FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(mListener.getReceiverUid())
                .child(user.getUid());
    }

    /**
     * Used to get receiver data.
     * A receiver gets messages from the current user
     */
    public interface ReceiverListener {
        String getReceiverUid();

        String getReceiverName();

        String getReceiverPhotoUrl();
    }

    /**
     * Метод для преобразования FirebaseUser в User.
     * @return
     */
    private User getUserFromDatabase() {
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue().toString();
                email = dataSnapshot.child("email").getValue().toString();
                Log.d(EnterActivity.TAG, name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return new User(name, FirebaseAuth.getInstance().getCurrentUser().getUid(), email);
    }
}
