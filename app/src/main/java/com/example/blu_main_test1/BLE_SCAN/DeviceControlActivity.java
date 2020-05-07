package com.example.blu_main_test1.BLE_SCAN;

/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_button.abstraction;
import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;

import com.example.blu_main_test1.MypageActivity;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.main_before.Machine_main;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.  The Activity
 * communicates with {@code BluetoothLeService}, which in turn interacts with the
 * Bluetooth LE API.
 */
public class DeviceControlActivity extends Activity {
    private final static String TAG = DeviceControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";


    private ImageButton back;
    private TextView coffee_b_amount,coffee_s_amount,tea_b_amount,tea_s_amount;
    private LinearLayout background;
    private FrameLayout sub_background;
    public Button sub_amount;
    private TextView versionView;
    private TextView stateView;
    private TextView temperView;
    private TextView mConnectionState;
    private Timer mTimer;

    private Boolean inflateView = false;
    private String text;
    private String mDeviceName;
    private String mDeviceAddress;
    //private ExpandableListView mGattServicesList;
    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics =
            new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    public static boolean mConnected = false;
    private BluetoothGattCharacteristic mNotifyCharacteristic;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private LinearLayout linear;
    private ProgressBar pgb,pgb2;
    private int coffee_big_number,coffee_small_number,tea_big_number,tea_small_number;
    private SeekBar sb_c_b,sb_c_s,sb_t_b,sb_t_s;

