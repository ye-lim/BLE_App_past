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
    private TextView coffee_b_amount,coffee_s_amount,tea_b_amount,tea_s_amount;
    private ProgressBar pgb;
    private int coffee_big_number,coffee_small_number,tea_big_number,tea_small_number;
    private SeekBar sb_c_b,sb_c_s,sb_t_b,sb_t_s;
    private String text;

    public amount_change_fragment() {
        // Required empty public constructor
    }



    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

        DeviceControlActivity.tmr.cancel();

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
                DeviceControlActivity.Tea_large();
            }
        }, 800);
        Handler delayHandler3 = new Handler();
        delayHandler3.postDelayed(new Runnable() {
            @Override
            public void run() {
                DeviceControlActivity.Tea_small();
            }
        }, 1200);


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
        pgb = (ProgressBar) getView().findViewById(R.id.progressBar3);
        pgb.setVisibility(View.VISIBLE);

        sb_c_b = (SeekBar) getView().findViewById(R.id.seek_coffee_big);
        sb_c_s = (SeekBar) getView().findViewById(R.id.seek_coffee_small);
        sb_t_b = (SeekBar) getView().findViewById(R.id.seek_Tea_big);
        sb_t_s = (SeekBar) getView().findViewById(R.id.seek_Tea_small);


        sb_c_b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //롱고 시크바

            public void onStopTrackingTouch(SeekBar seekBar) {
                coffee_big_number = seekBar.getProgress();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                coffee_big_number = seekBar.getProgress();
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if (progress >= 10) {
                    coffee_b_amount.setText("" + (progress * 10));
                } else {
                    coffee_b_amount.setText("0" + (progress * 10));
                    if (progress <= 3) {
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
                if (progress >= 10) {
                    coffee_s_amount.setText("" + (progress * 10));
                } else {
                    coffee_s_amount.setText("0" + (progress * 10));
                    if (progress <= 3) {
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
                if (progress >= 10) {
                    tea_b_amount.setText("" + (progress * 10));
                } else {
                    tea_b_amount.setText("0" + (progress * 10));
                    if (progress <= 3) {
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
                if (progress >= 10) {
                    tea_s_amount.setText("" + (progress * 10));
                } else {
                    tea_s_amount.setText("0" + (progress * 10));
                    if (progress <= 3) {
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
                                    if (Integer.parseInt(coffee_b_amount.getText().toString()) > 990) {
                                        Toast.makeText(getActivity(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                                        coffee_b_amount.requestFocus();
                                        return;
                                    }
                                    String c_value = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2);
                                    String cb_amount = "05TCL" + coffee_b_amount.getText().toString().substring(0, 2) + DeviceControlActivity.stringToHex(c_value);
                                    byte[] cb_value = {(byte) 0x02, (byte) 0x03};
                                    byte[] cb_temp = cb_amount.getBytes();
                                    byte[] cb_temp_data = new byte[cb_temp.length + 2];
                                    System.arraycopy(cb_value, 0, cb_temp_data, 0, 1);
                                    System.arraycopy(cb_temp, 0, cb_temp_data, 1, cb_temp.length);
                                    System.arraycopy(cb_value, 1, cb_temp_data, cb_temp.length + 1, 1);
                                    DeviceControlActivity.mBluetoothLeService.writeRXCharacteristic(cb_temp_data);

                                    DeviceControlActivity.pgb2.setVisibility(View.VISIBLE);

                                }


                                if (coffee_s_amount.getText().toString().length() != 0) {
                                    if (Integer.parseInt(coffee_s_amount.getText().toString()) > 990) {
                                        Toast.makeText(getActivity(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                                        coffee_s_amount.requestFocus();
                                        return;
                                    }
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
                                    if (Integer.parseInt(tea_b_amount.getText().toString()) > 990) {
                                        Toast.makeText(getActivity(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                                        tea_b_amount.requestFocus();
                                        return;
                                    }
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
                                    if (Integer.parseInt(tea_s_amount.getText().toString()) > 990) {
                                        Toast.makeText(getActivity(), "0~990사이로 설정해주세요", Toast.LENGTH_SHORT).show();
                                        tea_s_amount.requestFocus();
                                        return;
                                    }
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
                                           // Toast.makeText(getActivity(), "추출량이 변경되었습니다.", Toast.LENGTH_SHORT).show(); 예외발생 추후 개선
                                        }

                                    }, 2000);  // 1 초 후에 실행
                                }
                                DeviceControlActivity.tempTask();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                DeviceControlActivity.fragmentStack.pop();
                                fragmentTransaction.remove(DeviceControlActivity.amount_fragment).commit();
                                DeviceControlActivity.device_con_view.setVisibility(View.VISIBLE);



                            } else {
                                Toast.makeText(getActivity(), "블루투스를 재연결해 주세요.", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(getActivity(), "블루투스를 재연결해 주세요", Toast.LENGTH_SHORT).show();

                }
            }

        });
    }



    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() { //BroadcastReceiver는  연결상태와 데이터들을 받아오는 역할을 한다.
        @Override
        public void onReceive(final Context context, Intent intent) { //BluetoothLeService에서 sendBroadcast를 했을 때 호출.
            String action = intent.getAction(); //BluetoothLeService로부터 장치와 연결유뮤 상황을 action에 넣어 보내줌.
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) { //BLE장치에서 받은 데이터가 사용가능.
                final byte[] txValue = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                try {
                    //string형식으로 리스트 뷰에 표현
                    text = new String(txValue, "UTF-8");
                    if (text.substring(1, 6).equals("05RCL")) {
                        coffee_b_amount.setText(Integer.toString(Integer.parseInt(text.substring(6, 8)) * 10)); //ml은 안뜸, 페이지 나갔다 다시 들어오면 유지 x
                        sb_c_b.setProgress((Integer.parseInt(text.substring(6, 8))));


                    } else if (text.substring(1, 6).equals("05RCS")) {
                        coffee_s_amount.setText(Integer.toString(Integer.parseInt(text.substring(6, 8)) * 10));
                        sb_c_s.setProgress((Integer.parseInt(text.substring(6, 8))));

                    } else if (text.substring(1, 6).equals("05RTL")) {
                        tea_b_amount.setText(Integer.toString(Integer.parseInt(text.substring(6, 8)) * 10));
                        sb_t_b.setProgress((Integer.parseInt(text.substring(6, 8))));

                    } else if (text.substring(1, 6).equals("05RTS")) {
                        tea_s_amount.setText(Integer.toString(Integer.parseInt(text.substring(6, 8)) * 10));
                        sb_t_s.setProgress((Integer.parseInt(text.substring(6, 8))));
                        pgb.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_amount_change, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mGattUpdateReceiver, DeviceControlActivity.makeGattUpdateIntentFilter()); //브로드캐스트 등록

    }
}
