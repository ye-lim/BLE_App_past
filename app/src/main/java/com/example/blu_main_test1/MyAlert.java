package com.example.blu_main_test1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.blu_main_test1.BLE_SCAN.BluetoothLeService;
import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;

public class MyAlert extends AppCompatActivity {

    private BluetoothLeService mBluetoothLeService;
    private String mDeviceAddress;
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_alert);



        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MyAlert.this);
        alert_confirm.setMessage("블루투스 환경이 원활하지 않습니다. \n머신상태를 확인해 주세요").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                BluetoothLeService.connect_state = true;
                finish();
                Intent intent = new Intent(getApplicationContext(),DeviceControlActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        alert_confirm.show();


    }


}
