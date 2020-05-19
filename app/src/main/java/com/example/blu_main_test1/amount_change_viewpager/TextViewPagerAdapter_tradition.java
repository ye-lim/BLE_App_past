package com.example.blu_main_test1.amount_change_viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.Main_page.Main_page2.product_amount;
import com.example.blu_main_test1.R;

import java.lang.reflect.Field;


/**
 * Created by Deepak Mishra on 10/16/2017.
 */

public class TextViewPagerAdapter_tradition extends PagerAdapter {


    private Context mContext = null ;

    public TextViewPagerAdapter_tradition() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public TextViewPagerAdapter_tradition(Context context) {
        mContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page, container, false);
            TextView textView = (TextView) view.findViewById(R.id.page) ;
            textView.setText(DeviceControlActivity.tradition_textList.get(position));
        }

        // 뷰페이저에 추가.
        container.addView(view) ;

        return view ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 뷰페이저에서 삭제.
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return DeviceControlActivity.tradition_textList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}

