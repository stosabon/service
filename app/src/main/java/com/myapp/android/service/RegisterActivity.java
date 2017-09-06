package com.myapp.android.service;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.myapp.android.service.EnterActivity.mAuth;

public class RegisterActivity extends AppCompatActivity {

    /**
     * Имя.
     */
    private EditText mName;
    /**
     * Фамилия.
     */
    private EditText mSurname;
    /**
     * Маил.
     */
    private EditText mEmail;
    /**
     * Пароль.
     */
    private EditText mPassword;
    /**
     * Повтороение пароля.
     */
    private EditText mRepeatPassword;
    /**
     * Кнопка регистрации.
     */
    private Button mRegisterButton;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mName = (EditText) findViewById(R.id.name_text);
        mSurname = (EditText) findViewById(R.id.surname_text);
        mEmail = (EditText) findViewById(R.id.email_register_text);
        mPassword = (EditText) findViewById(R.id.password_register_text);
        mRepeatPassword = (EditText) findViewById(R.id.repeat_password_text);
        mRegisterButton = (Button) findViewById(R.id.register_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                String repeatPassword = mRepeatPassword.getText().toString().trim();
                final String name = mName.getText().toString().trim();
                final String surname = mSurname.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.empty_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.empty_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.short_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), R.string.empty_name, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(surname)) {
                    Toast.makeText(getApplicationContext(), R.string.empty_surname, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(repeatPassword)) {
                    Toast.makeText(getApplicationContext(), R.string.not_equals_passwords, Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, R.string.auth_failed, Toast.LENGTH_SHORT)
                                            .show();
                                } else {
                                    Toast.makeText(RegisterActivity.this, R.string.user_created, Toast.LENGTH_SHORT)
                                            .show();
                                    mAuth.signInWithEmailAndPassword(email, password);
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    String position = "users/" + currentUser.getUid().toString();
                                    User user = new User(name + " " + surname);
                                    mDatabase.child(position).setValue(user);
                                }
                            }
                        });
            }
        });

    }
}
