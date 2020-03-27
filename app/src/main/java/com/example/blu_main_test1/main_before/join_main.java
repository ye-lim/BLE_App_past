package com.example.blu_main_test1.main_before;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
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

import org.w3c.dom.Text;

import java.util.regex.Pattern;


public class join_main extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "join_main";

    private EditText user_name, user_id, user_pw, userpwdck, year, month,day, user_tel;
    Button man, woman;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_main);

        mAuth = FirebaseAuth.getInstance();

        man = (Button)findViewById(R.id.man);
        woman = (Button)findViewById(R.id.woman);

        findViewById(R.id.idoverlap).setOnClickListener(onClickListener);
        findViewById(R.id.next).setOnClickListener(onClickListener);


        user_name = (EditText)findViewById(R.id.user_name);
        user_id = (EditText)findViewById(R.id.user_id);
        user_pw = (EditText)findViewById(R.id.user_pw);
        userpwdck = (EditText)findViewById(R.id.userpwdck);
        year = (EditText)findViewById(R.id.year);
        month = (EditText)findViewById(R.id.month);
        day = (EditText)findViewById(R.id.day);
        user_tel = (EditText)findViewById(R.id.phone_number);

        user_name.addTextChangedListener(textWatcher);
        user_id.addTextChangedListener(textWatcher);
        user_pw.addTextChangedListener(textWatcher);
        userpwdck.addTextChangedListener(textWatcher);
        year.addTextChangedListener(textWatcher);
        month.addTextChangedListener(textWatcher);
        day.addTextChangedListener(textWatcher);
        user_tel.addTextChangedListener(textWatcher);
        findViewById(R.id.next).setEnabled(false);
        findViewById(R.id.next).setFocusable(false);




        //툴바 기능
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("회원가입.");

        //textWatcher
        //phone_number.addTextChangedListener(new ValidationTextWatcher(phone_number));
        //editText.addTextChangedListener(new ValidationTextWatcher(editText));
    }

    private final  TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if(user_id.getText().toString().length() > 0 && user_name.getText().toString().length() > 0 && user_pw.getText().toString().length() > 0 &&
                    userpwdck.getText().toString().length() > 0 && year.getText().toString().length() > 0 && month.getText().toString().length() > 0 &&
                    day.getText().toString().length() > 0 && user_tel.getText().toString().length() >0) {

                findViewById(R.id.next).setEnabled(true);
                findViewById(R.id.next).setFocusable(true);
                findViewById(R.id.next).setBackgroundColor(getResources().getColor(R.color.join));

            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

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
                    ifOverlap_ID = true;
                    break;
                case R.id.next:
                    if(!ifOverlap_ID) {
                        Toast.makeText(join_main.this, "아이디 중복확인을 해주세요", Toast.LENGTH_SHORT).show();
                    } else
                        signUp();
                    break;
            }
        }
    };

    @SuppressLint("ResourceType")
    public void sex (View v){
        switch (v.getId()){
            case R.id.man :
                man.setBackgroundResource(R.xml.join_main_sex);
                man.setTextColor(R.color.white);
                woman.setBackgroundResource(R.xml.join_main_sex2);
                woman.setTextColor(Color.parseColor("#a606234E"));
                whichSex = true;
                break;
            case R.id.woman :
                man.setBackgroundResource(R.xml.join_main_sex2);
                man.setTextColor(Color.parseColor("#a606234E"));
                woman.setBackgroundResource(R.xml.join_main_sex);
                woman.setTextColor(R.color.white);
                whichSex = false;
                break;
        }
    }

    boolean whichSex = true;
    boolean ifOverlap_ID = false;


    //Sign up new users
    private void signUp() {
        //성명
        final String user_name=((EditText)findViewById(R.id.user_name)).getText().toString();

        //gender
        final String female = woman.getText().toString();
        final String male =  man.getText().toString();
        final String user_sex;
        if (whichSex) {
            user_sex = male;
        } else user_sex = female;

        //생년월일
        final String year=((EditText)findViewById(R.id.year)).getText().toString();
        final String month=((EditText)findViewById(R.id.month)).getText().toString();
        final String day=((EditText)findViewById(R.id.day)).getText().toString();

        final String user_day = year+"."+month+"."+day;

        //전화번호
        final String user_tel=((EditText) findViewById(R.id.phone_number)).getText().toString();

        //아이디, 비밀번호, 비밀번호확인
        final String user_id = ((EditText)findViewById(R.id.user_id)).getText().toString();
        final String user_pw = ((EditText)findViewById(R.id.user_pw)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.userpwdck)).getText().toString();

        //TextInputEditText password;
        //password = (TextInputEditText) findViewById(R.id.user_pw);


        if(user_id.length() > 0 && user_pw.length() > 0 && passwordCheck.length() > 0 && user_name.length() > 0 && user_tel.length() > 0){
            if(user_pw.equals(passwordCheck) && checkPhoneNum(user_tel)){
                mAuth.createUserWithEmailAndPassword(user_id, user_pw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Sign in success, update UI with the signed-in user's information
                                    Log.w(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                                    Toast.makeText(join_main.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(join_main.this, Main_view_pager.class));
                                    //UI
                                    //updateUI(user);

                                    MemberInfo memberInfo = new MemberInfo(user1.getUid(), user_name, user_id, user_pw, user_sex, user_day, user_tel);
                                    if(user != null) {
                                        db.collection("user_info").document(user.getUid()).set(memberInfo)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        //Toast.makeText(join_main.this, "회원 가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(join_main.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }else{
                Toast.makeText(join_main.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(join_main.this, "모든 정보를 기입해 주세요..", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(join_main.this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        checked = false;
                                    }
                                }else {
                                    //Toast.makeText(join_main.this, "올바르지 않은 이메일 형식입니다.", Toast.LENGTH_SHORT).show(); //창이 계속 뜸
                                    checked = false;
                                }

                            }
                            if (checked) {
                                Toast.makeText(join_main.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void userOverlap(){ //제작중
        final EditText name = ((EditText)findViewById(R.id.user_name));
        final EditText tel = ((EditText)findViewById(R.id.phone_number));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_info")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean checked2 = true;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(tel.getText().toString().equals((String)document.getData().get("user_tel")) && name.getText().toString().equals((String)document.getData().get("user_name"))){
                                    Toast.makeText(join_main.this, "이미 가입된 회원입니다.", Toast.LENGTH_SHORT).show();
                                    checked2 = false;
                                }else {
                                    //showToast(join_main.this, "올바르지 않은 이메일 형식입니다."); //창이 계속 뜸
                                    //checked2 = false;
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    /*private class ValidationTextWatcher implements TextWatcher {
        private View view;
        private ValidationTextWatcher(View view) {
            this.view = view;
        }
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            //텍스트 변결 후 발생할 이벤트를 작성.
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            //텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성
        }

        /*@Override
        public void afterTextChanged(Editable editable) {
            //테스트가 변경될때마다 발생할 이벤트를 작성.
            switch (view.getId()) {
                case R.id.phone_number:
                    checkPhoneNum();
                    break;
                case R.id.user_id:
                    checkEmail();
                    break;
            }
        }*/
    //}

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static final Pattern PHONE_PATTERN = Pattern.compile(
            "^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$"
    );

    private void requestFocus(View view){
        if (view.requestFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    /*private boolean validatePassword(String pwpw) {
        if(pwpw.getText().toString().trim.isEmpty()) {
            pass.setError("Password is required");
            requestFocus(pwpw);
            return false;
        }
        else if(pwpw.getText().toString().length() < 6) {
            pass.setError("Password can't be less than 6 digit");
            requestFocus(pwpw);
            return false;
        }
        else pass.setErrorEnabled(false);
        return true;
    }*/
    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    private boolean checkPhoneNum(String user_phone){
        return PHONE_PATTERN.matcher(user_phone).matches();
    }


    /*public void textWatcher(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //텍스트 변경 후 발생할 이벤트를 작성.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //텍스트의 길이가 변경되었을 경우 발생할 이벤트를 작성
            }

            @Override
            public void afterTextChanged(Editable editable) {
               //테스트가 변경될때마다 발생할 이벤트를 작성.
               if(editable.toString().length()<6){
                    email.setError("Email");
               } else email.setErrorEnabled(false);
            }
        };
    }*/

    //check validate function
    /*private boolean checkEverything(String user_pw) {
        boolean checkE = false;
        if(checkPhoneNum(user_pw)) {
            checkE = true;
        }
        else {
            checkE = false;
            Toast.makeText(join_main.this, "핸드폰 번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
        }
        if(checkEmail(String user_email)) {
            checkE = true;
        }
        else {
            checkE = false;
            Toast.makeText(join_main.this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
        }
        if(!isEmpty()) checkE = true;
        else checkE = false;
        return true;
    }*/

    /*기존코드
    public void next(View v){
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