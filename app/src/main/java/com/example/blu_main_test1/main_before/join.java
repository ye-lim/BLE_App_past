package com.example.blu_main_test1.main_before;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.example.blu_main_test1.R;

import static com.example.blu_main_test1.R.drawable.check_icon;

public class join extends AppCompatActivity {

    Button next;
    CheckBox total_agree,select_agree,needful_agree1,needful_agree2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        total_agree=(CheckBox)findViewById(R.id.total_agree);
        select_agree=(CheckBox)findViewById(R.id.select_agree);
        needful_agree1=(CheckBox)findViewById(R.id.needful_agree1);
        needful_agree2=(CheckBox)findViewById(R.id.needful_agree2);
        next=(Button)findViewById(R.id.next);

        total_agree.setOnClickListener(btnListener);
        select_agree.setOnClickListener(btnListener);
        needful_agree1.setOnClickListener(btnListener);
        needful_agree2.setOnClickListener(btnListener);
        next.setOnClickListener(btnListener);


        //상단메뉴
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);  //상단 바 컬러 투명
                getWindow()
                        .getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow()
                        .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }


    //동의하기 버튼에 따른 구조
    View.OnClickListener btnListener = new View.OnClickListener() {
        @SuppressLint("ResourceType")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.total_agree:
                    if (total_agree.isChecked() == false) {
                        v.setSelected(true);
                        needful_agree1.setChecked(false);
                        needful_agree2.setChecked(false);
                        select_agree.setChecked(false);
                        next.setVisibility(View.VISIBLE);
                    } else {
                        v.setSelected(false);
                        needful_agree1.setChecked(true);
                        needful_agree2.setChecked(true);
                        select_agree.setChecked(true);
                        next.setVisibility(View.INVISIBLE);
                    }
                    break;

                case R.id.needful_agree1:
                    if (needful_agree1.isChecked() == false) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                        next.setVisibility(View.INVISIBLE);
                    }
                    break;

                case R.id.needful_agree2:
                    if (needful_agree2.isChecked() == false) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                        next.setVisibility(View.INVISIBLE);
                    }
                    break;

                case R.id.select_agree:
                    if (select_agree.isChecked() == false) {
                        v.setSelected(true);
                    } else {
                        v.setSelected(false);
                    }
                    break;

                case R.id.next:
                    Intent intent = new Intent(join.this, join_main.class);
                    startActivity(intent);
                    finish();
                    break;

            }

            if (needful_agree1.isChecked() == true && needful_agree2.isChecked() == true)
                next.setVisibility(View.VISIBLE);
            else if (needful_agree1.isChecked() == false || needful_agree2.isChecked() == false)
                next.setVisibility(View.INVISIBLE);
            else if (needful_agree1.isChecked() == false || needful_agree2.isChecked() == false || select_agree.isChecked() == false)
                total_agree.setSelected(false);
            else if(needful_agree1.isChecked()==true&&needful_agree2.isChecked()==true&&select_agree.isChecked()==true)
                total_agree.setChecked(true);

        }
    };

}
