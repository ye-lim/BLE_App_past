package com.example.blu_main_test1.main_before;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.main_before.passwordResetActivity;
import com.example.blu_main_test1.R;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;


public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private String TAG = "VideoActivity";
    private VideoView videoView;
    EditText userId, userPwd;
    Button loginBtn, joinBtn, renewPwBtn;
    LinearLayout lin_login, lin_login_small,logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        //상단메뉴 부분
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userId = (EditText) findViewById(R.id.userId);
        userPwd = (EditText) findViewById(R.id.userPwd);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        joinBtn = (Button) findViewById(R.id.joinBtn);
        renewPwBtn = (Button) findViewById(R.id.renewPwBtn);
        lin_login=(LinearLayout)findViewById(R.id.lin_login);
        lin_login_small=(LinearLayout)findViewById(R.id.lin_login_small);
        logo=(LinearLayout) findViewById(R.id.logo);

        userPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        userPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        loginBtn.setOnClickListener(btnListener);
        renewPwBtn.setOnClickListener(btnListener);
        joinBtn.setOnClickListener(btnListener);

        //버전 호환
        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //배경 비디오
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/raw/background2" ));
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
            }
        });


        //애니메이션을 위한 변수
        final Animation animTrans = AnimationUtils.loadAnimation(
                this,R.anim.anim_login);
        final Animation animTrans2 = AnimationUtils.loadAnimation(
                this,R.anim.anim_login_logo);

        //logo 애니메이션
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(animTrans2);
            }
        }, 1000);

        //LinearLayout 애니메이션
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                lin_login.startAnimation(animTrans);
                lin_login.setVisibility(View.VISIBLE);
            }
        },1*1500);

        userId.setVisibility(View.VISIBLE);
        userPwd.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.VISIBLE);
        renewPwBtn.setVisibility(View.VISIBLE);
        joinBtn.setVisibility(View.VISIBLE);
        lin_login_small.setVisibility(View.VISIBLE);
    }

    //로그인 버튼에 따른 설정
    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginBtn : // 로그인 버튼 눌렀을 경우
                    signin();
/*                    String loginid = userId.getText().toString();
                    String loginpwd = userPwd.getText().toString();
                    try {
                        /*String result = new CustomTask().execute(loginid,loginpwd).get();
                        result=result.replaceAll(" ","");
                        if(result.equals("true")) {
                            Toast.makeText(getApplicationContext(),"로그인",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if(result.equals("false")) {
                            Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                            userId.setText("");
                            userPwd.setText("");
                        } else if(result.equals("noId")) {
                            Toast.makeText(getApplicationContext(),"존재하지 않는 회원입니다.",Toast.LENGTH_SHORT).show();
                            userId.setText("");
                            userPwd.setText("");
                        }
                    }catch (Exception e) {} */
                    break;

                case R.id.renewPwBtn : //비밀번호 재설정
                    startActivity(new Intent(login.this, passwordResetActivity.class));
                    finish();
                    break;

                case R.id.joinBtn : // 회원가입
                    //Intent intent = new Intent(getApplicationContext(),join.class); //약관 제외
                    Intent intent = new Intent(getApplicationContext(), join_main.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    };

    private void signin() {
        String email = ((EditText) findViewById(R.id.userId)).getText().toString();
        String password = ((EditText) findViewById(R.id.userPwd)).getText().toString();

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //FirebaseUser user = mAuth.getCurrentUser();
                                //Toast.makeText(getApplicationContext(),"로그인",Toast.LENGTH_SHORT).show();

                                Toast.makeText(login.this, "로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Main_view_pager.class);
                                startActivity(intent);
                                finish();
                            } else {
                                if (task.getException() != null) {
                                    Toast.makeText(login.this, task.getException().toString(),Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });
        } else {
            Toast.makeText(login.this, "이메일 또는 비밀번호를 입력해 주세요.",Toast.LENGTH_SHORT).show();

        }
    }

        /*//jsp와 연결하는 비동기식 로그인
    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://best54.cafe24.com/blu_android_jsp/login.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1];//보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    //jsp에서 보낸 값을 받겠죠?
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값입니다.
            return receiveMsg;
        }
    }*/

}