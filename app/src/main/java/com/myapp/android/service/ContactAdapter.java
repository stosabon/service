package com.myapp.android.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.ui.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by pro on 18.09.2017.
 */

public class ContactAdapter extends FirebaseRecyclerAdapter<User, ContactAdapter.ContactViewHolder> {

    public ContactAdapter(DatabaseReference ref) {
        super(User.class, R.layout.item_contact, ContactViewHolder.class, ref);
    }

    @Override
    protected void populateViewHolder(ContactViewHolder viewHolder, User model, int position) {
        Glide.with(viewHolder.itemView.getContext())
                .load(model.getPhotoUrl())
                .into(viewHolder.imageContactAvatar);
        viewHolder.textContactName.setText(model.getName());
        // Not visible
        viewHolder.contactUid = model.getUid();
        viewHolder.contactPhotoUrl = model.getPhotoUrl();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageContactAvatar;
        public TextView textContactName;

        // Required only to download a specific contact's messages on another screen
        public String contactUid;
        public String contactPhotoUrl;

        public ContactViewHolder(View itemView) {
            super(itemView);
            imageContactAvatar = (ImageView) itemView.findViewById(R.id.image_user_avatar);
            textContactName = (TextView) itemView.findViewById(R.id.text_user_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Context context = itemView.getContext();
                    Intent intent = new Intent(context, EnterActivity.class);
                    intent.putExtra(UserExtraConstants.EXTRA_USER_UID, contactUid);
                    intent.putExtra(UserExtraConstants.EXTRA_USER_NAME,
                            textContactName.getText().toString());
                    intent.putExtra(UserExtraConstants.EXTRA_USER_PHOTO_URL, contactPhotoUrl);
                    context.startActivity(intent);
                    ((Activity) context).finish();*/
                }
            });
        }
    }
}
