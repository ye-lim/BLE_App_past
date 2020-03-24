package com.example.blu_main_test1.BLE_button;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_connect.UartService;
import com.example.blu_main_test1.BLE_connect.connect;
import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;

import java.lang.reflect.Field;

public class abstraction extends AppCompatActivity {
    ImageButton back;
    View positionView;
    UartService m_UartService;
    AppCompatDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstraction);
        back=findViewById(R.id.back);
        positionView = findViewById(R.id.position_view);
        //상단바 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //API level 19
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { //API level 21
                getWindow().setStatusBarColor(Color.TRANSPARENT);  //상단 바 컬러 투명
                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }


        dealStatusBar(); // 상태 표시줄 높이 조정

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);

        Intent bindIntent = new Intent(getApplicationContext(), UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.rong_coffee).setOnClickListener(onClickListener); //롱고버튼 리스너
        findViewById(R.id.presso).setOnClickListener(onClickListener); //에스프레소버튼 리스너
        findViewById(R.id.tea_big).setOnClickListener(onClickListener); //tea대 리스서
        findViewById(R.id.tea_small).setOnClickListener(onClickListener); //tea스몰 리스너




    }


    public void progressON(Activity activity, String message) {

        if (activity == null || activity.isFinishing()) {
            return;
        }


        if (progressDialog != null && progressDialog.isShowing()) {
            progressSET(message);
        } else {

            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.loading);
            progressDialog.show();

        }


        final ImageView img_loading_frame = (ImageView) progressDialog.findViewById(R.id.iv_frame_loading);
        final AnimationDrawable frameAnimation = (AnimationDrawable) img_loading_frame.getBackground();
        img_loading_frame.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }


    }

    public void progressSET(String message) {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }


        TextView tv_progress_message = (TextView) progressDialog.findViewById(R.id.tv_progress_message);
        if (!TextUtils.isEmpty(message)) {
            tv_progress_message.setText(message);
        }

    }

    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void startProgress() {
        progressON(this,"추출중.....");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressOFF();
            }
        },3500);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rong_coffee:
                    if(connect.IsConnect) {
                        String rong_coffee = "03ECL37";
                        byte[] rong_coffee_value = {(byte) 0x02, (byte) 0x03};
                        byte[] rong_coffee__temp = rong_coffee.getBytes();
                        byte[] rong_coffee_temp_data = new byte[rong_coffee__temp.length + 2];
                        System.arraycopy(rong_coffee_value, 0, rong_coffee_temp_data, 0, 1);
                        System.arraycopy(rong_coffee__temp, 0, rong_coffee_temp_data, 1, rong_coffee__temp.length);
                        System.arraycopy(rong_coffee_value, 1, rong_coffee_temp_data, rong_coffee__temp.length + 1, 1);
                        m_UartService.writeRXCharacteristic(rong_coffee_temp_data);
                        startProgress();
                    } else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }

                    break;

                case R.id.presso:
                    if(connect.IsConnect) {
                        String presso_start = "03ECS3E";
                        byte[] presso_value = {(byte) 0x02, (byte) 0x03};
                        byte[] presso_temp = presso_start.getBytes();
                        byte[] presso_temp_data = new byte[presso_temp.length + 2];
                        System.arraycopy(presso_value, 0, presso_temp_data, 0, 1);
                        System.arraycopy(presso_temp, 0, presso_temp_data, 1, presso_temp.length);
                        System.arraycopy(presso_value, 1, presso_temp_data, presso_temp.length + 1, 1);
                        m_UartService.writeRXCharacteristic(presso_temp_data);
                        startProgress();
                    } else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }

                    break;

                case R.id.tea_big:

                    if(connect.IsConnect) {
                        String tea_big_start = "03ETL48";
                        byte[] tea_big_value = {(byte) 0x02, (byte) 0x03};
                        byte[] tea_big_temp = tea_big_start.getBytes();
                        byte[] tea_big_temp_data = new byte[tea_big_temp.length + 2];
                        System.arraycopy(tea_big_value, 0, tea_big_temp_data, 0, 1);
                        System.arraycopy(tea_big_temp, 0, tea_big_temp_data, 1, tea_big_temp.length);
                        System.arraycopy(tea_big_value, 1, tea_big_temp_data, tea_big_temp.length + 1, 1);
                        m_UartService.writeRXCharacteristic(tea_big_temp_data);
                        startProgress();
                    } else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }

                    break;

                case R.id.tea_small:

                    if(connect.IsConnect) {
                        String tea_small_start = "03ETS4F";
                        byte[] tea_small_value = {(byte) 0x02, (byte) 0x03};
                        byte[] tea_small_temp = tea_small_start.getBytes();
                        byte[] tea_small_temp_data = new byte[tea_small_temp.length + 2];
                        System.arraycopy(tea_small_value, 0, tea_small_temp_data, 0, 1);
                        System.arraycopy(tea_small_temp, 0, tea_small_temp_data, 1, tea_small_temp.length);
                        System.arraycopy(tea_small_value, 1, tea_small_temp_data, tea_small_temp.length + 1, 1);
                        m_UartService.writeRXCharacteristic(tea_small_temp_data);
                        startProgress();
                    } else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }
                    break;
            }
        }
    };

    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }




    //시본바 크기만큼 view를 늘려서 메뉴를 기본바 하단으로 이동
    private void dealStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }
    }
    //핸드폰 상단 status bar 높이를 얻는 메소드
    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    //UART service connected/disconnected
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            m_UartService = ((UartService.LocalBinder) rawBinder).getService();

            //Log.d(TAG, "onServiceConnected m_UartService= " + m_UartService);
            if (!m_UartService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onServiceDisconnected(ComponentName classname) {
            if(m_UartService != null) {
                m_UartService.disconnect();
            }
        }
    };

    @Override
    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }


}
