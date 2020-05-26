package com.example.blu_main_test1.main_before;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_page2.Main_page_2;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.BackPressHandler;

public class theMainPage extends AppCompatActivity {
    Button to_BLE, to_shop;
    private BackPressHandler backPressHandler;
    private String mDeviceName;
    private String mDeviceAddress;

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
            }
        }
    };

    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed();
    }
}
