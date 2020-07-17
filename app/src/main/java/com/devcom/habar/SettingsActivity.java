package com.devcom.habar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button saveInfoBtn;
    private EditText userNameET, statusET;
    private CircleImageView circleImageView;
    private String currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveInfoBtn = findViewById(R.id.save_user_info);
        userNameET = findViewById(R.id.set_user_name);
        statusET = findViewById(R.id.set_user_status);
        circleImageView = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
            }
        });
    }
        private void updateUserInfo() {
            String setName = userNameET.getText().toString();
            String setStatus = statusET.getText().toString();

            if (TextUtils.isEmpty(setName)) {
                Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(setStatus)) {
                Toast.makeText(this, "Введите статус", Toast.LENGTH_SHORT).show();
            } else {
                HashMap<String, Object> profileHashMap = new HashMap<>();
                profileHashMap.put("uid", currentUserID);
                profileHashMap.put("name", setName);
                profileHashMap.put("status", setStatus);

                rootRef
                        .child("Users")
                        .child(currentUserID)
                        .setValue(profileHashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Информация обновлена", Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(SettingsActivity.this, MainActivity.class);
                                    startActivity(mainIntent);
                                } else {
                                    String message = task.getException().toString();
                                    Toast.makeText(SettingsActivity.this, "Произошла ошибка:" + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
    }
}