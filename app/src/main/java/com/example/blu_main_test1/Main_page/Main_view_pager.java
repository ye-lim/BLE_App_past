package com.example.blu_main_test1.Main_page;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_connect.UartService;
import com.example.blu_main_test1.BLE_connect.connect;
import com.example.blu_main_test1.Main_page.Main_page2.Main_page_2;
import com.example.blu_main_test1.Main_page.MainActivity;

import com.example.blu_main_test1.Main_page.listadapter;
import com.example.blu_main_test1.Main_page.sampledata;
import com.example.blu_main_test1.MypageActivity;
import com.example.blu_main_test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Main_view_pager extends AppCompatActivity implements View.OnClickListener {
    ArrayList<sampledata> drawDataList;
    private View positionView,menu_icon;
    public Button main1_btn, main2_btn,sub_amount;
    private FragmentAdapter fragmentAdapter;
    private ViewPager viewPager;
    private MainActivity mainActivity;
    private Main_page_2 main_page_2;
    private TabLayout tabLayout;
    private String text,action;
    UartService m_UartService;
    private Timer mTimer[]=new Timer[6];
    private ImageButton amount_change,back,amount_start,product_amount,state_start,amount_stop,low_start;
    private EditText coffee_b_amount,coffee_s_amount,tea_b_amount,tea_s_amount;
    private LinearLayout background,sub_background;
    private TextView draw_state, draw_temper, draw_version, draw_coffee_b,draw_coffee_s, draw_tea_b, draw_tea_s;
    public static TextView draw_connect;
    String amount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewpager);

        amount_change=(ImageButton)findViewById(R.id.amount_change);
        amount_start=(ImageButton)findViewById(R.id.amount_start);
        product_amount=(ImageButton)findViewById(R.id.product_amount);
        amount_stop=(ImageButton)findViewById(R.id.amount_stop);
        low_start=(ImageButton)findViewById(R.id.low_start);

        draw_connect=(TextView)findViewById(R.id.draw_connect);
        draw_state=(TextView)findViewById(R.id.draw_state);
        draw_temper=(TextView)findViewById(R.id.draw_temper);
        draw_version=(TextView)findViewById(R.id.draw_version);
        draw_coffee_b=(TextView)findViewById(R.id.draw_coffee_b);
        draw_coffee_s=(TextView)findViewById(R.id.draw_coffee_s);
        draw_tea_b=(TextView)findViewById(R.id.draw_tea_b);
        draw_tea_s=(TextView)findViewById(R.id.draw_tea_s);

        //상단바 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);  //상단 바 컬러 투명
                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

        positionView = findViewById(R.id.position_view);
        menu_icon = findViewById(R.id.menu_icon);
        //BLE 기능 활성화 유/무
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText(this, "BLE가 활성화 되지 않음", Toast.LENGTH_SHORT).show();
        }

        dealStatusBar();

        //fragment 관련
        viewPager = findViewById(R.id.vp);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());

        viewPager.setAdapter(fragmentAdapter);

        mainActivity = new MainActivity();
        main_page_2 = new Main_page_2();

        fragmentAdapter.addItem(mainActivity);
        fragmentAdapter.addItem(main_page_2);

        fragmentAdapter.notifyDataSetChanged();

        //BLE 관련
        for(int i=0;i<6;i++){
            mTimer[i] = new Timer();
        }


        //ble 연결이 완료 되면 머신의 세부상태 모두 호출
        if(connect.IsConnect) {
            mTimer[0].schedule(new Tea_large(), 1000, 15000);
            mTimer[1].schedule(new State(), 1500, 15000);
            mTimer[2].schedule(new Tea_small(), 2000, 15000);
            mTimer[3].schedule(new Coffee_large(), 2500, 15000);
            mTimer[4].schedule(new Coffee_small(), 3000, 15000);
            mTimer[5].schedule(new version(), 3500, 15000);
        }
        //데이터 받는걸 확인
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UartService.ACTION_DATA_AVAILABLE);

        Intent bindIntent = new Intent(getApplicationContext(), UartService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(UARTStatusChangeReceiver, intentFilter);

        //drawer 슬라이딩
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(!drawer.isDrawerOpen(Gravity.LEFT)){
                    drawer.openDrawer(Gravity.LEFT);
                }
                else{
                    drawer.closeDrawer(Gravity.LEFT);
                }
            }
        });


        this.InitializeMovieData();

        ListView listView = (ListView)findViewById(R.id.listView);
        final listadapter myAdapter = new listadapter(this,drawDataList);

        listView.setAdapter(myAdapter);
        //listview를 통해 자사몰 연동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){

                switch (myAdapter.getItem(position).getGrade()){
                    case "공지사항" :
                        Intent news = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/board/med-%EB%89%B4%EC%8A%A4/2/"));
                        startActivity(news);
                        break;
                    case "회사소개" :
                        Intent company = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/sub/brand01.html"));
                        startActivity(company);
                        break;
                    case "머신 구입하기" :
                        Intent machine = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EC%BB%A4%ED%94%BC%EC%B0%A8%EC%BA%A1%EC%8A%90%EB%A8%B8%EC%8B%A0medi-cntm01%ED%8B%B0%ED%8F%AC%ED%8A%B8%EC%BB%A4%ED%94%BC%EB%A8%B8%EC%8B%A0%EC%BA%A1%EC%8A%90%EC%BB%A4%ED%94%BC%EB%A8%B8%EC%8B%A0/13/category/24/display/2/"));
                        startActivity(machine);
                        break;
                    case "캡슐 구입하기" :
                        Intent capsul = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/category/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%BA%A1%EC%8A%90/25/"));
                        startActivity(capsul);
                        break;
                    case "회원정보 변경" :
                        Intent mypage = new Intent(getApplicationContext(), MypageActivity.class);
                        startActivity(mypage);
                      /*  Toast.makeText(getApplicationContext(),
                                myAdapter.getItem(position).getGrade(),
                                Toast.LENGTH_LONG).show(); */
                        break;

                }
            }
        });



