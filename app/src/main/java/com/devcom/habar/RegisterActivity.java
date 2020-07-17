package com.devcom.habar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button emailLoginBtn, emailRegisterBtn;
    private EditText emailET, passwordET;
    private TextView emailChangeTV;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rerister);

        emailLoginBtn = findViewById(R.id.email_login_btn);
        emailRegisterBtn = findViewById(R.id.email_register_btn);
        emailET = findViewById(R.id.email_input);
        passwordET = findViewById(R.id.pass_input);
        emailChangeTV = findViewById(R.id.email_change_tv);

        mAuth = FirebaseAuth.getInstance();

        emailRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
        emailLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInAccount();
            }
        });
    }

    private void signInAccount() {
        // метод должен реализовать вход
    }

    private void createAccount() {
        // метод должен реализовать создание акка
    }
}