package com.example.blu_main_test1.BLE_SCAN;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.example.blu_main_test1.R;


public class DesignedDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView btn_cancel;
    private TextView btn_ok;

    public DesignedDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_designed);

        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_ok = (TextView) findViewById(R.id.btn_ok);

        btn_cancel.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_ok:
                dismiss();
                Fragment nextFragment = DeviceControlActivity.fragmentStack.pop();
                DeviceControlActivity.fragmentManager.beginTransaction().remove(nextFragment).commit();
                DeviceControlActivity.device_con_view.setVisibility(View.VISIBLE);
                if(DeviceControlActivity.stateView.getText().toString().equals("가열중")){
                    DeviceControlActivity.main_text.setText("머신이 예열중입니다. 잠시만 기다려 주세요.");
                }else if(DeviceControlActivity.stateView.getText().toString().equals("절전모드")){
                    DeviceControlActivity.main_text.setText("추출을 원하시면 예열버튼을 눌러주세요.");
                }else{
                    DeviceControlActivity.main_text.setText("머신을 취향에 맞게 자유롭게 조절해 보세요.");
                }
                break;
        }
    }
}