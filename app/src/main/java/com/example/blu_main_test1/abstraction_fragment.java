package com.example.blu_main_test1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_SCAN.BluetoothLeService;
import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;

public class abstraction_fragment extends Fragment {

    public static AppCompatDialog progressDialog;
    private String text;
    public static boolean abstraction = false;
    private Context context;





    public abstraction_fragment() {
        // Required empty public constructor
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

    public static void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void rong_coffee_startProgress() {
        progressON(getActivity(),"룽고를");
        abstraction = true;
    }

    public void presso_startProgress() {
        progressON(getActivity(),"에스프레소를");
        abstraction = true;
    }

    public void tea_big_startProgress() {
        progressON(getActivity(),"tea-대를");
        abstraction = true;
    }

    public void tea_small_startProgress() {
        progressON(getActivity(),"tea-일반을");
        abstraction = true;
    }


    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        DeviceControlActivity.main_text.setText("추출하고싶은 종류의 버튼을 누르면 추출이 됩니다.");
        getView().findViewById(R.id.rong_coffee).setOnClickListener(onClickListener); //롱고버튼 리스너
        getView().findViewById(R.id.presso).setOnClickListener(onClickListener); //에스프레소버튼 리스너
        getView().findViewById(R.id.tea_big).setOnClickListener(onClickListener); //tea대 리스서
        getView().findViewById(R.id.tea_small).setOnClickListener(onClickListener); //tea스몰 리스너
        getView().findViewById(R.id.backclose).setOnClickListener(onClickListener);

    }
    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.backclose:
                    Fragment nextFragmet = DeviceControlActivity.fragmentStack.pop();
                    DeviceControlActivity.fragmentManager.beginTransaction().remove(nextFragmet).commit();
                    DeviceControlActivity.device_con_view.setVisibility(View.VISIBLE);
                    DeviceControlActivity.main_text.setText("머신을 취향에 맞게 자유롭게 조절해 보세요.");
                    break;

                case R.id.rong_coffee:
                    if(DeviceControlActivity.mConnected && !DeviceControlActivity.row_state){
                        String rong_coffee = "03ECL37";
                        byte[] rong_coffee_value = {(byte) 0x02, (byte) 0x03};
                        byte[] rong_coffee__temp = rong_coffee.getBytes();
                        byte[] rong_coffee_temp_data = new byte[rong_coffee__temp.length + 2];
                        System.arraycopy(rong_coffee_value, 0, rong_coffee_temp_data, 0, 1);
                        System.arraycopy(rong_coffee__temp, 0, rong_coffee_temp_data, 1, rong_coffee__temp.length);
                        System.arraycopy(rong_coffee_value, 1, rong_coffee_temp_data, rong_coffee__temp.length + 1, 1);
                        DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(rong_coffee_temp_data);
                        rong_coffee_startProgress();
                    } else if(DeviceControlActivity.row_state){
                        Toast.makeText(context,"머신이 절전모드 상태 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"블루투스가 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                    }



                    break;

                case R.id.presso:
                    if(DeviceControlActivity.mConnected && !DeviceControlActivity.row_state) {
                        String presso_start = "03ECS3E";
                        byte[] presso_value = {(byte) 0x02, (byte) 0x03};
                        byte[] presso_temp = presso_start.getBytes();
                        byte[] presso_temp_data = new byte[presso_temp.length + 2];
                        System.arraycopy(presso_value, 0, presso_temp_data, 0, 1);
                        System.arraycopy(presso_temp, 0, presso_temp_data, 1, presso_temp.length);
                        System.arraycopy(presso_value, 1, presso_temp_data, presso_temp.length + 1, 1);
                        DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(presso_temp_data);
                        presso_startProgress();
                    } else if(DeviceControlActivity.row_state){
                        Toast.makeText(context,"머신이 절전모드 상태 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"블루투스가 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.tea_big:

                    if(DeviceControlActivity.mConnected && !DeviceControlActivity.row_state) {
                        String tea_big_start = "03ETL48";
                        byte[] tea_big_value = {(byte) 0x02, (byte) 0x03};
                        byte[] tea_big_temp = tea_big_start.getBytes();
                        byte[] tea_big_temp_data = new byte[tea_big_temp.length + 2];
                        System.arraycopy(tea_big_value, 0, tea_big_temp_data, 0, 1);
                        System.arraycopy(tea_big_temp, 0, tea_big_temp_data, 1, tea_big_temp.length);
                        System.arraycopy(tea_big_value, 1, tea_big_temp_data, tea_big_temp.length + 1, 1);
                        DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(tea_big_temp_data);
                        tea_big_startProgress();
                    } else if(DeviceControlActivity.row_state){
                        Toast.makeText(context,"머신이 절전모드 상태 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"블루투스가 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
                    }

                    break;

                case R.id.tea_small:

                    if(DeviceControlActivity.mConnected && !DeviceControlActivity.row_state) {
                        String tea_small_start = "03ETS4F";
                        byte[] tea_small_value = {(byte) 0x02, (byte) 0x03};
                        byte[] tea_small_temp = tea_small_start.getBytes();
                        byte[] tea_small_temp_data = new byte[tea_small_temp.length + 2];
                        System.arraycopy(tea_small_value, 0, tea_small_temp_data, 0, 1);
                        System.arraycopy(tea_small_temp, 0, tea_small_temp_data, 1, tea_small_temp.length);
                        System.arraycopy(tea_small_value, 1, tea_small_temp_data, tea_small_temp.length + 1, 1);
                        DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(tea_small_temp_data);
                        tea_small_startProgress();
                    } else if(DeviceControlActivity.row_state){
                        Toast.makeText(context,"머신이 절전모드 상태 입니다.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(context,"블루투스가 연결되어 있지 않습니다",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context,"추출 중지 되었습니다. ",Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = container.getContext();
        return inflater.inflate(R.layout.abstraction_fragment, container, false);


    }

}
