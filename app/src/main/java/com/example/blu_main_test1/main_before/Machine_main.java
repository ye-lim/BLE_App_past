package com.example.blu_main_test1.main_before;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.blu_main_test1.BLE_SCAN.DeviceScanActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.BackPressHandler;

public class Machine_main extends AppCompatActivity {
    Button BLE_Btn, login_Btn;
    private BackPressHandler backPressHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.   FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        BLE_Btn = (Button) findViewById(R.id.BLE_Btn);
        login_Btn = (Button) findViewById(R.id.login_Btn);

        BLE_Btn.setOnClickListener(btnListener);
        login_Btn.setOnClickListener(btnListener);

        backPressHandler = new BackPressHandler(this);

        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        BLE_Btn.setVisibility(View.VISIBLE);
        login_Btn.setVisibility(View.VISIBLE);
    }


    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view){
            switch(view.getId()) {
                case R.id.BLE_Btn:
                    Intent intent = new Intent(getApplicationContext(), DeviceScanActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.login_Btn:
                    Intent intent2 = new Intent(getApplication(), login.class);
                    startActivity(intent2);
                    break;
            }
        }
    };

}
