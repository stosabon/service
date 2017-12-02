package com.myapp.android.service.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.myapp.android.service.R;
import com.myapp.android.service.model.Message;

/**
 * Created by pro on 31.10.2017.
 */

public class MessageAdapter extends FirebaseRecyclerAdapter<Message, MessageAdapter.MessageViewHolder> {

    private static final int ITEM_TYPE_LEFT = 1;
    private static final int ITEM_TYPE_RIGHT = 0;

    public MessageAdapter(DatabaseReference ref) {
        super(Message.class, R.layout.item_message_right, MessageViewHolder.class, ref);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case ITEM_TYPE_LEFT:
                layout = R.layout.item_message_left;
                break;
            case ITEM_TYPE_RIGHT:
                layout = R.layout.item_message_right;
                break;
        }
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layout, null);
        return new MessageViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        // 2 is number of items
        if (!getItem(position).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            position = 0;
        } else {
            position = 1;
        }
        return position;
    }

    @Override
    protected void populateViewHolder(MessageAdapter.MessageViewHolder viewHolder, Message model,
                                      int position) {
        viewHolder.textMessage.setText(model.getMessage());
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView textMessage;

        public MessageViewHolder(View itemView) {
            super(itemView);
            textMessage = (TextView) itemView.findViewById(R.id.text_message);
        }
    }

}
