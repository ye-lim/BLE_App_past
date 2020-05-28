package com.example.blu_main_test1.Main_page.Main_page2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.blu_main_test1.BLE_SCAN.BluetoothLeService;
import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.abstraction_fragment;
import com.example.blu_main_test1.amount_change_viewpager.ImgViewPagerAdapter_blend;
import com.example.blu_main_test1.amount_change_viewpager.ImgViewPagerAdapter_hancha;
import com.example.blu_main_test1.amount_change_viewpager.ImgViewPagerAdapter_total;
import com.example.blu_main_test1.amount_change_viewpager.ImgViewPagerAdapter_tradition;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_blend;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_hancha;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_total;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_tradition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.blu_main_test1.abstraction_fragment.progressDialog;


public class product_amount extends AppCompatActivity {

    public static ViewPager tradition_viewpager,hancha_viewpager,blend_viewpager,total_viewpager;
    public static ViewPager tradition_viewpager_img,hancha_viewpager_img,blend_viewpager_img,total_viewpager_img;
    private TextViewPagerAdapter_tradition tradition_pagerAdapter;
    private TextViewPagerAdapter_hancha hancha_pagerAdapter;
    private TextViewPagerAdapter_blend blend_pagerAdapter;
    private ImgViewPagerAdapter_tradition tradition_pagerAdapter_img;
    private ImgViewPagerAdapter_hancha hancha_pagerAdapter_img;
    private ImgViewPagerAdapter_blend blend_pagerAdapter_img;
    private TextViewPagerAdapter_total total_pagerAdapter;
    private ImgViewPagerAdapter_total total_pagerAdapter_img;
    private TextView kind, name,amount,origin;
    private Button press_btn;
    private ImageView left_btn, right_btn,search_btn,select_img,back_btn, plus_btn, minus_btn;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    BluetoothLeService bluetoothLeService;
    private String text;
    private int pos;
    private ProgressBar pgb;
    private EditText search_edit;
    private Boolean success = false;
    private InputMethodManager imm;

