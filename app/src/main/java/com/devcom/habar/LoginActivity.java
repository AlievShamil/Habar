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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button nextButton, verificationButton, emailRegisterButton;
    private EditText phoneInput, verificationInput;
    private TextView tv1, tv2;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nextButton = findViewById(R.id.login_next_btn);
        verificationButton = findViewById(R.id.login_verification_btn);
        verificationInput = findViewById(R.id.login_verification_input);
        emailRegisterButton = findViewById(R.id.email_register_btn);
        phoneInput = findViewById(R.id.login_phone_input);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButton.setVisibility(View.INVISIBLE);
                verificationButton.setVisibility(View.VISIBLE);
                verificationInput.setVisibility(View.VISIBLE);
                phoneInput.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);

                String phoneNumber = phoneInput.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(LoginActivity.this, "Заполните номер", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Проверка номера");
                    loadingBar.setMessage("Пожалуйста подождите");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();


                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            LoginActivity.this,
                            mCallbacks
                    );
                }
            }
        });

        emailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntenet = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntenet);
            }
        });

        verificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String verificationCode = verificationInput.getText().toString();

                if (TextUtils.isEmpty(verificationCode)) {
                    Toast.makeText(LoginActivity.this, "Введите код", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Проверка кода");
                    loadingBar.setMessage("Пожалуйста подождите");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Ошибка номера", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.VISIBLE);
                verificationButton.setVisibility(View.INVISIBLE);
                verificationInput.setVisibility(View.INVISIBLE);
                phoneInput.setVisibility(View.VISIBLE);
                tv2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();

                Toast.makeText(LoginActivity.this, "Код отправлен", Toast.LENGTH_SHORT).show();
                nextButton.setVisibility(View.INVISIBLE);
                verificationButton.setVisibility(View.VISIBLE);
                verificationInput.setVisibility(View.VISIBLE);
                phoneInput.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Проверка прошла успешно",Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this,"Ошибка проверки номера",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}