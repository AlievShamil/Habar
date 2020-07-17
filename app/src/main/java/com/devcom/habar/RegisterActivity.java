package com.devcom.habar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private Button emailLoginBtn, emailRegisterBtn;
    private EditText emailET, passwordET;
    private TextView emailChangeTV;
    ProgressDialog loadingBar;

    FirebaseAuth emailAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailLoginBtn = findViewById(R.id.email_login_btn);
        emailRegisterBtn = findViewById(R.id.email_register_btn);
        emailET = findViewById(R.id.email_input);
        passwordET = findViewById(R.id.pass_input);
        emailChangeTV = findViewById(R.id.email_change_tv);

        emailAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        emailChangeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailChangeTV.setVisibility(View.INVISIBLE);
                emailRegisterBtn.setVisibility(View.INVISIBLE);
                emailLoginBtn.setVisibility(View.VISIBLE);
            }
        });

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
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Заполните поле email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните поле пароль", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Вход в аккаунт");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            emailAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Успешный вход", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                String massage = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Ошибка: " + massage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void createAccount() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Заполните поле email", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Заполните поле пароль", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста подождите");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            emailAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                loadingBar.dismiss();
                                Toast.makeText(RegisterActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                loadingBar.dismiss();
                                String massage = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Ошибка: " + massage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}