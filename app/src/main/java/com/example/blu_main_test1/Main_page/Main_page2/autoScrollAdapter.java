package com.example.blu_main_test1.Main_page.Main_page2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.blu_main_test1.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class autoScrollAdapter extends PagerAdapter  {
    Context context;
    ArrayList<Integer> data;

    public autoScrollAdapter(Context context, ArrayList<Integer> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auto_viewpager,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        Glide.with(context).load(data.get(position)).into(image_container);
        container.addView(v);

        //이미지 클릭시
        image_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (data.get(position)){
                    case R.drawable.radius_pic1:
                        Intent pic1= new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/list.html?cate_no=46"));
                        context.startActivity(pic1);
                        break;
                    case R.drawable.radius_pic2:
                        Intent pic2= new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EC%9D%B4%EB%B2%A4%ED%8A%B8-%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%ED%95%9C%EC%B0%A8-%ED%8B%B0%EC%BA%A1%EC%8A%90-7%EC%A2%85-11/36/category/25/display/1/"));
                        context.startActivity(pic2);
                        break;
                    case R.drawable.radius_pic3:
                        Intent pic3= new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EB%82%B4%EB%A7%98%EB%8C%80%EB%A1%9C-%EA%B3%A8%EB%9D%BC%EB%8B%B4%EB%8A%94-%ED%8B%B0%EC%BA%A1%EC%8A%90-%EB%AA%A8%EC%9D%8C/39/category/46/display/1/"));
                        context.startActivity(pic3);
                        break;
                }
            }
        });


        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
