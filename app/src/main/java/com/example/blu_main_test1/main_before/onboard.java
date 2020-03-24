package com.example.blu_main_test1.main_before;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.example.blu_main_test1.R;
import com.example.blu_main_test1.main_before.login;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;


public class onboard extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_onboard);
        SharedPreferences prefs=getSharedPreferences("isFirstRun", MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        if(isFirstRun)
        {

            addSlide(AppIntroFragment.newInstance("머신 간편 조작", "블루투스를 이용하여 휴대전화로 간편하게 조작하세요",
                    R.drawable.machine2, ContextCompat.getColor(getApplicationContext(), R.color.first_onboard)));
            addSlide(AppIntroFragment.newInstance("조작 기능 소개", "추출량 변화, 솔팅타임, 예열, 인체감지, 슬립시간, 추출 시간 등을 조작 가능합니다.",
                    R.drawable.machine, ContextCompat.getColor(getApplicationContext(), R.color.first_onboard)));
            addSlide(AppIntroFragment.newInstance("캡슐 구매, 구독 서비스", "앱과 연동된 홈페이지에서 캡슐 구매와 구독 신청 가능합니다. ",
                    R.drawable.capsure, ContextCompat.getColor(getApplicationContext(), R.color.three_onboard)));

            prefs.edit().putBoolean("isFirstRun",false).apply();
        }
        else{
            Intent newIntent = new Intent(getApplicationContext(), login.class);
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
        Intent intent=new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),login.class);
        startActivity(intent);
        finish();
    }


}
