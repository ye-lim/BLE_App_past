package com.example.blu_main_test1.Main_page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_button.abstraction;
import com.example.blu_main_test1.BLE_connect.UartService;
import com.example.blu_main_test1.BLE_connect.connect;
import com.example.blu_main_test1.Main_page.Main_page2.product_amount;
import com.example.blu_main_test1.R;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import static app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT;


public class MainActivity extends Fragment {

    UartService m_UartService;

   private ImageButton blu_connect, amount_change,back,amount_start,product_amount,state_start,amount_stop,low_start;

   public static TextView connect_result, coffee_b,coffee_s, tea_b,tea_s, state, temper;
    private LinearLayout background,sub_background;



    private static final int REQUEST_NEW_DEVICE = 1;

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_main);

        //퍼미션 관리
        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)){
                    Toast.makeText(this, "블루투스 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return;
            } else {
            }
        }else
        {
        }
        
        //
        this.InitializeMovieData();

        ListView listView = (ListView)v.findViewById(R.id.listView);
        final listadapter myAdapter = new listadapter(this,drawDataList);

        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getGrade(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }*/

    @SuppressLint("ResourceAsColor")
    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main, container, false);

        blu_connect=(ImageButton)view.findViewById(R.id.blu_connect);
        amount_change=(ImageButton)view.findViewById(R.id.amount_change);
        amount_start=(ImageButton)view.findViewById(R.id.amount_start);
        product_amount=(ImageButton)view.findViewById(R.id.product_amount);
        amount_stop=(ImageButton)view.findViewById(R.id.amount_stop);
        low_start=(ImageButton)view.findViewById(R.id.low_start);
        connect_result=(TextView)view.findViewById(R.id.connect_result);
        coffee_b=(TextView)view.findViewById(R.id.coffee_b);
        coffee_s=(TextView)view.findViewById(R.id.coffee_s);
        tea_b=(TextView)view.findViewById(R.id.tea_b);
        tea_s=(TextView)view.findViewById(R.id.tea_s);
        state=(TextView)view.findViewById(R.id.state);
        temper=(TextView)view.findViewById(R.id.temper);


        //블루투스 버튼
        blu_connect.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(getActivity(), connect.class);
            startActivity(intent);
        }

    });
        amount_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), abstraction.class);
                startActivity(intent);
            }
        });

        product_amount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onClick(View v) {
                Intent intent_product=new Intent(getActivity(), com.example.blu_main_test1.Main_page.Main_page2.product_amount.class);
                startActivity(intent_product);
            }
        });

    return view;
    }











}
