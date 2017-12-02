package com.myapp.android.service.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.myapp.android.service.ui.activity.MessageActivity;
import com.myapp.android.service.R;
import com.myapp.android.service.util.UserExtraConstants;
import com.myapp.android.service.model.Chat;

/**
 * Created by pro on 31.10.2017.
 */

public class ChatAdapter extends FirebaseRecyclerAdapter<Chat, ChatAdapter.ChatViewHolder> {

    public ChatAdapter(DatabaseReference ref) {
        super(Chat.class, R.layout.item_chat, ChatViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ChatViewHolder viewHolder, Chat model, int position) {
        Glide.with(viewHolder.itemView.getContext())
                .load(model.getPhotoUrl())
                .into(viewHolder.imageChatAvatar);
        viewHolder.textChatName.setText(model.getName());
        viewHolder.textChatLastMessage.setText(model.getLastMessage());

        // Not visible
        viewHolder.chatUid = model.getUid();
        viewHolder.chatPhotoUrl = model.getPhotoUrl();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageChatAvatar;
        public TextView textChatName;
        public TextView textChatLastMessage;

        // Required only to download a specific chat's messages on another screen
        public String chatUid;
        public String chatPhotoUrl;

        public ChatViewHolder(final View itemView) {
            super(itemView);
            imageChatAvatar = (ImageView) itemView.findViewById(R.id.image_chat_avatar);
            textChatName = (TextView) itemView.findViewById(R.id.text_user_name);
            textChatLastMessage = (TextView) itemView.findViewById(R.id.text_chat_last_message);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = itemView.getContext();
                    Intent intent = new Intent(context, MessageActivity.class);
                    intent.putExtra(UserExtraConstants.EXTRA_USER_UID, chatUid);
                    intent.putExtra(UserExtraConstants.EXTRA_USER_NAME,
                            textChatName.getText().toString());
                    intent.putExtra(UserExtraConstants.EXTRA_USER_PHOTO_URL, chatPhotoUrl);
                    context.startActivity(intent);
                }
            });
        }
    }
}
