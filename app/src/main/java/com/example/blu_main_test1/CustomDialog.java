package com.example.blu_main_test1;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.blu_main_test1.main_before.theMainPage;

public class CustomDialog {

    private Context context;

    public CustomDialog(Context context){
        this.context = context;
    }

    public void callFunction(){
        final Dialog dlg = new Dialog(context);

        dlg.setContentView(R.layout.custom_dialog);

        dlg.show();


        final TextView serial_number = (TextView)dlg.findViewById(R.id.serial_num);
        final Button okButton = (Button) dlg.findViewById(R.id.okButton);
        final Button cancelButton = (Button) dlg.findViewById(R.id.cancelButton);

        serial_number.setText(theMainPage.serial_number_get);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theMainPage.coupon.setVisibility(View.GONE);
                dlg.dismiss();
            }
        });



    }
}
