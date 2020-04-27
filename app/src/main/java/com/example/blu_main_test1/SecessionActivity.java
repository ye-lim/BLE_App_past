package com.example.blu_main_test1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blu_main_test1.main_before.login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecessionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secession);



        findViewById(R.id.secession_btn).setOnClickListener(btnListener);
        findViewById(R.id.secession_btn).setEnabled(false);
        findViewById(R.id.secession_btn).setFocusable(false);
        findViewById(R.id.sertification_btn).setOnClickListener(btnListener);
        mAuth = FirebaseAuth.getInstance();

    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sertification_btn:
                    signIn();
                    break;

                case R.id.secession_btn:
                    secession();
                    break;
            }
        }
    };

    private void signIn() {
        String email = ((EditText)findViewById(R.id.user_id)).getText().toString();
        String password = ((EditText)findViewById(R.id.user_pw)).getText().toString();

        if(email.length() > 0 && password.length() >0){

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SecessionActivity.this, "인증에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                findViewById(R.id.secession_btn).setEnabled(true);
                                findViewById(R.id.secession_btn).setFocusable(true);
                                findViewById(R.id.secession_btn).setBackgroundColor(getResources().getColor(R.color.join));
                            } else {
                                if(task.getException() != null){
                                    Toast.makeText(SecessionActivity.this, "회원정보가 일치 하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
        }else{
            Toast.makeText(SecessionActivity.this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void secession(){
        email = ((EditText)findViewById(R.id.user_id));

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(SecessionActivity.this);
        alert_confirm.setMessage("정말 계정을 삭제 하겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences auto = getSharedPreferences("auto",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                editor.clear();
                editor.commit();


                FirebaseUser user = mAuth.getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                //CollectionReference result = db.collection("user_info");

                String del_user = email.getText().toString();
                /*Query query = result.whereEqualTo("user_id", del_user);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                String del_user_uid = document.getData().get("uid").toString();
                            }
                        }
                });*/

                if(del_user == user.getEmail()) {
                    db.collection("user_info").document(user.getUid())
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    user.delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(SecessionActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), login.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SecessionActivity.this, "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert_confirm.show();
    }

}




