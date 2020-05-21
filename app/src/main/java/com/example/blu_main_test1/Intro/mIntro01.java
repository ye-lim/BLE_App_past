package com.example.blu_main_test1.Intro;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.R;

public class mIntro01 extends Fragment {

    public mIntro01() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mintro01, container, false);

    }

}
