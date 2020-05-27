package com.example.blu_main_test1.main_before;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class theMainPage extends AppCompatActivity {
    Button to_BLE, to_shop;
    private BackPressHandler backPressHandler;
    private String mDeviceName;
    private String mDeviceAddress;
    private String serial_number;
    private String serial_using;
    private ImageView coupon;


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

        SharedPreferences prefs=getSharedPreferences("isFirstserial", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstserial",true);

        if(isFirstRun){

            while(true){
                String num = Integer.toString((int)(Math.random()*5000)+1);
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("serial_number")
                        .whereEqualTo("num", num)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        serial_number = document.getData().get("serial").toString();
                                        serial_using = document.getData().get("using").toString();
                                    }
                                }
                            }
                        });
                if(true){ //seria_using.equals("true")
                    SharedPreferences serialdevice = getSharedPreferences("serial_num", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor serialconnect = serialdevice.edit();
                    serialconnect.putString("serial","serial_number");
                    serialconnect.commit();
                    break;
                }
            }
            coupon();
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
                    break;

            }
        }
    };

    public void coupon(){
        SharedPreferences serialdevice = getSharedPreferences("serial_num", Activity.MODE_PRIVATE);
        serial_number= serialdevice.getString("serial",null);
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(theMainPage.this);
        alert_confirm.setMessage(serial_number).setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert_confirm.show();

    }

    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed();
    }
}