    // Code to manage Service lifecycle.
    //서비스가 연결됐을 때, 안됐을 때 관리
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) { //연결이 되었다면
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService(); //또 다른 파일인 BluetoothLeService 클래스로 만들어진 변수 mBluetoothLeservice에
            if (!mBluetoothLeService.initialize()) {                                        //BluetoothLeService객체를 받아오게 하는 getSerVice()로 초기화 시킴.
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(mDeviceAddress); //장치를 연결시킴 connect함수는 BluetoothLeService에 구현되어있음.

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { //BroadcastReceiver는  연결상태와 데이터들을 받아오는 역할을 한다.
        @Override
        public void onReceive(final Context context, Intent intent) { //BluetoothLeService에서 sendBroadcast를 했을 때 호출.
            String action = intent.getAction(); //BluetoothLeService로부터 장치와 연결유뮤 상황을 action에 넣어 보내줌.
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) { //연결 성공
                mConnected = true;
                updateConnectionState(R.string.connected);//연결됨을 ui에서 표시
                invalidateOptionsMenu(); //onCreateOptionsMenu 호출
                if(mConnected){
                    mTimer.schedule(new State(), 2000, 10000);

                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Version();
                        }
                    },2500);

                }

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) { //연결 실패
                mConnected = false;
                Handler delayHandler2 = new Handler();
                delayHandler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(!mConnected){
                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DeviceControlActivity.this);
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


            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) { //GATT 서비스 발견
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                mBluetoothLeService.enableTXNotification();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //BLE장치에서 받은 데이터가 사용가능.
                final byte[] txValue = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                runOnUiThread(new Runnable() {
                    public void run() {
                        try {
                            //string형식으로 리스트 뷰에 표현
                            text = new String(txValue, "UTF-8");
                            if (text.substring(1, 6).equals("07RST")) {
                                switch (text.substring(6, 8)) {
                                    case "00":
                                        stateView.setText("절전모드");
                                        break;
                                    case "10":
                                        stateView.setText("가열중");
                                        break;
                                    case "20":
                                        stateView.setText("추출대기");
                                        break;
                                    case "91":
                                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DeviceControlActivity.this);
                                        alert_confirm.setMessage("머신 내부 온도가 너무 높습니다. 절전모드 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String start = "01PB1";
                                                byte [] value={(byte)0x02,(byte)0x03};
                                                byte[] temp=start.getBytes();
                                                byte[] temp_data= new byte[temp.length+2];
                                                System.arraycopy(value,0,temp_data,0,1);
                                                System.arraycopy(temp,0,temp_data,1,temp.length);
                                                System.arraycopy(value,1,temp_data,temp.length+1,1);
                                                mBluetoothLeService.writeRXCharacteristic(temp_data);
                                                startToast("절전모드");
                                            }
                                        });
                                        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        alert_confirm.show();
                                        break;
                                    case "92":
                                        AlertDialog.Builder alert_confirm2 = new AlertDialog.Builder(DeviceControlActivity.this);
                                        alert_confirm2.setMessage("온도퓨즈가 단선되었습니다. 고객센터로 문의주세요. ").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String start = "01PB1";
                                                byte [] value={(byte)0x02,(byte)0x03};
                                                byte[] temp=start.getBytes();
                                                byte[] temp_data= new byte[temp.length+2];
                                                System.arraycopy(value,0,temp_data,0,1);
                                                System.arraycopy(temp,0,temp_data,1,temp.length);
                                                System.arraycopy(value,1,temp_data,temp.length+1,1);
                                                mBluetoothLeService.writeRXCharacteristic(temp_data);
                                                startToast("절전모드");
                                            }
                                        });
                                        alert_confirm2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                        alert_confirm2.show();
                                        break;
                                    case "93":
                                        AlertDialog.Builder alert_confirm3 = new AlertDialog.Builder(DeviceControlActivity.this);
                                        alert_confirm3.setMessage("물 보충이 필요합니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        alert_confirm3.show();
                                        break;

                                }
                                temperView.setText(text.substring(8,10));
                                pgb2.setVisibility(View.GONE);
                            } else if(text.substring(1,6).equals("05RCL")){
                                coffee_b_amount.setText(Integer.toString(Integer.parseInt(text.substring(6,8))*10)); //ml은 안뜸, 페이지 나갔다 다시 들어오면 유지 x
                                sb_c_b.setProgress((Integer.parseInt(text.substring(6,8))));


                            }
                            else if(text.substring(1,6).equals("05RCS")){
                                coffee_s_amount.setText(Integer.toString(Integer.parseInt(text.substring(6,8))*10));
                                sb_c_s.setProgress((Integer.parseInt(text.substring(6,8))));

                            }
                            else if(text.substring(1,6).equals("05RTL")){
                                tea_b_amount.setText(Integer.toString(Integer.parseInt(text.substring(6,8))*10));
                                sb_t_b.setProgress((Integer.parseInt(text.substring(6,8))));

                            }
                            else if(text.substring(1,6).equals("05RTS")){
                                tea_s_amount.setText(Integer.toString(Integer.parseInt(text.substring(6,8))*10));
                                sb_t_s.setProgress((Integer.parseInt(text.substring(6,8))));
                                pgb.setVisibility(View.GONE);
                            } else if(text.substring(1,6).equals("0FRVE")){
                                versionView.setText(text.substring(6,18));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    };

    // If a given GATT characteristic is selected, check for supported features.  This sample
    // demonstrates 'Read' and 'Notify' features.  See
    // http://d.android.com/reference/android/bluetooth/BluetoothGatt.html for the complete
    // list of supported characteristic features.
    private final ExpandableListView.OnChildClickListener servicesListClickListner =
            new ExpandableListView.OnChildClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                            int childPosition, long id) {
                    if (mGattCharacteristics != null) {
                        final BluetoothGattCharacteristic characteristic =
                                mGattCharacteristics.get(groupPosition).get(childPosition);
                        final int charaProp = characteristic.getProperties();
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                            // If there is an active notification on a characteristic, clear
                            // it first so it doesn't update the data field on the user interface.
                            if (mNotifyCharacteristic != null) {
                                mBluetoothLeService.setCharacteristicNotification(
                                        mNotifyCharacteristic, false);
                                mNotifyCharacteristic = null;
                            }
                            mBluetoothLeService.readCharacteristic(characteristic);
                        }
                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            mNotifyCharacteristic = characteristic;
                            mBluetoothLeService.setCharacteristicNotification(
                                    characteristic, true);
                        }
                        return true;
                    }
                    return false;
                }
            };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gatt_services_characteristics);

        final Intent intent = getIntent();
        // mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME); //DeviceScanActivity에서 인텐트로 같이 넘어온 장치 이름과 주소를 추출.
        // mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        SharedPreferences autodevice = getSharedPreferences("autodevice", Activity.MODE_PRIVATE);
        mDeviceAddress = autodevice.getString("address",null);
        mDeviceName = autodevice.getString("devicename",null);

        // Sets up UI references.
        //((TextView) findViewById(R.id.device_address)).setText(mDeviceAddress);
        mConnectionState = (TextView) findViewById(R.id.connection_state);
        stateView = (TextView) findViewById(R.id.state);
        temperView = (TextView) findViewById(R.id.temper);
        versionView = (TextView)findViewById(R.id.draw_version);
        pgb2 = (ProgressBar)findViewById(R.id.progressBar4);
        pgb2.setVisibility(View.VISIBLE);

        Handler delayHandler = new Handler();

        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(stateView.getText().toString().length() == 0 && !DeviceControlActivity.this.isFinishing()){
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DeviceControlActivity.this);
                    alert_confirm.setMessage("주변에 머신을 찾을 수 없습니다. 재연결 하시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences autodevice = getSharedPreferences("autodevice",Activity.MODE_PRIVATE);
                            SharedPreferences.Editor autoconnect = autodevice.edit();
                            autoconnect.clear();
                            autoconnect.commit();
                            Intent intent = new Intent(getApplicationContext(), Machine_main.class);
                            startActivity(intent);
                            finish();

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
        },15000);




        getActionBar().setTitle(mDeviceName);
        getActionBar().setDisplayHomeAsUpEnabled(true); //액션바의 앱 아이콘 옆에 화살표를 만들어 전의 액티비티로 돌아갈 수 있게 함.
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class); //서비스와 특성들을 불러오고 특성을 눌렀을때 mDataField에 데이터를 불러 오도록하기 위해
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE); //인텐트를 만들고 그것으로 서비스를 실행시킴.

        findViewById(R.id.product_amount).setOnClickListener(onClickListener);
        findViewById(R.id.state_start).setOnClickListener(onClickListener);
        findViewById(R.id.low_start).setOnClickListener(onClickListener);
        findViewById(R.id.amount_start).setOnClickListener(onClickListener);
        findViewById(R.id.amount_change).setOnClickListener(onClickListener);
        findViewById(R.id.amount_stop).setOnClickListener(onClickListener);


        mTimer = new Timer();


        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress); // 연결
            Log.d(TAG, "Connect request result=" + result);
        }



    }  //서비스를 실행시키고 요청을 하게 되면, 요청에 대한 결과를 mServiceConnection함수에서 받아와 활용할 수 있음. 세번째 인자는 바인딩의 옵션을 설정하는 flags를 설정.

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.amount_start:
                    Intent intent=new Intent(getApplicationContext(), abstraction.class);
                    startActivity(intent);
                    break;

                case R.id.product_amount:
                    Intent intent_product=new Intent(getApplicationContext(), com.example.blu_main_test1.Main_page.Main_page2.product_amount.class);
                    startActivity(intent_product);
                    break;

                case R.id.state_start:
                    if(mConnected) {
                        String start = "01WB8";
                        byte[] value = {(byte) 0x02, (byte) 0x03};
                        byte[] temp = start.getBytes();
                        byte[] temp_data = new byte[temp.length + 2];
                        System.arraycopy(value, 0, temp_data, 0, 1);
                        System.arraycopy(temp, 0, temp_data, 1, temp.length);
                        System.arraycopy(value, 1, temp_data, temp.length + 1, 1);
                        mBluetoothLeService.writeRXCharacteristic(temp_data);
                        startToast("머신 예열");
                    }else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }
                    break;

                case R.id.low_start:
                    if(mConnected) {
                        String low_start = "01PB1";
                        byte[] low_value = {(byte) 0x02, (byte) 0x03};
                        byte[] low_temp = low_start.getBytes();
                        byte[] low_temp_data = new byte[low_temp.length + 2];
                        System.arraycopy(low_value, 0, low_temp_data, 0, 1);
                        System.arraycopy(low_temp, 0, low_temp_data, 1, low_temp.length);
                        System.arraycopy(low_value, 1, low_temp_data, low_temp.length + 1, 1);
                        mBluetoothLeService.writeRXCharacteristic(low_temp_data);
                        startToast("절전 모드");
                    }else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }
                    break;

                case R.id.amount_stop:
                    if(mConnected) {
                        String stop_start = "01SB4";
                        byte[] stop_value = {(byte) 0x02, (byte) 0x03};
                        byte[] stop_temp = stop_start.getBytes();
                        byte[] stop_temp_data = new byte[stop_temp.length + 2];
                        System.arraycopy(stop_value, 0, stop_temp_data, 0, 1);
                        System.arraycopy(stop_temp, 0, stop_temp_data, 1, stop_temp.length);
                        System.arraycopy(stop_value, 1, stop_temp_data, stop_temp.length + 1, 1);
                        mBluetoothLeService.writeRXCharacteristic(stop_temp_data);
                        startToast("추출 중지");
                    }else{
                        startToast("블루투스가 연결 되어 있지 않습니다.");
                    }
                    break;

                case R.id.amount_change:
                    inflateView = true;
                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    linear = (LinearLayout) inflater.inflate(R.layout.activity_amount_change, null);


                    Coffee_large();

                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Coffee_small();
                        }
                    },400);
                    Handler delayHandler2 = new Handler();
                    delayHandler2.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Tea_large();
                        }
                    },800);
                    Handler delayHandler3 = new Handler();
                    delayHandler3.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Tea_small();
                        }
                    },1200);



                    LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(

                            LinearLayout.LayoutParams.MATCH_PARENT,

                            LinearLayout.LayoutParams.MATCH_PARENT
                    );

                    back=(ImageButton)linear.findViewById(R.id.back);
                    background=(LinearLayout)linear.findViewById(R.id.background);
                    sub_background=(FrameLayout)linear.findViewById(R.id.sub_background);
                    coffee_b_amount=(TextView) linear.findViewById(R.id.coffee_b_amount);
                    coffee_s_amount=(TextView)linear.findViewById(R.id.coffee_s_amount);
                    tea_b_amount=(TextView)linear.findViewById(R.id.tea_b_amount);
                    tea_s_amount=(TextView)linear.findViewById(R.id.tea_s_amount);
                    sub_amount=(Button)linear.findViewById(R.id.sub_amount);
                    pgb = (ProgressBar)linear.findViewById(R.id.progressBar3);
                    pgb.setVisibility(View.VISIBLE);

                    sb_c_b = (SeekBar)linear.findViewById(R.id.seek_coffee_big);
                    sb_c_s = (SeekBar)linear.findViewById(R.id.seek_coffee_small);
                    sb_t_b = (SeekBar)linear.findViewById(R.id.seek_Tea_big);
                    sb_t_s = (SeekBar)linear.findViewById(R.id.seek_Tea_small);


                    sb_c_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //롱고 시크바

                        public void onStopTrackingTouch(SeekBar seekBar) {
                            coffee_big_number = seekBar.getProgress();
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                            coffee_big_number = seekBar.getProgress();
                        }

                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {
                            if(progress >= 10){
                                coffee_b_amount.setText(""+(progress*10));
                            }else{
                                coffee_b_amount.setText("0"+(progress*10));
                                if(progress <= 3){
                                    progress = 3;
                                    coffee_b_amount.setText("0"+(progress*10));

                                }
                            }

                        }
                    });

                    sb_c_s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //에스프레소 시크바
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            coffee_small_number = seekBar.getProgress();
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                            coffee_small_number = seekBar.getProgress();
                        }


                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {
                            if(progress >= 10){
                                coffee_s_amount.setText(""+(progress*10));
                            }else{
                                coffee_s_amount.setText("0"+(progress*10));
                                if(progress <= 3){
                                    progress = 3;
                                    coffee_s_amount.setText("0"+(progress*10));

                                }
                            }

                        }
                    });

                    sb_t_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //티대 시크바
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            tea_big_number = seekBar.getProgress();
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                            tea_big_number = seekBar.getProgress();
                        }


                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {
                            if(progress >= 10){
                                tea_b_amount.setText(""+(progress*10));
                            }else{
                                tea_b_amount.setText("0"+(progress*10));
                                if(progress <= 3){
                                    progress = 3;
                                    tea_b_amount.setText("0"+(progress*10));

                                }
                            }

                        }
                    });

                    sb_t_s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //티소 시크바
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            tea_small_number = seekBar.getProgress();
                        }

                        public void onStartTrackingTouch(SeekBar seekBar) {
                            tea_small_number = seekBar.getProgress();
                        }


                        public void onProgressChanged(SeekBar seekBar, int progress,
                                                      boolean fromUser) {
                            if(progress >= 10){
                                tea_s_amount.setText(""+(progress*10));
                            }else{
                                tea_s_amount.setText("0"+(progress*10));
                                if(progress <= 3){
                                    progress = 3;
                                    tea_s_amount.setText("0"+(progress*10));

                                }
                            }

                        }
                    });






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
                            ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                            parentViewGroup.removeView(linear);

                        }
                    });


                    //바탕부분 클릭시
                    background.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                            parentViewGroup.removeView(linear);


                        }
                    });
                    //뒤로가기 이미지
                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                            parentViewGroup.removeView(linear);

                        }
                    });


                    sub_amount.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                AlertDialog.Builder alert_confirm2 = new AlertDialog.Builder(DeviceControlActivity.this);
                                alert_confirm2.setMessage("추출량을 변경 하시겠습니까?  ").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (mConnected) {
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
                                                mBluetoothLeService.writeRXCharacteristic(cb_temp_data);
                                                pgb2.setVisibility(View.VISIBLE);

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

                                                        mBluetoothLeService.writeRXCharacteristic(cs_temp_data);
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
                                                        mBluetoothLeService.writeRXCharacteristic(tb_temp_data);
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
                                                        mBluetoothLeService.writeRXCharacteristic(ts_temp_data);
                                                        pgb2.setVisibility(View.GONE);
                                                        startToast("추출량이 변경 되었습니다. ");
                                                    }

                                                }, 2000);  // 1 초 후에 실행
                                            }

                                            ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
                                            parentViewGroup.removeView(linear);
                                        }else{
                                            startToast("블루투스를 재연결해 주세요");
                                        }
                                    }
                                });

                                alert_confirm2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                });
                                alert_confirm2.show();

                            }catch (Exception e)
                            {

                                Toast.makeText(getApplicationContext(),"블루투스를 재연결해 주세요",Toast.LENGTH_SHORT).show();

                            }
                        }

                    });

                    break;


            }
        }
    };

    public static String stringToHex(String s) {
        int ch2=0;
        for (int i = 0; i < s.length(); i++) {
            byte ch=(byte)s.charAt( i );
            ch2+=(byte)ch;
        }
        String s5=Integer.toHexString(ch2);
        return s5.substring(s5.length()-2,s5.length());
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter()); //브로드캐스트 등록

    }

    @Override
    protected void onPause() { //리시버를 해제
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGattUpdateReceiver);

    }

    @Override
    protected void onDestroy() { //서비스를 해제
        super.onDestroy();

        mTimer.cancel();

        unbindService(mServiceConnection); //unbindService()를 호출하면 연결이 끊기고 서비스에 연결된 컴포넌트가 하나도 남지 않게 되면서 안드로이드 시스템이 서비스를 소멸.
        mBluetoothLeService = null;
        mConnected = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(DeviceControlActivity.this);
                alert_confirm.setMessage("머신과의 연결을 끊으시겠습니까?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        mBluetoothLeService.disconnect();
                        SharedPreferences autodevice = getSharedPreferences("autodevice",Activity.MODE_PRIVATE);
                        SharedPreferences.Editor autoconnect = autodevice.edit();
                        autoconnect.clear();
                        autoconnect.commit();
                        Intent intent = new Intent(getApplicationContext(), Machine_main.class);
                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert_confirm.show();

                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConnectionState.setText(resourceId);
            }
        });
    }



    // Demonstrates how to iterate through the supported GATT Services/Characteristics.
    // In this sample, we populate the data structure that is bound to the ExpandableListView
    // on the UI.
    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData
                = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(
                    LIST_NAME, SampleGattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData =
                    new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics =
                    gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas =
                    new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(
                        LIST_NAME, SampleGattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            mGattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }


    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }



    class State extends TimerTask {
        @Override
        public void run() {
            if(mConnected) {
                if (mBluetoothLeService != null) {

                    String TL_amount, basic_state;
                    byte[] value = {(byte) 0x02, (byte) 0x03};

                    basic_state = "03QST5B"; //현재 상태질의

                    byte[] state = basic_state.getBytes();
                    byte[] state_data = new byte[state.length + 2];

                    System.arraycopy(value, 0, state_data, 0, 1);
                    System.arraycopy(state, 0, state_data, 1, state.length);
                    System.arraycopy(value, 1, state_data, state.length + 1, 1);
                    mBluetoothLeService.writeRXCharacteristic(state_data);
                }
            }
        }
    }





    public void Tea_large()  {

        if(mConnected) {
            if (mBluetoothLeService != null) {

                String TL_amount, basic_state;
                byte[] value = {(byte) 0x02, (byte) 0x03};
                TL_amount = "03QTL54";  //현재

                byte[] temp = TL_amount.getBytes();
                byte[] temp_data = new byte[temp.length + 2];

                System.arraycopy(value, 0, temp_data, 0, 1);
                System.arraycopy(temp, 0, temp_data, 1, temp.length);
                System.arraycopy(value, 1, temp_data, temp.length + 1, 1);
                mBluetoothLeService.writeRXCharacteristic(temp_data);

            }
        }
    }


    public void Tea_small(){
        if(mConnected) {
            if (mBluetoothLeService != null) {
                String TL_amount, basic_state;
                byte[] value = {(byte) 0x02, (byte) 0x03};

                basic_state = "03QTS5B"; //현재 상태질의

                byte[] state = basic_state.getBytes();
                byte[] state_data = new byte[state.length + 2];

                System.arraycopy(value, 0, state_data, 0, 1);
                System.arraycopy(state, 0, state_data, 1, state.length);
                System.arraycopy(value, 1, state_data, state.length + 1, 1);
                mBluetoothLeService.writeRXCharacteristic(state_data);
            }
        }
    }
    public void Coffee_large() {

        if(mConnected) {
            if (mBluetoothLeService != null) {

                String TL_amount, basic_state;
                byte[] value = {(byte) 0x02, (byte) 0x03};

                basic_state = "03QCL43"; //현재 상태질의

                byte[] state = basic_state.getBytes();
                byte[] state_data = new byte[state.length + 2];

                System.arraycopy(value, 0, state_data, 0, 1);
                System.arraycopy(state, 0, state_data, 1, state.length);
                System.arraycopy(value, 1, state_data, state.length + 1, 1);
                mBluetoothLeService.writeRXCharacteristic(state_data);
            }
        }
    }

    public void Coffee_small() {

        if(mConnected) {
            if (mBluetoothLeService != null) {

                String TL_amount, basic_state;
                byte[] value = {(byte) 0x02, (byte) 0x03};

                basic_state = "03QCS4A"; //현재 상태질의

                byte[] state = basic_state.getBytes();
                byte[] state_data = new byte[state.length + 2];

                System.arraycopy(value, 0, state_data, 0, 1);
                System.arraycopy(state, 0, state_data, 1, state.length);
                System.arraycopy(value, 1, state_data, state.length + 1, 1);
                mBluetoothLeService.writeRXCharacteristic(state_data);
            }
        }
    }


    public void  Version(){
        if(mConnected) {
            if (mBluetoothLeService != null) {

                String TL_amount, basic_state;
                byte[] value = {(byte) 0x02, (byte) 0x03};
                basic_state = "03QVE4F"; //현재 상태질의
                byte[] state = basic_state.getBytes();
                byte[] state_data = new byte[state.length + 2];
                System.arraycopy(value, 0, state_data, 0, 1);
                System.arraycopy(state, 0, state_data, 1, state.length);
                System.arraycopy(value, 1, state_data, state.length + 1, 1);
                mBluetoothLeService.writeRXCharacteristic(state_data);
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(inflateView){ //추출량 변화 view 에서 뒤로가기 버튼을 눌렀을 경우 액티비티가 종료되지 않고 뷰만 종료되도록
            ViewGroup parentViewGroup = (ViewGroup) linear.getParent();
            parentViewGroup.removeView(linear);
            inflateView = false;
        }else{
            super.onBackPressed();
        }
    }


}