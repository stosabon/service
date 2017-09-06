package com.myapp.android.service;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by pro on 28.08.2017.
 */

public class ResetPasswordActivity extends AppCompatActivity {
    /**
     * Поле ввода логина.
     */
    private EditText mLoginText;
    /**
     * Кнопка отправления уведомления.
     */
    private Button mSendButton;
    /**
     * Экземляр auth.
     */
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        mAuth = FirebaseAuth.getInstance();
        mLoginText = (EditText) findViewById(R.id.email_send_text);
        mSendButton = (Button) findViewById(R.id.send_email_button);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginText.getText().toString().trim();
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(ResetPasswordActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.send_new_password), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, getString(R.string.not_send_new_password), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