    private TabLayout tabLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_amount);

        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        pgb = (ProgressBar)findViewById(R.id.progressBar5);
        pgb.setVisibility(View.VISIBLE);
        kind = (TextView)findViewById(R.id.kind);
        name = (TextView)findViewById(R.id.name);
        amount =(TextView)findViewById(R.id.amount);
        origin =(TextView)findViewById(R.id.origin);
        press_btn = (Button)findViewById(R.id.press_btn);
        press_btn.setOnClickListener(onClickListener);

        left_btn = (ImageView)findViewById(R.id.left_btn);
        left_btn.setOnClickListener(onClickListener);
        right_btn = (ImageView)findViewById(R.id.right_btn);
        right_btn.setOnClickListener(onClickListener);
        search_btn = (ImageView)findViewById(R.id.search_image);
        search_btn.setOnClickListener(onClickListener);
        search_edit = (EditText)findViewById(R.id.search_edit);
        select_img = (ImageView)findViewById(R.id.select_img);
        select_img.setVisibility(View.GONE);
        back_btn = (ImageView)findViewById(R.id.backclose);
        back_btn.setOnClickListener(onClickListener);
        plus_btn = (ImageView)findViewById(R.id.plus_amount);
        plus_btn.setOnClickListener(onClickListener);
        minus_btn = (ImageView)findViewById(R.id.minus_amount);
        minus_btn.setOnClickListener(onClickListener);

        tradition_viewpager = findViewById(R.id.tradition_viewpager);
        tradition_viewpager.setClipToPadding(false);
        tradition_viewpager.setOffscreenPageLimit(5);

        int dpValue = 60;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        tradition_viewpager.setPadding(margin,0, margin, 0);
        tradition_viewpager.setPageMargin(0);

        hancha_viewpager =findViewById(R.id.hancha_viewpager);
        hancha_viewpager.setClipToPadding(false);
        hancha_viewpager.setOffscreenPageLimit(5);
        hancha_viewpager.setPadding(margin,0, margin, 0);
        hancha_viewpager.setPageMargin(0);


        int dpValue_blend = 55;
        int margin_blend = (int) (dpValue_blend * d);
        blend_viewpager = findViewById(R.id.blend_viewpager);
        blend_viewpager.setClipToPadding(false);
        blend_viewpager.setOffscreenPageLimit(5);
        blend_viewpager.setPadding(margin_blend,0, margin_blend, 0);
        blend_viewpager.setPageMargin(0);

        total_viewpager =findViewById(R.id.total_viewpager);
        total_viewpager.setClipToPadding(false);
        total_viewpager.setOffscreenPageLimit(5);
        total_viewpager.setPadding(margin_blend,0, margin_blend, 0);
        total_viewpager.setPageMargin(0);


        tradition_pagerAdapter = new TextViewPagerAdapter_tradition(this);
        tradition_viewpager.setAdapter(tradition_pagerAdapter);

        hancha_pagerAdapter = new TextViewPagerAdapter_hancha(this);
        hancha_viewpager.setAdapter(hancha_pagerAdapter);

        blend_pagerAdapter = new TextViewPagerAdapter_blend(this);
        blend_viewpager.setAdapter(blend_pagerAdapter);

        total_pagerAdapter = new TextViewPagerAdapter_total(this);
        total_viewpager.setAdapter(total_pagerAdapter);

        tradition_viewpager.setVisibility(View.GONE);
        hancha_viewpager.setVisibility(View.GONE);
        blend_viewpager.setVisibility(View.GONE);

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tradition_value();
                pgb.setVisibility(View.GONE);
            }
        },1000);


        tradition_viewpager_img = findViewById(R.id.tradition_viewpagerimg);
        hancha_viewpager_img = findViewById(R.id.hancha_viewpagerimg);
        blend_viewpager_img = findViewById(R.id.blend_viewpagerimg);
        total_viewpager_img = findViewById(R.id.total_viewpagerimg);

        tradition_pagerAdapter_img = new ImgViewPagerAdapter_tradition(this);
        tradition_viewpager_img.setAdapter(tradition_pagerAdapter_img);

        hancha_pagerAdapter_img = new ImgViewPagerAdapter_hancha(this);
        hancha_viewpager_img.setAdapter(hancha_pagerAdapter_img);

        blend_pagerAdapter_img = new ImgViewPagerAdapter_blend(this);
        blend_viewpager_img.setAdapter(blend_pagerAdapter_img);

        total_pagerAdapter_img = new ImgViewPagerAdapter_total(this);
        total_viewpager_img.setAdapter(total_pagerAdapter_img);

        int dpValue_img = 90;
        int margin_img = (int)(dpValue_img * d);

        tradition_viewpager_img.setClipToPadding(false);
        tradition_viewpager_img.setPadding(margin_img,0, margin_img, 0);
        tradition_viewpager_img.setPageMargin(margin/2);
        tradition_viewpager_img.setOffscreenPageLimit(5);

        hancha_viewpager_img.setClipToPadding(false);
        hancha_viewpager_img.setPadding(margin_img,0, margin_img, 0);
        hancha_viewpager_img.setPageMargin(margin/2);
        hancha_viewpager_img.setOffscreenPageLimit(5);

        blend_viewpager_img.setClipToPadding(false);
        blend_viewpager_img.setPadding(margin_img,0, margin_img, 0);
        blend_viewpager_img.setPageMargin(margin/2);
        blend_viewpager_img.setOffscreenPageLimit(5);

        total_viewpager_img.setClipToPadding(false);
        total_viewpager_img.setPadding(margin_img,0, margin_img, 0);
        total_viewpager_img.setPageMargin(margin/2);
        total_viewpager_img.setOffscreenPageLimit(5);


        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        Intent bindIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        tradition_viewpager_img.setVisibility(View.GONE);
        hancha_viewpager_img.setVisibility(View.GONE);
        blend_viewpager_img.setVisibility(View.GONE);
        total_value();

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                select_img.setVisibility(View.GONE);
                left_btn.setVisibility(View.VISIBLE);
                right_btn.setVisibility(View.VISIBLE);
                success = false;
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        total_viewpager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                total_viewpager.setCurrentItem(total_viewpager_img.getCurrentItem());
                total_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        tradition_viewpager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
               kind.setText("전통");
                name.setText(DeviceControlActivity.tradition_textList.get(i));
                tradition_viewpager.setCurrentItem(tradition_viewpager_img.getCurrentItem());
                tradition_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        hancha_viewpager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                kind.setText("한차");
                name.setText(DeviceControlActivity.hancha_textList.get(i));
                hancha_viewpager.setCurrentItem(hancha_viewpager_img.getCurrentItem());
                hancha_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        blend_viewpager_img.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                kind.setText("블렌드");
                name.setText(DeviceControlActivity.blend_textList.get(i));
                blend_viewpager.setCurrentItem(blend_viewpager_img.getCurrentItem());
                blend_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        total_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                total_viewpager_img.setCurrentItem(total_viewpager.getCurrentItem());
                total_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });



        tradition_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                kind.setText("전통");
                name.setText(DeviceControlActivity.tradition_textList.get(i));
                tradition_viewpager_img.setCurrentItem(tradition_viewpager.getCurrentItem());
                tradition_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        hancha_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                kind.setText("한차");
                name.setText(DeviceControlActivity.hancha_textList.get(i));
                hancha_viewpager_img.setCurrentItem(hancha_viewpager.getCurrentItem());
                hancha_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        blend_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                kind.setText("블렌드");
                name.setText(DeviceControlActivity.blend_textList.get(i));
                blend_viewpager_img.setCurrentItem(blend_viewpager.getCurrentItem());
                blend_value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public void search() {
        String search = search_edit.getText().toString();


        for (int i= 0; i < DeviceControlActivity.total_textList.size(); i++) {

            if (search.equals(DeviceControlActivity.total_textList.get(i))) {
                success = true;
                Glide.with(this).load(DeviceControlActivity.total_textList_img.get(i)).into(select_img);


                db.collection("BLE_APP")
                        .whereEqualTo("product_name", DeviceControlActivity.total_textList.get(i)) //파이어베이스 전통 종목 리스트에 추가
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        kind.setText(document.getData().get("product_kind").toString());
                                        name.setText(document.getData().get("product_name").toString());
                                        amount.setText(document.getData().get("amount").toString());
                                        origin.setText(document.getData().get("product_origin").toString());
                                    }
                                }
                            }
                        });
            }
        }



        if(success){
            total_viewpager.setVisibility(View.GONE);
            tradition_viewpager.setVisibility(View.GONE);
            hancha_viewpager.setVisibility(View.GONE);
            blend_viewpager.setVisibility(View.GONE);
            total_viewpager_img.setVisibility(View.GONE);
            tradition_viewpager_img.setVisibility(View.GONE);
            hancha_viewpager_img.setVisibility(View.GONE);
            blend_viewpager_img.setVisibility(View.GONE);
            left_btn.setVisibility(View.GONE);
            right_btn.setVisibility(View.GONE);
            select_img.setVisibility(View.VISIBLE);
            success = false;
        }else{
            Toast.makeText(product_amount.this, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show();
        }
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
        Button stop_btn = (Button)progressDialog.findViewById(R.id.stop_Btn);
        stop_btn.setOnClickListener(onClickListener);
    }

    public static void progressSET(String message) {
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
        progressON(this,name.getText().toString()+" 을(를)");
        abstraction_fragment.abstraction = true;
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            bluetoothLeService = ((BluetoothLeService.LocalBinder) rawBinder).getService();
            if (!bluetoothLeService.initialize()) {
            }
        }

        public void onServiceDisconnected(ComponentName classname) {
            if(bluetoothLeService != null) {
                bluetoothLeService.disconnect();
            }
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { //BroadcastReceiver는  연결상태와 데이터들을 받아오는 역할을 한다.
        @Override
        public void onReceive(final Context context, Intent intent) { //BluetoothLeService에서 sendBroadcast를 했을 때 호출.
            String action = intent.getAction(); //BluetoothLeService로부터 장치와 연결유뮤 상황을 action에 넣어 보내줌.
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //BLE장치에서 받은 데이터가 사용가능.
                final byte[] txValue = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            //string형식으로 리스트 뷰에 표현
                            text = new String(txValue, "UTF-8");
                            if (text.substring(1, 6).equals("07RST")) {
                                switch (text.substring(6, 8)) {
                                    case "20": //추출대기상태
                                        progressOFF();
                                        break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //연결 실패
                DeviceControlActivity.mConnected = false;
                Handler delayHandler2 = new Handler();
                delayHandler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!DeviceControlActivity.mConnected&& !product_amount.this.isFinishing()){
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(product_amount.this);
                            alert_confirm.setMessage("블루투스 환경이 원활하지 않습니다. \n머신상태를 확인해 주세요").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                    Intent intent = new Intent(getApplicationContext(), DeviceControlActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
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
                },5000);
            }
        }
    };

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.backclose:
                    finish();
                    break;

                case R.id.press_btn:
                    if(DeviceControlActivity.mConnected && !DeviceControlActivity.row_state){
                        if(Integer.parseInt(amount.getText().toString().substring(0,2))<30) {
                            String press_value = "04ET" + amount.getText().toString().substring(0, 2);
                            String press_amount = "04ET" + amount.getText().toString().substring(0,2) + stringToHex(press_value);
                            byte[] press_btn_value = {(byte) 0x02, (byte) 0x03 };
                            byte[] press_btn__temp = press_amount.getBytes();
                            byte[] press_btn_temp_data = new byte[press_btn__temp.length + 2];
                            System.arraycopy(press_btn_value, 0, press_btn_temp_data, 0, 1);
                            System.arraycopy(press_btn__temp, 0, press_btn_temp_data, 1, press_btn__temp.length);
                            System.arraycopy(press_btn_value, 1, press_btn_temp_data, press_btn__temp.length + 1, 1);
                            bluetoothLeService.writeRXCharacteristic(press_btn_temp_data);
                            startProgress();
                        }
                        else {
                            String press_value = "04ET0" + amount.getText().toString().substring(0,1);
                            String press_amount = "04ET0" + amount.getText().toString().substring(0,1) + stringToHex(press_value);
                            byte[] press_btn_value = {(byte) 0x02, (byte) 0x03 };
                            byte[] press_btn__temp = press_amount.getBytes();
                            byte[] press_btn_temp_data = new byte[press_btn__temp.length + 2];
                            System.arraycopy(press_btn_value, 0, press_btn_temp_data, 0, 1);
                            System.arraycopy(press_btn__temp, 0, press_btn_temp_data, 1, press_btn__temp.length);
                            System.arraycopy(press_btn_value, 1, press_btn_temp_data, press_btn__temp.length + 1, 1);
                            bluetoothLeService.writeRXCharacteristic(press_btn_temp_data);
                            startProgress();
                        }
                    } else if(DeviceControlActivity.row_state){
                        Toast.makeText(getApplicationContext(),"머신이 절전모드 상태 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"블루투스가 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.stop_Btn:
                    String stop_start = "01SB4";
                    byte[] stop_value = {(byte) 0x02, (byte) 0x03};
                    byte[] stop_temp = stop_start.getBytes();
                    byte[] stop_temp_data = new byte[stop_temp.length + 2];
                    System.arraycopy(stop_value, 0, stop_temp_data, 0, 1);
                    System.arraycopy(stop_temp, 0, stop_temp_data, 1, stop_temp.length);
                    System.arraycopy(stop_value, 1, stop_temp_data, stop_temp.length + 1, 1);
                    DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(stop_temp_data);
                    progressOFF();
                    break;

                case R.id.left_btn:
                    if(pos == 0){
                        total_viewpager.setCurrentItem(total_viewpager.getCurrentItem()-1);
                    }else if(pos == 1){
                        tradition_viewpager.setCurrentItem(tradition_viewpager.getCurrentItem()-1);
                    }else if(pos ==2){
                        hancha_viewpager.setCurrentItem(hancha_viewpager.getCurrentItem()-1);
                    }else if(pos ==3){
                        blend_viewpager.setCurrentItem(blend_viewpager.getCurrentItem()-1);
                    }
                    break;

                case R.id.right_btn:
                    if(pos == 0){
                        total_viewpager.setCurrentItem(total_viewpager.getCurrentItem()+1);
                    }else if(pos == 1){
                        tradition_viewpager.setCurrentItem(tradition_viewpager.getCurrentItem()+1);
                    }else if(pos ==2){
                        hancha_viewpager.setCurrentItem(hancha_viewpager.getCurrentItem()+1);
                    }else if(pos ==3){
                        blend_viewpager.setCurrentItem(blend_viewpager.getCurrentItem()+1);
                    }
                    break;

                case R.id.search_image:
                    search();
                    imm.hideSoftInputFromWindow(search_edit.getWindowToken(),0);
                    break;

                case R.id.plus_amount:
                    success = true;
                    String plus = amount.getText().toString().substring(0,2);
                    int plus_int = Integer.parseInt(plus);
                    if(plus_int >= 30)
                        plus = amount.getText().toString().substring(0,1);
                    if(Integer.parseInt(plus) >= 3 && Integer.parseInt(plus) < 25)
                        amount.setText(amount.getText().toString().replace(plus, String.valueOf(Integer.parseInt(plus)+ 1)));
                    else Toast.makeText(getApplicationContext(), "30ml에서 250ml만 추출 가능합니다.", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.minus_amount:
                    success = true;
                    String minus = amount.getText().toString().substring(0,2);
                    int minus_int = Integer.parseInt(minus);
                    if(minus_int > 30) minus = amount.getText().toString().substring(0,1);
                    if(Integer.parseInt(minus) > 3 && Integer.parseInt(minus) <= 25)
                        amount.setText(amount.getText().toString().replace(minus, String.valueOf(Integer.parseInt(minus)- 1)));
                    else Toast.makeText(getApplicationContext(), "30ml에서 250ml만 추출 가능합니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static String stringToHex(String s){
        int ch2 = 0;
        for (int i = 0; i < s.length(); i++){
            byte ch=(byte)s.charAt(i);
            ch2+=(byte)ch;
        }
        String s5 = Integer.toHexString(ch2);
        return s5.substring(s5.length()-2, s5.length());
    }

    public void total_value(){
        if(DeviceControlActivity.total_textList.size()!=0){
            db.collection("BLE_APP")
                    .whereEqualTo("product_name", DeviceControlActivity.total_textList.get(total_viewpager.getCurrentItem())) //파이어베이스 전통 종목 리스트에 추가
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    kind.setText(document.getData().get("product_kind").toString());
                                    name.setText(document.getData().get("product_name").toString());
                                    amount.setText(document.getData().get("amount").toString());
                                    origin.setText(document.getData().get("product_origin").toString());
                                }
                            }
                        }
                    });
        }
    }



    public void tradition_value(){
        if(DeviceControlActivity.tradition_textList.size()!=0){
            db.collection("BLE_APP")
                    .whereEqualTo("product_name", DeviceControlActivity.tradition_textList.get(tradition_viewpager.getCurrentItem())) //파이어베이스 전통 종목 리스트에 추가
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    amount.setText(document.getData().get("amount").toString());
                                    origin.setText(document.getData().get("product_origin").toString());
                                }
                            }
                        }
                    });
        }
    }


    public void hancha_value(){
        if(DeviceControlActivity.hancha_textList.size() !=0){
            db.collection("BLE_APP")
                    .whereEqualTo("product_name", DeviceControlActivity.hancha_textList.get(hancha_viewpager.getCurrentItem())) //파이어베이스 전통 종목 리스트에 추가
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    amount.setText(document.getData().get("amount").toString());
                                    origin.setText(document.getData().get("product_origin").toString());
                                }
                            }
                        }
                    });
        }
    }

    public void blend_value(){
        if(DeviceControlActivity.blend_textList.size() != 0){
            db.collection("BLE_APP")
                    .whereEqualTo("product_name", DeviceControlActivity.blend_textList.get(blend_viewpager.getCurrentItem())) //파이어베이스 전통 종목 리스트에 추가
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    amount.setText(document.getData().get("amount").toString());
                                    origin.setText(document.getData().get("product_origin").toString());
                                }
                            }
                        }
                    });
        }
    }

    private void changeView(int index){
        switch (index) {
            case 0 :
                total_viewpager.setVisibility(View.VISIBLE);
                tradition_viewpager.setVisibility(View.GONE);
                hancha_viewpager.setVisibility(View.GONE);
                blend_viewpager.setVisibility(View.GONE);
                total_viewpager_img.setVisibility(View.VISIBLE);
                tradition_viewpager_img.setVisibility(View.GONE);
                hancha_viewpager_img.setVisibility(View.GONE);
                blend_viewpager_img.setVisibility(View.GONE);
                total_value();
                break;

            case 1 :
                total_viewpager.setVisibility(View.GONE);
                tradition_viewpager.setVisibility(View.VISIBLE);
                hancha_viewpager.setVisibility(View.GONE);
                blend_viewpager.setVisibility(View.GONE);
                total_viewpager_img.setVisibility(View.GONE);
                tradition_viewpager_img.setVisibility(View.VISIBLE);
                hancha_viewpager_img.setVisibility(View.GONE);
                blend_viewpager_img.setVisibility(View.GONE);
                kind.setText("전통");
                name.setText(DeviceControlActivity.tradition_textList.get(tradition_viewpager.getCurrentItem()));
                tradition_value();

                break;

            case 2:
                total_viewpager.setVisibility(View.GONE);
                tradition_viewpager.setVisibility(View.GONE);
                hancha_viewpager.setVisibility(View.VISIBLE);
                blend_viewpager.setVisibility(View.GONE);
                total_viewpager_img.setVisibility(View.GONE);
                tradition_viewpager_img.setVisibility(View.GONE);
                hancha_viewpager_img.setVisibility(View.VISIBLE);
                blend_viewpager_img.setVisibility(View.GONE);
                kind.setText("한차");
                name.setText(DeviceControlActivity.hancha_textList.get(hancha_viewpager.getCurrentItem()));
                hancha_value();

                break;

            case 3:
                total_viewpager.setVisibility(View.GONE);
                tradition_viewpager.setVisibility(View.GONE);
                hancha_viewpager.setVisibility(View.GONE);
                blend_viewpager.setVisibility(View.VISIBLE);
                total_viewpager_img.setVisibility(View.GONE);
                tradition_viewpager_img.setVisibility(View.GONE);
                hancha_viewpager_img.setVisibility(View.GONE);
                blend_viewpager_img.setVisibility(View.VISIBLE);
                kind.setText("블렌드");
                name.setText(DeviceControlActivity.blend_textList.get(blend_viewpager.getCurrentItem()));
                blend_value();

                break;
        }
    }


    @Override
    public void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mGattUpdateReceiver, intentFilter); //브로드캐스트 등록
    }

    @Override
    protected void onPause() { //리시버를 해제
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);
    }
}