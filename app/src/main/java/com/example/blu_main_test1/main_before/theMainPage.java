package com.example.blu_main_test1.main_before;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.CustomDialog;
import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_page2.Main_page_2;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.BackPressHandler;
import com.example.blu_main_test1.serial;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class theMainPage extends AppCompatActivity {
    Button to_BLE, to_shop;
    private BackPressHandler backPressHandler;
    private String mDeviceName;
    private String mDeviceAddress;
    public static String serial_number_get;
    public static  ImageView coupon;
    private String key;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        to_BLE = (Button) findViewById(R.id.to_BLE);
        to_shop = (Button) findViewById(R.id.to_shop);
        coupon = (ImageView)findViewById(R.id.coupon);
        coupon.setOnClickListener(btnListener);
        to_BLE.setOnClickListener(btnListener);
        to_shop.setOnClickListener(btnListener);

        backPressHandler = new BackPressHandler(this);

        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        to_BLE.setVisibility(View.VISIBLE);
        to_shop.setVisibility(View.VISIBLE);

        prefs=getSharedPreferences("isFirstserial", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstserial",true);
        if(isFirstRun){
            coupon();
            CustomDialog customDialog = new CustomDialog(theMainPage.this);
            customDialog.callFunction();
            prefs.edit().putBoolean("isFirstserial",false).apply();
        }


    }


    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view){
            switch(view.getId()) {
                case R.id.to_BLE:
                    SharedPreferences autodevice = getSharedPreferences("autodevice", Activity.MODE_PRIVATE);
                    mDeviceAddress = autodevice.getString("address",null);
                    mDeviceName = autodevice.getString("devicename",null);

                    if( mDeviceAddress != null && mDeviceName != null ){
                        Intent intent1 = new Intent(getApplicationContext(), DeviceControlActivity.class);
                        startActivity(intent1);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Machine_main.class);
                        startActivity(intent);
                    }
                    break;
                case R.id.to_shop:
                    Intent shop = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.m.medipresso.com/product/list_thumb.html?cate_no=46"));
                    startActivity(shop);
                    break;
                case R.id.coupon:
                    coupon();
                    CustomDialog customDialog = new CustomDialog(theMainPage.this);
                    customDialog.callFunction();
                    break;

            }
        }
    };

    public void coupon(){
        SharedPreferences serialdevice = getSharedPreferences("serial_num", Activity.MODE_PRIVATE);
        serial_number_get= serialdevice.getString("serial",null);


    }

    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed();
    }
}
