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

import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_page2.Main_page_2;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.BackPressHandler;

public class theMainPage extends AppCompatActivity {
    Button to_BLE, to_shop;
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
        setContentView(R.layout.activity_the_main_page);

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
                    Intent intent = new Intent(getApplicationContext(), Machine_main.class);
                    startActivity(intent);
                    break;
                case R.id.to_shop:
                    Intent intent2 = new Intent(getApplication(), Main_view_pager.class);
                    startActivity(intent2);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed(){
        backPressHandler.onBackPressed();
    }
}