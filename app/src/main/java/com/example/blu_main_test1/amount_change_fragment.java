package com.example.blu_main_test1;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_SCAN.BluetoothLeService;
import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class amount_change_fragment extends Fragment {

    public Button sub_amount;
    public static TextView coffee_b_amount,coffee_s_amount,tea_b_amount,tea_s_amount;
    public static ProgressBar pgb;
    private int coffee_big_number,coffee_small_number,tea_big_number,tea_small_number;
    public static SeekBar sb_c_b,sb_c_s,sb_t_b,sb_t_s;
    private String text;
    private ImageView close_amount;
    private Context context;

    public amount_change_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        DeviceControlActivity.main_text.setText("추출량을 자유롭게 변경해보세요.");

        DeviceControlActivity.Coffee_small();

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceControlActivity.Coffee_large();
            }
        }, 400);
        Handler delayHandler2 = new Handler();
        delayHandler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceControlActivity.Tea_small();
            }
        }, 800);
        Handler delayHandler3 = new Handler();
        delayHandler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceControlActivity.Tea_large();
            }
        }, 1200);
        Handler delayHandler4 = new Handler();
        delayHandler4.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceControlActivity.tempTask();
            }
        }, 1600);


        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,

                LinearLayout.LayoutParams.MATCH_PARENT
        );

        //back=(ImageButton)linear.findViewById(R.id.back);
        // background=(LinearLayout)linear.findViewById(R.id.background);
        // sub_background=(FrameLayout)linear.findViewById(R.id.sub_background);
        coffee_b_amount = (TextView) getView().findViewById(R.id.coffee_b_amount);
        coffee_s_amount = (TextView) getView().findViewById(R.id.coffee_s_amount);
        tea_b_amount = (TextView) getView().findViewById(R.id.tea_b_amount);
        tea_s_amount = (TextView) getView().findViewById(R.id.tea_s_amount);
        sub_amount = (Button) getView().findViewById(R.id.sub_amount);
        close_amount = (ImageView)getView().findViewById(R.id.close_amount);
        pgb = (ProgressBar) getView().findViewById(R.id.progressBar3);
        pgb.setVisibility(View.VISIBLE);


        sb_c_b = (SeekBar) getView().findViewById(R.id.seek_coffee_big);
        sb_c_s = (SeekBar) getView().findViewById(R.id.seek_coffee_small);
        sb_t_b = (SeekBar) getView().findViewById(R.id.seek_Tea_big);
        sb_t_s = (SeekBar) getView().findViewById(R.id.seek_Tea_small);

        close_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment nextFragmet = DeviceControlActivity.fragmentStack.pop();
                DeviceControlActivity.fragmentManager.beginTransaction().remove(nextFragmet).commit();
                DeviceControlActivity.device_con_view.setVisibility(View.VISIBLE);
                if(DeviceControlActivity.stateView.getText().toString().equals("가열중")){
                    DeviceControlActivity.main_text.setText("머신이 예열중입니다. 잠시만 기다려 주세요.");
                }else if(DeviceControlActivity.stateView.getText().toString().equals("절전모드")){
                    DeviceControlActivity.main_text.setText("추출을 원하시면 예열버튼을 눌러주세요.");
                }else{
                    DeviceControlActivity.main_text.setText("머신을 취향에 맞게 자유롭게 조절해 보세요.");
                }
            }
        });


        sb_c_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //롱고 시크바

            public void onStopTrackingTouch(SeekBar seekBar) {
                coffee_big_number = seekBar.getProgress();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                coffee_big_number = seekBar.getProgress();
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress >= 7) {
                    coffee_b_amount.setText("" + (progress * 10+30));
                } else {
                    coffee_b_amount.setText("0" + (progress * 10+30));
                    if (progress == 0) {
                        progress = 3;
                        coffee_b_amount.setText("0" + (progress * 10));
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
                if (progress >= 7) {
                    coffee_s_amount.setText("" + (progress * 10+30));
                } else {
                    coffee_s_amount.setText("0" + (progress * 10+30));
                    if (progress == 0) {
                        progress = 3;
                        coffee_s_amount.setText("0" + (progress * 10));
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
                if (progress >= 7) {
                    tea_b_amount.setText("" + (progress * 10+30));
                } else {
                    tea_b_amount.setText("0" + (progress * 10+30));
                    if (progress == 0) {
                        progress = 3;
                        tea_b_amount.setText("0" + (progress * 10));
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
                if (progress >= 7) {
                    tea_s_amount.setText("" + (progress * 10+30));

                } else {
                    tea_s_amount.setText("0" + (progress * 10+30));
                    if (progress == 0) {
                        progress = 3;
                        tea_s_amount.setText("0" + (progress * 10));
                    }
                }

            }
        });


        sub_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AlertDialog.Builder alert_confirm2 = new AlertDialog.Builder(getActivity());
                    alert_confirm2.setMessage("추출량을 변경 하시겠습니까?  ").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (DeviceControlActivity.mConnected) {
                                if (coffee_b_amount.getText().toString().length() != 0) {
                                    if(DeviceControlActivity.tmr!=null){
                                        DeviceControlActivity.tmr.cancel();
                                    }
                                    DeviceControlActivity.pgb2.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            //int coffee_b_amount_ex = Integer.parseInt(coffee_b_amount.getText().toString().substring(0, 2)); //임시
                                            String c_value = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2); //기존
                                            //String c_value = Integer.toString(coffee_b_amount_ex); //임시
                                            String cb_amount = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2) + DeviceControlActivity.stringToHex(c_value);//기존
                                            //String cb_amount = "05TCL" + c_value + DeviceControlActivity.stringToHex(c_value);//임시
                                            byte[] cb_value = {(byte) 0x02, (byte) 0x03};
                                            byte[] cb_temp = cb_amount.getBytes();
                                            byte[] cb_temp_data = new byte[cb_temp.length + 2];
                                            System.arraycopy(cb_value, 0, cb_temp_data, 0, 1);
                                            System.arraycopy(cb_temp, 0, cb_temp_data, 1, cb_temp.length);
                                            System.arraycopy(cb_value, 1, cb_temp_data, cb_temp.length + 1, 1);
                                            DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(cb_temp_data);
                                        }

                                    }, 500);  // 1 초 후에 실행


                                }


                                if (coffee_s_amount.getText().toString().length() != 0) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String c_value2 = "05TCS" + coffee_s_amount.getText().toString().substring(0, 2);
                                            String cs_amount = "05TCS" + coffee_s_amount.getText().toString().substring(0, 2) + DeviceControlActivity.stringToHex(c_value2);
                                            byte[] cs_value = {(byte) 0x02, (byte) 0x03};
                                            byte[] cs_temp = cs_amount.getBytes();
                                            byte[] cs_temp_data = new byte[cs_temp.length + 2];
                                            System.arraycopy(cs_value, 0, cs_temp_data, 0, 1);
                                            System.arraycopy(cs_temp, 0, cs_temp_data, 1, cs_temp.length);
                                            System.arraycopy(cs_value, 1, cs_temp_data, cs_temp.length + 1, 1);

                                            DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(cs_temp_data);
                                        }

                                    }, 1000);  // 1 초 후에 실행
                                }


                                if (tea_b_amount.getText().toString().length() != 0) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            String t_value = "05TTL" + tea_b_amount.getText().toString().substring(0, 2);
                                            String tb_amount = "05TTL" + tea_b_amount.getText().toString().substring(0, 2) + DeviceControlActivity.stringToHex(t_value);
                                            byte[] tb_value = {(byte) 0x02, (byte) 0x03};
                                            byte[] tb_temp = tb_amount.getBytes();
                                            byte[] tb_temp_data = new byte[tb_temp.length + 2];
                                            System.arraycopy(tb_value, 0, tb_temp_data, 0, 1);
                                            System.arraycopy(tb_temp, 0, tb_temp_data, 1, tb_temp.length);
                                            System.arraycopy(tb_value, 1, tb_temp_data, tb_temp.length + 1, 1);
                                            DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(tb_temp_data);
                                        }

                                    }, 1500);  // 1 초 후에 실행
                                }


                                if (tea_s_amount.getText().toString().length() != 0) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override

                                        public void run() {

                                            String t_value2 = "05TTS" + tea_s_amount.getText().toString().substring(0, 2);
                                            String ts_amount = "05TTS" + tea_s_amount.getText().toString().substring(0, 2) + DeviceControlActivity.stringToHex(t_value2);
                                            byte[] ts_value = {(byte) 0x02, (byte) 0x03};
                                            byte[] ts_temp = ts_amount.getBytes();
                                            byte[] ts_temp_data = new byte[ts_temp.length + 2];
                                            System.arraycopy(ts_value, 0, ts_temp_data, 0, 1);
                                            System.arraycopy(ts_temp, 0, ts_temp_data, 1, ts_temp.length);
                                            System.arraycopy(ts_value, 1, ts_temp_data, ts_temp.length + 1, 1);
                                            DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(ts_temp_data);
                                            DeviceControlActivity.pgb2.setVisibility(View.GONE);
                                            Toast.makeText(context,"추출량이 변경되었습니다. ",Toast.LENGTH_SHORT).show();
                                        }

                                    }, 2000);  // 1 초 후에 실행
                                }
                                DeviceControlActivity.tempTask();
                                DeviceControlActivity.fragmentManager = getFragmentManager();
                                DeviceControlActivity.fragmentTransaction = DeviceControlActivity.fragmentManager.beginTransaction();
                                DeviceControlActivity.fragmentStack.pop();
                                DeviceControlActivity.fragmentTransaction.remove(DeviceControlActivity.amount_fragment).commit();
                                DeviceControlActivity.device_con_view.setVisibility(View.VISIBLE);
                                DeviceControlActivity.main_text.setText("머신을 취향에 맞게 자유롭게 조절해 보세요.");




                            } else {
                                Toast.makeText(context, "블루투스를 재연결해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    alert_confirm2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alert_confirm2.show();

                } catch (Exception e) {

                    Toast.makeText(context, "블루투스를 재연결해 주세요", Toast.LENGTH_SHORT).show();

                }
            }

        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.activity_amount_change, container, false);

    }

}
