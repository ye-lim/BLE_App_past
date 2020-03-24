package com.example.blu_main_test1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MypageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextView user_id;
    TextView user_name;
    EditText user_pw;
    EditText user_pw_ch;
    EditText user_tell;
    TextView user_day;
    TextView user_sex;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        user_id = (TextView) findViewById(R.id.user_id);
        user_name = (TextView)findViewById(R.id.user_name);
        user_pw = (EditText)findViewById(R.id.user_pw);
        user_pw_ch = (EditText)findViewById(R.id.user_pw_ch);
        user_tell = (EditText)findViewById(R.id.user_tell);
        user_day = (TextView)findViewById(R.id.user_day);
        user_sex = (TextView)findViewById(R.id.user_sex);




        //리스너
        findViewById(R.id.su_btn).setOnClickListener(onClickListener); //완료 버튼
        findViewById(R.id.pw_up_btn).setOnClickListener(onClickListener); //비밀번호 수정
        findViewById(R.id.tell_up_btn).setOnClickListener(onClickListener); //휴대폰 수정
        db_update();


    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.su_btn:
                    Intent intent = new Intent(MypageActivity.this, com.example.blu_main_test1.Main_page.Main_view_pager.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.pw_up_btn:
                    //pw_change();
                    break;

                case R.id.tell_up_btn:
                    tell_change();
                    break;
            }

        }
    };

    private void db_update(){ //사용자 정보를 user_info db에서 가져오는 메소드
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user_info").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    user_id.setText(document.getData().get("user_id").toString());
                    user_name.setText(document.getData().get("user_name").toString());
                    user_tell.setText(document.getData().get("user_tel").toString());
                    user_day.setText(document.getData().get("user_day").toString());
                    user_sex.setText(document.getData().get("user_sex").toString());
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //  Log.d(TAG, "No such document");
                    }
                } else {
                    //  Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void pw_change(){ //최근에 사용자가 로그인한 경험이 있어야 updatePassword 메소드 동작가능 .
        String new_user_pw = user_pw.getText().toString();
        String new_user_pw_ch = user_pw_ch.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(new_user_pw.equals(new_user_pw_ch)){
            user.updatePassword(new_user_pw);
        }else{
            startToast("비밀번호가 일치하지 않습니다.");
        }

    }

    private void tell_change(){
        String new_user_tell = user_tell.getText().toString();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference user_tell = db.collection("user_info").document(user.getUid()); //로그인 기능 구현되면 user.getUid() 메소드로 해당 유저 접근.
        if(new_user_tell.length() == 11 ){
            user_tell
                    .update("user_tell", new_user_tell)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            startToast("휴대전화 변경에 성공하였습니다.");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } else{
            startToast("휴대전화를 올바르게 입력하세요.");
        }
    }

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}