package com.example.blu_main_test1.amount_change_viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.blu_main_test1.BLE_SCAN.DeviceControlActivity;
import com.example.blu_main_test1.R;

public class ImgViewPagerAdapter_blend extends PagerAdapter {


    private Context mContext = null ;

    public ImgViewPagerAdapter_blend() {

    }

    // Context를 전달받아 mContext에 저장하는 생성자 추가.
    public ImgViewPagerAdapter_blend(Context context) {
        mContext = context ;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null ;

        if (mContext != null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.page_img, container, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.page_img) ;
            Glide.with(mContext).load(DeviceControlActivity.blend_textList_img.get(position)).into(imageView);

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

        return DeviceControlActivity.blend_textList_img.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View)object);
    }
}
