package com.example.blu_main_test1.Main_page.Main_page2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.blu_main_test1.R;
import com.example.blu_main_test1.product_viewpager.CommonFragment;
import com.example.blu_main_test1.product_viewpager.CustPagerTransformer;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.ArrayList;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class Main_page_2 extends Fragment implements View.OnClickListener {
    AutoScrollViewPager autoViewPager;
    private ViewPager pager;
    private List<CommonFragment> fragments = new ArrayList<>(); // ViewPager의 경우
    private final String[] imageArray = {"assets://ssanghwa.jpg","assets://chegam.jpg","assets://sipjeon.jpg",
            "assets://bugi.jpg","assets://gawol.jpg", "assets://kwaecheong.jpg",
            "assets://huyuan.jpg","assets://cheonggong.jpg","assets://danggam.jpg","assets://ongi.jpg",
            "assets://rooibos.jpg","assets://chamomile.jpg","assets://hibiscus.jpg","assets://machine.png"};
    private final String[] textArray = {"쌍화 티캡슐","체감 티캡슐", "십전 티캡슐", "부기 티캡슐", "가월 티캡슐"
            , "쾌청 티캡슐", "휴안 티캡슐", "청공 티캡슐", "당감 티캡슐", "온기 티캡슐", "루이보스", "캐모마일", "히비스커스", "머신"};
    private LinearLayout product1, product2, product3, product4;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main_2, container, false);
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.fragment_url);
        pager = (ViewPager) v.findViewById(R.id.viewpager);

        product1=(LinearLayout)v.findViewById(R.id.product1);
        product1.setOnClickListener(this);

        product2=(LinearLayout)v.findViewById(R.id.product2);
        product2.setOnClickListener(this);

        product3=(LinearLayout)v.findViewById(R.id.product3);
        product3.setOnClickListener(this);

        product4=(LinearLayout)v.findViewById(R.id.product4);
        product4.setOnClickListener(this);

        //autoscroll 리스트 추가
        final ArrayList<Integer> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add(R.drawable.radius_pic1);
        data.add(R.drawable.radius_pic2);
        data.add(R.drawable.radius_pic3);

        autoViewPager = (AutoScrollViewPager) v.findViewById(R.id.autoViewPager);
        autoScrollAdapter scrollAdapter = new autoScrollAdapter(getActivity(), data);
        autoViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoViewPager.setInterval(3000); // 페이지 넘어갈 시간 간격 설정
        autoViewPager.startAutoScroll(); //Auto Scroll 시작

        //상단 메뉴 조정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            autoViewPager.setElevation(6);
        }
        autoViewPager.setClipToPadding(false);
        int dpValue = 40; //사진의 크기를 나타냄
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        autoViewPager.setPadding(80, 0, 80, 0); //(int)(변수*d) 변수를 바꿔서 간격 조정
        autoViewPager.setPageMargin(margin/4);//사진간의 간격을 숫자로 조정




//이미지 로더
        initImageLoader();
//뷰페이저 채우기
        fillViewPager();



        return v;
    }



    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product1:
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/category/%EC%A0%84%ED%86%B5%ED%8B%B0%EC%BA%A1%EC%8A%90/32/"));
                startActivity(intent);
                break;
            case R.id.product2:
                Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/category/%ED%95%9C%EC%B0%A8%ED%8B%B0%EC%BA%A1%EC%8A%90/33/"));
                startActivity(intent2);
                break;
            case R.id.product3:
                Intent intent3=new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/category/%EB%B8%94%EB%A0%8C%EB%93%9C%ED%8B%B0%EC%BA%A1%EC%8A%90/45/"));
                startActivity(intent3);
                break;
            case R.id.product4:
                Intent intent4=new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/category/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EB%A8%B8%EC%8B%A0/24/"));
                startActivity(intent4);
                break;
        }
    }




    private void fillViewPager() {

        // 1. viewpager는 pagetransformer를 사용하여 시차효과를 추가합니다.
        pager.setPageTransformer(false, new CustPagerTransformer(getContext()));

        // 2. viewPager 어댑터 추가
        for (int i = 0; i < 13; i++) {
            // 10개 생성
            fragments.add(new CommonFragment());
        }
        pager.setAdapter(new FragmentStatePagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position % 10);
                fragment.bindData(imageArray[position % imageArray.length], textArray[position % imageArray.length]);
                return fragment;
            }

            @Override
            public int getCount() {
                return 666;
            }
        });

        // 3. viewPager가 슬라이드하여 표시기 조정
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    }


    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext())
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13) // default
                .discCacheSize(50 * 1024 * 1024) // 버퍼크기 50mb
                .discCacheFileCount(100) // 버퍼파일수
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(getContext())) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.싱글톤 imageloader 클래스 초기화
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

}


