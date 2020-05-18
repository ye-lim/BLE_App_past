package com.example.blu_main_test1.main_before;

import android.bluetooth.BluetoothAdapter;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.blu_main_test1.BLE_SCAN.BottomSheet_scan;
import com.example.blu_main_test1.BLE_SCAN.DeviceScanActivity;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.BackPressHandler;
import com.bumptech.glide.Glide;

public class Machine_main extends AppCompatActivity {
    Button BLE_Btn;
    ImageButton btn_back;
    TextView mTextView;
    private BluetoothAdapter mBluetoothAdapter; //bluetooth_finding 어댑터
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
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        BLE_Btn.setOnClickListener(btnListener);
        btn_back.setOnClickListener(btnListener);

        backPressHandler = new BackPressHandler(this);

        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        BLE_Btn.setVisibility(View.VISIBLE);

        ImageView iv_frame_ble_start = (ImageView)findViewById(R.id.iv_frame_ble);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(iv_frame_ble_start);
        Glide.with(this).load(R.raw.ble_start).into(gifImage);
    }



    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view){
            switch(view.getId()) {
                case R.id.BLE_Btn:
                    //BottomSheet_scan bottomSheet = new BottomSheet_scan();
                    //bottomSheet.show(getSupportFragmentManager(), "BottomSheet_Scan");
                    Intent intent = new Intent(getApplicationContext(), DeviceScanActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.btn_back:
                    Intent intent2 = new Intent(getApplicationContext(), theMainPage.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };
    public void onOptionClick(String text) {
        //change text on click
        mTextView.setText(text);
    }
}
