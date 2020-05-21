package com.example.blu_main_test1.main_before;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.blu_main_test1.Intro.mIntro01;
import com.example.blu_main_test1.Intro.mIntro02;
import com.example.blu_main_test1.Intro.mIntro03;
import com.example.blu_main_test1.Main_page.MainActivity;
import com.example.blu_main_test1.Main_page.Main_view_pager;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.abstraction_fragment;
import com.example.blu_main_test1.main_before.login;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import javax.crypto.Mac;


public class onboard extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_onboard);
        SharedPreferences prefs=getSharedPreferences("isFirstRun", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        Fragment mintro01 = new mIntro01();
        Fragment mintro02 = new mIntro02();
        Fragment mintro03 = new mIntro03();

        if(isFirstRun)
        {

            addSlide(mintro01);
            addSlide(mintro02);
            addSlide(mintro03);

            prefs.edit().putBoolean("isFirstRun",false).apply();
        }
        else{
            Intent newIntent = new Intent(getApplicationContext(), theMainPage.class);
            startActivity(newIntent);

        }

        // 상단메뉴
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),theMainPage.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(), theMainPage.class);
        startActivity(intent);
        finish();
    }


}
