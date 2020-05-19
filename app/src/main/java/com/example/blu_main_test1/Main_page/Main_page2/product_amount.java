package com.example.blu_main_test1.Main_page.Main_page2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.R;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_blend;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_hancha;
import com.example.blu_main_test1.amount_change_viewpager.TextViewPagerAdapter_tradition;


public class product_amount extends AppCompatActivity {

    public static ViewPager tradition_viewpager,hancha_viewpager,blend_viewpager;
    private TextViewPagerAdapter_tradition tradition_pagerAdapter;
    private TextViewPagerAdapter_hancha hancha_pagerAdapter;
    private TextViewPagerAdapter_blend blend_pagerAdapter;




    private TabLayout tabLayout;



    private final String[] imageArray = {"assets://ssanghwa.png", "assets://chegam.png", "assets://sipjeon.png", "assets://bugi.png",
            "assets://gawol.png", "assets://kwaecheong.png", "assets://huyuan.png", "assets://cheonggong.png", "assets://danggam.png","assets://ongi.png",
            "assets://rooibos.png","assets://chamomile.png","assets://hibi.png"};







    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_amount);




        tradition_viewpager = findViewById(R.id.tradition_viewpager);
        tradition_viewpager.setClipToPadding(false);
        int dpValue = 60;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        tradition_viewpager.setPadding(margin,0, margin, 0);
        tradition_viewpager.setPageMargin(0);


        hancha_viewpager =findViewById(R.id.hancha_viewpager);
        hancha_viewpager.setClipToPadding(false);
        hancha_viewpager.setPadding(margin,0, margin, 0);
        hancha_viewpager.setPageMargin(0);


        int dpValue_blend = 50;
        float d_blend = getResources().getDisplayMetrics().density;
        int margin_blend = (int) (dpValue_blend * d_blend);
        blend_viewpager = findViewById(R.id.blend_viewpager);
        blend_viewpager.setClipToPadding(false);
        blend_viewpager.setPadding(margin_blend,0, margin_blend, 0);
        blend_viewpager.setPageMargin(0);


        tradition_pagerAdapter = new TextViewPagerAdapter_tradition(this);
        tradition_viewpager.setAdapter(tradition_pagerAdapter);

        hancha_pagerAdapter = new TextViewPagerAdapter_hancha(this);
        hancha_viewpager.setAdapter(hancha_pagerAdapter);

        blend_pagerAdapter = new TextViewPagerAdapter_blend(this);
        blend_viewpager.setAdapter(blend_pagerAdapter);

        hancha_viewpager.setVisibility(View.GONE);
        blend_viewpager.setVisibility(View.GONE);



        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                chagneView(pos);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tradition_viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });





       // all_textList = new ArrayList<String>();

       // all_textList.addAll(tradition_textList);
       // all_textList.addAll(hancha_textList);
       // all_textList.addAll(blend_textList);





    }

    private void chagneView(int index){

        switch (index) {
            case 0 :
                tradition_viewpager.setVisibility(View.VISIBLE);
                hancha_viewpager.setVisibility(View.GONE);
                blend_viewpager.setVisibility(View.GONE);
                break;

            case 1:
                tradition_viewpager.setVisibility(View.GONE);
                hancha_viewpager.setVisibility(View.VISIBLE);
                blend_viewpager.setVisibility(View.GONE);
                break;

            case 2:
                tradition_viewpager.setVisibility(View.GONE);
                hancha_viewpager.setVisibility(View.GONE);
                blend_viewpager.setVisibility(View.VISIBLE);
        }

    }






    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


}