//하단 메뉴바
        main1_btn=(Button)findViewById(R.id.main1_btn);
        main1_btn.setOnClickListener(this);
        main2_btn=(Button)findViewById(R.id.main2_btn);
        main2_btn.setOnClickListener(this);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i)
                {
                    case 0:
                        main1_btn.setTextColor(Color.parseColor("#125B8F"));
                        main2_btn.setTextColor(Color.parseColor("#AAAAAA"));
                        break;
                    case 1:
                        main1_btn.setTextColor(Color.parseColor("#aaaaaa"));
                        main2_btn.setTextColor(Color.parseColor("#125B8F"));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                   //     String token = task.getResult().getToken();
                   //     Log.d("FCM Log", "FCM 토큰: " + token);
                      //  Toast.makeText(Main_view_pager.this, token, Toast.LENGTH_SHORT).show();
                    }
                });



    }
    //버튼기능 활성화
    @SuppressLint("ResourceAsColor")
    public void ble_click(View view) {
    switch (view.getId()){
        //추출량 변경 버튼
        case R.id.amount_change:
            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final LinearLayout linear = (LinearLayout)inflater.inflate(R.layout.activity_amount_change, null);

            //파라미터를 세팅해줌
            final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(

                    LinearLayout.LayoutParams.MATCH_PARENT,

                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            back=(ImageButton)linear.findViewById(R.id.back);
            background=(LinearLayout)linear.findViewById(R.id.background);
            sub_background=(LinearLayout)linear.findViewById(R.id.sub_background);
            coffee_b_amount=(EditText)linear.findViewById(R.id.coffee_b_amount);
            coffee_s_amount=(EditText)linear.findViewById(R.id.coffee_s_amount);
            tea_b_amount=(EditText)linear.findViewById(R.id.tea_b_amount);
            tea_s_amount=(EditText)linear.findViewById(R.id.tea_s_amount);
            sub_amount=(Button)linear.findViewById(R.id.sub_amount);

            //윈도우에 추가시킴
            addContentView(linear,paramlinear);
            //linear부분은 아무 동작 하지 않음
            linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            //뒤로가기
            sub_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm.hideSoftInputFromWindow(coffee_b_amount.getWindowToken(),0);
                }
            });

            //바탕부분 클릭시
            background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                    parentViewGroup.removeView(linear);
                    imm.hideSoftInputFromWindow(main1_btn.getWindowToken(),0);

                }
            });
            //뒤로가기 이미지
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                    parentViewGroup.removeView(linear);
                    imm.hideSoftInputFromWindow(main1_btn.getWindowToken(),0);
                }
            });


            sub_amount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             try {

                 if (coffee_b_amount.getText().toString().length() != 0) {
                     if (Integer.parseInt(coffee_b_amount.getText().toString()) > 990) {
                         Toast.makeText(getApplicationContext(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                         coffee_b_amount.requestFocus();
                         return;
                     }
                     String c_value = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2);
                     String cb_amount = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2) + stringToHex(c_value);
                     byte[] cb_value = {(byte) 0x02, (byte) 0x03};
                     byte[] cb_temp = cb_amount.getBytes();
                     byte[] cb_temp_data = new byte[cb_temp.length + 2];
                     System.arraycopy(cb_value, 0, cb_temp_data, 0, 1);
                     System.arraycopy(cb_temp, 0, cb_temp_data, 1, cb_temp.length);
                     System.arraycopy(cb_value, 1, cb_temp_data, cb_temp.length + 1, 1);
                     m_UartService.writeRXCharacteristic(cb_temp_data);
                 }


                 if (coffee_s_amount.getText().toString().length() != 0) {
                     if (Integer.parseInt(coffee_s_amount.getText().toString()) > 990) {
                         Toast.makeText(getApplicationContext(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                         coffee_s_amount.requestFocus();
                         return;
                     }
                     new Handler().postDelayed(new Runnable() {
                         @Override

                         public void run() {

                             String c_value2 = "05TCS" + coffee_s_amount.getText().toString().substring(0, 2);
                             String cs_amount = "05TCS" + coffee_s_amount.getText().toString().substring(0, 2) + stringToHex(c_value2);
                             byte[] cs_value = {(byte) 0x02, (byte) 0x03};
                             byte[] cs_temp = cs_amount.getBytes();
                             byte[] cs_temp_data = new byte[cs_temp.length + 2];
                             System.arraycopy(cs_value, 0, cs_temp_data, 0, 1);
                             System.arraycopy(cs_temp, 0, cs_temp_data, 1, cs_temp.length);
                             System.arraycopy(cs_value, 1, cs_temp_data, cs_temp.length + 1, 1);

                             m_UartService.writeRXCharacteristic(cs_temp_data);
                         }

                     }, 1000);  // 1 초 후에 실행
                 }


                 if (tea_b_amount.getText().toString().length() != 0) {
                     if (Integer.parseInt(tea_b_amount.getText().toString()) > 990) {
                         Toast.makeText(getApplicationContext(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                         tea_b_amount.requestFocus();
                         return;
                     }
                     new Handler().postDelayed(new Runnable() {
                         @Override

                         public void run() {

                             String t_value = "05TTL" + tea_b_amount.getText().toString().substring(0, 2);
                             String tb_amount = "05TTL" + tea_b_amount.getText().toString().substring(0, 2) + stringToHex(t_value);
                             byte[] tb_value = {(byte) 0x02, (byte) 0x03};
                             byte[] tb_temp = tb_amount.getBytes();
                             byte[] tb_temp_data = new byte[tb_temp.length + 2];
                             System.arraycopy(tb_value, 0, tb_temp_data, 0, 1);
                             System.arraycopy(tb_temp, 0, tb_temp_data, 1, tb_temp.length);
                             System.arraycopy(tb_value, 1, tb_temp_data, tb_temp.length + 1, 1);
                             m_UartService.writeRXCharacteristic(tb_temp_data);
                         }

                     }, 1500);  // 1 초 후에 실행
                 }


                 if (tea_s_amount.getText().toString().length() != 0) {
                     if (Integer.parseInt(tea_s_amount.getText().toString()) > 990) {
                         Toast.makeText(getApplicationContext(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                         tea_s_amount.requestFocus();
                         return;
                     }
                     new Handler().postDelayed(new Runnable() {
                         @Override

                         public void run() {

                             String t_value2 = "05TTS" + tea_s_amount.getText().toString().substring(0, 2);
                             String ts_amount = "05TTS" + tea_s_amount.getText().toString().substring(0, 2) + stringToHex(t_value2);
                             byte[] ts_value = {(byte) 0x02, (byte) 0x03};
                             byte[] ts_temp = ts_amount.getBytes();
                             byte[] ts_temp_data = new byte[ts_temp.length + 2];
                             System.arraycopy(ts_value, 0, ts_temp_data, 0, 1);
                             System.arraycopy(ts_temp, 0, ts_temp_data, 1, ts_temp.length);
                             System.arraycopy(ts_value, 1, ts_temp_data, ts_temp.length + 1, 1);
                             m_UartService.writeRXCharacteristic(ts_temp_data);
                         }

                     }, 2000);  // 1 초 후에 실행
                 }


                 ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                 parentViewGroup.removeView(linear);
             }catch (Exception e)
             {

                 Toast.makeText(getApplicationContext(),"블루투스를 재연결해 주세요",Toast.LENGTH_SHORT).show();

             }
                        }

            });

            break;
        case R.id.state_start:
            if(connect.IsConnect) {
                String start = "01WB8";
                byte[] value = {(byte) 0x02, (byte) 0x03};
                byte[] temp = start.getBytes();
                byte[] temp_data = new byte[temp.length + 2];
                System.arraycopy(value, 0, temp_data, 0, 1);
                System.arraycopy(temp, 0, temp_data, 1, temp.length);
                System.arraycopy(value, 1, temp_data, temp.length + 1, 1);
                m_UartService.writeRXCharacteristic(temp_data);
            }
            break;
        case R.id.low_start:
            if(connect.IsConnect) {
                String low_start = "01PB1";
                byte[] low_value = {(byte) 0x02, (byte) 0x03};
                byte[] low_temp = low_start.getBytes();
                byte[] low_temp_data = new byte[low_temp.length + 2];
                System.arraycopy(low_value, 0, low_temp_data, 0, 1);
                System.arraycopy(low_temp, 0, low_temp_data, 1, low_temp.length);
                System.arraycopy(low_value, 1, low_temp_data, low_temp.length + 1, 1);
                m_UartService.writeRXCharacteristic(low_temp_data);
            }
            break;

        case R.id.amount_stop:
            if(connect.IsConnect) {
                String stop_start = "01SB4";
                byte[] stop_value = {(byte) 0x02, (byte) 0x03};
                byte[] stop_temp = stop_start.getBytes();
                byte[] stop_temp_data = new byte[stop_temp.length + 2];
                System.arraycopy(stop_value, 0, stop_temp_data, 0, 1);
                System.arraycopy(stop_temp, 0, stop_temp_data, 1, stop_temp.length);
                System.arraycopy(stop_value, 1, stop_temp_data, stop_temp.length + 1, 1);
                m_UartService.writeRXCharacteristic(stop_temp_data);
            }
                break;

    }
    }

    public static String stringToHex(String s) {
        int ch2=0;
        for (int i = 0; i < s.length(); i++) {
            byte ch=(byte)s.charAt( i );
             ch2+=(byte)ch;
        }
        String s5=Integer.toHexString(ch2);
        return s5.substring(s5.length()-2,s5.length());
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


            private void InitializeMovieData() {
                drawDataList = new ArrayList<sampledata>();

                drawDataList.add(new sampledata("공지사항"));
                drawDataList.add(new sampledata("회사소개"));
                drawDataList.add(new sampledata("회원정보 변경"));
                drawDataList.add(new sampledata("머신 구입하기"));
                drawDataList.add(new sampledata("캡슐 구입하기"));
            }



            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.main1_btn :
                        viewPager.setCurrentItem(0,true);
                        main1_btn.setTextColor(Color.parseColor("#125B8F"));
                        main2_btn.setTextColor(Color.parseColor("#AAAAAA"));
                        break;
                    case R.id.main2_btn :
                        viewPager.setCurrentItem(1,true);
                        main1_btn.setTextColor(Color.parseColor("#aaaaaa"));
                        main2_btn.setTextColor(Color.parseColor("#125B8F"));
                        break;
                }
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
       for(int i=0;i<6;i++){
           mTimer[i].cancel();
       }
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    class Tea_large extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};
                    TL_amount = "03QTL54";  //현재

                    byte[] temp = TL_amount.getBytes();
                    byte[] temp_data = new byte[temp.length + 2];

                    System.arraycopy(value, 0, temp_data, 0, 1);
                    System.arraycopy(temp, 0, temp_data, 1, temp.length);
                    System.arraycopy(value, 1, temp_data, temp.length + 1, 1);
                    m_UartService.writeRXCharacteristic(temp_data);

                }
            }
        }
    }
    class State extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QST5B"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    m_UartService.writeRXCharacteristic(state_data);
                }
            }
        }
    }

    class Tea_small extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QTS5B"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    m_UartService.writeRXCharacteristic(state_data);
                }
            }
        }
    }
    class Coffee_large extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QCL43"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    m_UartService.writeRXCharacteristic(state_data);
                }
            }
        }
    }
    class Coffee_small extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QCS4A"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    m_UartService.writeRXCharacteristic(state_data);
                }
            }
        }
    }
    class version extends TimerTask {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void run() {
            if(connect.IsConnect) {
                if (m_UartService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QVE4F"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    m_UartService.writeRXCharacteristic(state_data);
                }
            }
        }
    }

       private final BroadcastReceiver UARTStatusChangeReceiver = new BroadcastReceiver() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onReceive(final Context context, Intent intent) {
            String action = intent.getAction();
            final MainActivity main=new MainActivity();
            if (action.equals(UartService.ACTION_DATA_AVAILABLE)) {
                final byte[] txValue = intent.getByteArrayExtra(UartService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            //string형식으로 리스트 뷰에 표현
                            text = new String(txValue, "UTF-8");
                            if(text.substring(1,6).equals("07RST")){
                                switch(text.substring(6,8)){
                                    case "00" :
                                        main.state.setText("절전모드");
                                        draw_state.setText("절전모드");
                                        break;
                                    case "10" :
                                        main.state.setText("가열중");
                                        draw_state.setText("가열중");
                                        break;
                                    case "20" :
                                        main.state.setText("추출대기");
                                        draw_state.setText("추출대기");
                                        break;
                                    case "91":
                                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                                        // 제목셋팅
                                        alertDialogBuilder.setTitle("에러 발생");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder .setMessage("머신 내부 온도가 너무 높습니다. 절전모드 하시겠습니까?")
                                                .setCancelable(false)
                                                .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                                    public void onClick( DialogInterface dialog, int id) {
                                                        // 다이얼로그를 취소한다
                                                        dialog.cancel();

                                                    }
                                                })
                                                .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                                    public void onClick( DialogInterface dialog, int id) {

                                                        String start = "01PB1";
                                                        byte [] value={(byte)0x02,(byte)0x03};
                                                        byte[] temp=start.getBytes();
                                                        byte[] temp_data= new byte[temp.length+2];
                                                        System.arraycopy(value,0,temp_data,0,1);
                                                        System.arraycopy(temp,0,temp_data,1,temp.length);
                                                        System.arraycopy(value,1,temp_data,temp.length+1,1);
                                                        m_UartService.writeRXCharacteristic(temp_data);

                                                        dialog.cancel();
                                                    }
                                                }); // 다이얼로그 생성
                                        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create(); // 다이얼로그 보여주기
                                        alertDialog.show();
                                        break;
                                    case "92":
                                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder2 = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                                        // 제목셋팅
                                        alertDialogBuilder2.setTitle("에러 발생");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder2 .setMessage("온도퓨즈가 단선되었습니다. 고객센터로 문의주세요")
                                                .setCancelable(false)
                                                .setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                                    public void onClick( DialogInterface dialog, int id) {
                                                        // 다이얼로그를 취소한다
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setNegativeButton("예", new DialogInterface.OnClickListener() {
                                                    public void onClick( DialogInterface dialog, int id) {
                                                        String start = "01PB1";
                                                        byte [] value={(byte)0x02,(byte)0x03};
                                                        byte[] temp=start.getBytes();
                                                        byte[] temp_data= new byte[temp.length+2];
                                                        System.arraycopy(value,0,temp_data,0,1);
                                                        System.arraycopy(temp,0,temp_data,1,temp.length);
                                                        System.arraycopy(value,1,temp_data,temp.length+1,1);
                                                        m_UartService.writeRXCharacteristic(temp_data);

                                                        dialog.cancel();
                                                    }
                                                }); // 다이얼로그 생성
                                        android.support.v7.app.AlertDialog alertDialog2 = alertDialogBuilder2.create(); // 다이얼로그 보여주기
                                        alertDialog2.show();
                                        break;
                                    case "93":
                                        android.support.v7.app.AlertDialog.Builder alertDialogBuilder3 = new android.support.v7.app.AlertDialog.Builder(getApplicationContext());
                                        // 제목셋팅
                                        alertDialogBuilder3.setTitle("에러 발생");
                                        // AlertDialog 셋팅
                                        alertDialogBuilder3 .setMessage("물 보충이 필요합니다.")
                                                .setCancelable(false)
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    public void onClick( DialogInterface dialog, int id) {
                                                        // 다이얼로그를 취소한다
                                                        dialog.cancel();
                                                    }
                                                });
                                        // 다이얼로그 생성
                                        AlertDialog alertDialog3 = alertDialogBuilder3.create(); // 다이얼로그 보여주기
                                        alertDialog3.show();
                                        break;
                                }
                                main.temper.setText(text.substring(9,11));
                                draw_temper.setText(text.substring(9,11));
                            }
                            else if(text.substring(1,6).equals("05RCL")){
                                main.coffee_b.setText(Integer.parseInt(text.substring(6,8))*10 + "ml");
                                draw_coffee_b.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                                coffee_b_amount.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                            }
                            else if(text.substring(1,6).equals("05RCS")){
                                main.coffee_s.setText(Integer.parseInt(text.substring(6,8))*10 + "ml");
                                draw_coffee_s.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                                coffee_s_amount.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                            }
                            else if(text.substring(1,6).equals("05RTL")){
                                main.tea_b.setText(Integer.parseInt(text.substring(6,8))*10 + "ml");
                                draw_tea_b.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                                tea_b_amount.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                            }
                            else if(text.substring(1,6).equals("05RTS")){
                                main.tea_s.setText(Integer.parseInt(text.substring(6,8))*10 + "ml");
                                draw_tea_s.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                                tea_s_amount.setText(Integer.parseInt(text.substring(6,8))*10+"ml");
                            }
                            else if(text.substring(1,6).equals("0FRVE")){
                                draw_version.setText(text.substring(6,18));
                            }

                            else
                                Toast.makeText(getApplicationContext(),text.substring(1,6),Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };





}



class FragmentAdapter extends FragmentPagerAdapter {

    // ViewPager에 들어갈 Fragment들을 담을 리스트
    private ArrayList<Fragment> fragments = new ArrayList<>();

    // 필수 생성자
    FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    // List에 Fragment를 담을 함수
    void addItem(Fragment fragment) {
        fragments.add(fragment);
    }
}

