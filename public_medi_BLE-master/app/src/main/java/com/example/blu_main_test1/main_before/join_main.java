package com.example.blu_main_test1.main_before;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;




public class join_main extends AppCompatActivity {
    private FirebaseAuth mAuth;
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        private static final String TAG = "join_main";

        EditText name, year, month, day, phone_number;
        Button man, woman;
        @Override

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_join_main);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.idoverlap).setOnClickListener(onClickListener);
        findViewById(R.id.next).setOnClickListener(onClickListener);

        //툴바 기능
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("회원가입");
    }

    //활동 초기화 시 사용자가 현재 로그인되어 있는지 확인함.
    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    //public void next(View v) 부분과 OnClickListener로 합침
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.idoverlap:
                    idOverlap();
                    break;
                case R.id.next:
                    signUp();
                    break;
            }
        }
    };

    @SuppressLint("ResourceType")
    public void sex(View v){
        switch (v.getId()){
            case R.id.man :
                man.setBackgroundResource(R.xml.join_main_sex);
                man.setTextColor(R.color.white);
                woman.setBackgroundResource(R.xml.join_main_sex2);
                woman.setTextColor(Color.parseColor("#a606234E"));
                break;
            case R.id.woman :
                man.setBackgroundResource(R.xml.join_main_sex2);
                man.setTextColor(Color.parseColor("#a606234E"));
                woman.setBackgroundResource(R.xml.join_main_sex);
                woman.setTextColor(R.color.white);
        }
    }

    //Sign up new users
    private void signUp() {
        //성명
        final String user_name=((EditText)findViewById(R.id.user_name)).getText().toString();

        //gender
        final String user_sex = ((Button)findViewById(R.id.man)).getText().toString();
        final String woman=((Button)findViewById(R.id.woman)).getText().toString();

        //생년월일
        final String year=((EditText)findViewById(R.id.year)).getText().toString();
        final String month=((EditText)findViewById(R.id.month)).getText().toString();
        final String day=((EditText)findViewById(R.id.day)).getText().toString();

        final String user_day = year+"."+month+"."+day;

        //전화번호
        final String user_tel=((EditText) findViewById(R.id.phone_number)).getText().toString();

        final String user_id = ((EditText)findViewById(R.id.user_id)).getText().toString();
        final String user_pw = ((EditText)findViewById(R.id.user_pw)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.userpwdck)).getText().toString();

        if(user_id.length() > 0 && user_pw.length() > 0 && passwordCheck.length() > 0 && user_name.length() > 0 && user_tel.length() > 0){
            if(user_pw.equals(passwordCheck)){
                mAuth.createUserWithEmailAndPassword(user_id, user_pw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in success, update UI with the signed-in user's information
                                    Log.w(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                    Toast.makeText(join_main.this, "회원가입에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(join_main.this, Main_view_pager.class));
                                    //UI
                                    //updateUI(user);

                                    MemberInfo memberInfo = new MemberInfo(user1.getUid(), user_name, user_id, user_pw, user_sex, user_day, user_tel);
                                    if(user != null) {
                                        db.collection("user_info").document(user.getUid()).set(memberInfo)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //showToast(join_main.this, "회원 가입에 성공하였습니다.");
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    //UI
                                    //Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    //Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                    if(task.getException() != null){
                                        Toast.makeText(join_main.this, task.getException().toString(),Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }
                        });
            }else{
                Toast.makeText(join_main.this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(join_main.this, "모든 정보를 기입해 주세요.",Toast.LENGTH_SHORT).show();

        }
    }

    public void idOverlap(){
        final EditText id = ((EditText)findViewById(R.id.user_id));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean checked = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(id.length() > 0 && checkEmail(id.getText().toString())){
                                    if(id.getText().toString().equals((String)document.getData().get("user_id"))){
                                        Toast.makeText(join_main.this, "중복된 아이디입니다.",Toast.LENGTH_SHORT).show();
                                        checked = false;
                                    }
                                }else {
                                    //showToast(join_main.this, "올바르지 않은 이메일 형식입니다."); //창이 계속 뜸
                                    checked = false;
                                }

                            }
                            if (checked) {
                                Toast.makeText(join_main.this, "사용가능한 아이디입니다.",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void userOverlap(){
        final EditText id = ((EditText)findViewById(R.id.user_id));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean checked = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(id.length() > 0 && checkEmail(id.getText().toString())){
                                    if(id.getText().toString().equals((String)document.getData().get("user_id"))){
                                        Toast.makeText(join_main.this, "이미 가입된 회원입니다.",Toast.LENGTH_SHORT).show();
                                        checked = false;
                                    }
                                }else {
                                    //showToast(join_main.this, "올바르지 않은 이메일 형식입니다."); //창이 계속 뜸
                                    checked = false;
                                }

                            }
                            if (checked) {
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static final Pattern BD_PATTERN = Pattern.compile(
            ""
    );

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    /*public void next(View v){
        if(v.getId()==R.id.next){
            //   if(name.getText().toString().equals("")||year.getText().toString().equals("")||month.getText().toString().equals("")||day.getText().toString().equals("")) {
            //    Toast.makeText(getApplicationContext(),"모든 내용을 입력해주세요", Toast.LENGTH_SHORT).show();
            // }
            // else {
            //signUp();
            Intent intent = new Intent(join_main.this, Main_view_pager.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            //   }
        }
    }*/

    //보류
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        return true;
    }

}

/*//뒤로가기 - xml undone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }*/
