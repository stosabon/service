package com.myapp.android.service.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.myapp.android.service.R;

public class EnterActivity extends AppCompatActivity {

    public static final String TAG = "EmailPassword";
    /**
     * Экземпляр firebase auth.
     */
    public FirebaseAuth mAuth;
    /**
     * Обработчик событий auth.
     */
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    /**
     * Кнопка для входа в аккаунт.
     */
    private Button mSignUpButton;
    /**
     * Кнопка для регистрации акааунта.
     */
    private Button mRegisterButton;
    /**
     * Кнопка сброса пароля.
     */
    private Button mResetButton;
    /**
     * Поле ввода логина.
     */
    private EditText mLoginText;
    /**
     * Поле неправильного пароля.
     */
    private TextView mWrongPasswordText;
    /**
     * Поле ввода пароля.
     */
    private EditText mPasswordText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        mAuth = FirebaseAuth.getInstance();
        //mAuth.signOut();
        checkUser(mAuth.getCurrentUser());

        mSignUpButton = (Button) findViewById(R.id.signup_button);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mResetButton = (Button) findViewById(R.id.reset_password_button);
        mLoginText = (EditText) findViewById(R.id.email_register_text);
        mPasswordText = (EditText) findViewById(R.id.password_register_text);
        mWrongPasswordText = (TextView) findViewById(R.id.password_warning_text);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginText.getText().toString();
                String password = mPasswordText.getText().toString();
                signIn(email,password);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnterActivity.this, RegisterActivity.class));
            }
        });

        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EnterActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    /**
     * Метод для входа в аккаунт.
     * @param email -email
     * @param password - пароль
     */
    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(EnterActivity.this, ProfileActivity.class));
                            finish();
                        } else {
                            mWrongPasswordText.setText(R.string.wrong_password);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                });
    }

    /**
     * Метод для проверки залогинен ли пользователь
     * @param user - пользователь
     */
    private void checkUser(FirebaseUser user) {
        if (user == null) {
            Log.d(TAG, "user not founded");
        } else {
            Log.d(TAG, "user founded");
            Intent intent = new Intent(EnterActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
