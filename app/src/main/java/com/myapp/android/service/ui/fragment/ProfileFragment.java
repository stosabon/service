package com.myapp.android.service.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.myapp.android.service.R;
import com.myapp.android.service.ui.activity.EnterActivity;


import static android.app.Activity.RESULT_OK;
import static com.myapp.android.service.ui.activity.EnterActivity.TAG;

public class ProfileFragment extends Fragment {
    /**
     * Экземпляр firebase auth.
     */
    public FirebaseAuth mAuth;
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
            Log.d(EnterActivity.TAG, data.getData().getPath());
            //Log.d(EnterActivity.TAG, data.getData().toString());
            uploadImageToFirebaseStorage(data.getData());
        }
    }

    /**
     * Метод для загрузки изображения в хранилище.
     * @param uriImage - выбранное изображение
     */
    private void uploadImageToFirebaseStorage(Uri uriImage) {
        mAuth = FirebaseAuth.getInstance();
        // Создаем ссылку в Хранилище Firebase
        StorageReference riversRef = mStorageRef.child(mAuth.getCurrentUser().getUid() + "/avatar");
        // создаем uploadTask посредством вызова метода putFile(), в качестве аргумента идет созданная нами ранее Uri
        UploadTask uploadTask = riversRef.putFile(uriImage);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "Изображение не загрузилось в хранилище");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Изображение загрузилось в хранилище");
                setAvatarFromFirebaseStorage();
            }
        });

    }

    /**
     * Метод для выгрузки имени пользователя из базы данных.
     */
    private void setUserNameFromDatabase() {
        mAuth = FirebaseAuth.getInstance();
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
     * Метод для установки аватара пользователя.
     */
    private void setAvatarFromFirebaseStorage() {

        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(mStorageRef.child(mAuth.getCurrentUser().getUid() + "/avatar"))
                .into(mAvatarImage);
        Log.d(TAG, "Изображение изменено");
    }
}
