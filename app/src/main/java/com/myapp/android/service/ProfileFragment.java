package com.myapp.android.service;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.myapp.android.service.EnterActivity.mAuth;

public class ProfileFragment extends Fragment {
    /**
     * Аватар пользователя.
     */
    private ImageButton mAvatarImage;
    /**
     * Имя и фамилия.
     */
    private TextView mUserNameTextView;

    private DatabaseReference mDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        mAvatarImage = (ImageButton) v.findViewById(R.id.avatar_image);
        mUserNameTextView = (TextView) v.findViewById(R.id.name_surname_text);

        String currentUid = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(currentUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUserNameTextView.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                final int SELECT_PHOTO = 1234;
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        return v;
    }

}
