package com.myapp.android.service;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import static android.app.Activity.RESULT_OK;
import static com.myapp.android.service.EnterActivity.TAG;
import static com.myapp.android.service.EnterActivity.mAuth;

public class ProfileFragment extends Fragment {
    /**
     * RESULT CODE для выбора изображения.
     */
    private final int SELECT_PHOTO = 1234;
    /**
     * Аватар пользователя.
     */
    private ImageButton mAvatarImage;
    /**
     * Имя и фамилия.
     */
    private TextView mUserNameTextView;
    /**
     * Ссылка на базу данных.
     */
    private DatabaseReference mDatabase;
    /**
     * Объект хранилища.
     */
    private FirebaseStorage mStorage;
    /**
     * Ссылка на хранилище.
     */
    private StorageReference mStorageRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Получаем доступ к хранилищу
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        mAvatarImage = (ImageButton) v.findViewById(R.id.avatar_image);
        mUserNameTextView = (TextView) v.findViewById(R.id.name_surname_text);

        setUserNameFromDatabase();
        setAvatarFromFirebaseStorage();
        mAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK) {
            uploadImageToFirebaseStorage(data.getData());
            setAvatarFromFirebaseStorage();
        }
    }

    /**
     * Метод для загрузки изображения в хранилище.
     * @param uriImage - выбранное изображение
     */
    private void uploadImageToFirebaseStorage(Uri uriImage) {
        // Создаем ссылку в Хранилище Firebase
        StorageReference riversRef = mStorageRef.child(mAuth.getCurrentUser().getUid() + "/avatar");
        // создаем uploadTask посредством вызова метода putFile(), в качестве аргумента идет созданная нами ранее Uri
        UploadTask uploadTask = riversRef.putFile(uriImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Изображение не загрузилось в хранилище");
            }
        });
    }

    /**
     * Метод для выгрузки имени пользователя из базы данных.
     */
    private void setUserNameFromDatabase() {
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
    }

    /**
     * Метод для установки аватара пользоавтеля
     */
    private void setAvatarFromFirebaseStorage() {
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(mStorageRef.child(mAuth.getCurrentUser().getUid() + "/avatar"))
                .into(mAvatarImage);
    }
}
