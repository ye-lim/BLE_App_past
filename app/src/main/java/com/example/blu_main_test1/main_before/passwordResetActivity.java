package com.example.blu_main_test1.main_before;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blu_main_test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class passwordResetActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "passwordResetActivity";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        findViewById(R.id.sendButton).setOnClickListener(onClickListener);
        findViewById(R.id.loginButton).setOnClickListener(onClickListener);

        /*
        //툴바 기능
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("비밀번호 재설정");

         */
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.sendButton:
                    send();
                    break;
                case R.id.loginButton:
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                    finish();
            }
        }
    };

    private void send() {
        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString().trim();
        if (email.length() > 0) {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(passwordResetActivity.this, "이메일을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(passwordResetActivity.this, "가입된 이메일이 없습니다. 다시 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(passwordResetActivity.this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
        }
    }
}