package com.example.blu_main_test1.Main_page.Main_page2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blu_main_test1.R;
import com.example.blu_main_test1.amount_change_viewpager.CommonFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;



public class
product_amount extends FragmentActivity {
    ViewPager viewPager;
    ImageButton top_btn, bottom_btn;
    private List<CommonFragment> fragments = new ArrayList<>();
    private final String[] textArray = {"쌍화","체감", "십전", "부기", "가월"
            , "쾌청", "휴안", "청공", "당감", "온기", "루이보스", "캐모마일", "히비스커스"};
    private final String[] textArray2 = {"전통","전통", "전통", "한차", "한차"
            , "한차", "한차", "한차", "한차", "한차", "블렌딩", "블렌딩", "블렌딩"};
    private final String[] imageArray = {"assets://ssanghwa.png", "assets://chegam.png", "assets://sipjeon.png", "assets://bugi.png",
            "assets://gawol.png", "assets://kwaecheong.png", "assets://huyuan.png", "assets://cheonggong.png", "assets://danggam.png","assets://ongi.png",
            "assets://rooibos.png","assets://chamomile.png","assets://hibi.png"};
    ImageView image;
    String imageview;
    TextView name, amount, origin, kind;
    ArrayList<String> db_result=new ArrayList<>();

    ImageButton back;
    View positionView;

   ProgressBar pgb ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_amount);

        pgb = (ProgressBar)findViewById(R.id.progressBar2);

        image=(ImageView)findViewById(R.id.image);
        viewPager = findViewById(R.id.view_pager);
        top_btn=findViewById(R.id.top_btn);
        bottom_btn=findViewById(R.id.bottom_btn);
        kind=(TextView)findViewById(R.id.kind);
        name=(TextView)findViewById(R.id.name);
        amount=(TextView)findViewById(R.id.amount);
        origin=(TextView)findViewById(R.id.origin);
        back=findViewById(R.id.back);
        positionView = findViewById(R.id.position_view);

        top_btn.setOnClickListener(top_move);
        bottom_btn.setOnClickListener(bottom_move);


        pgb.setVisibility(View.VISIBLE);
        //상단바 설정
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

     //  new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,textArray[viewPager.getCurrentItem()]);
      value();
        // new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        dealStatusBar(); // 상태 표시줄 높이 조정

        viewPager.setOffscreenPageLimit(4);

        initImageLoader();
        fillViewPager();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
    //firebase를 이용하여 cloud db에 있는 정보를 가져옴
    public void value()
    {
        final String[] a = new String[1];
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //컬렉션 선택
        CollectionReference result= db.collection("BLE_APP");
        //호출할 키값 쿼리 작성
        Query query = result.whereEqualTo("product_name",textArray[viewPager.getCurrentItem()]);
        //쿼리 get형식 호출
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        //해당 value값 불러옴
                            amount.setText(document.getData().get("amount").toString());
                            origin.setText(document.getData().get("product_origin").toString());
                    pgb.setVisibility(View.GONE);
                    }

                }
               else{
                    Log.d("err","err");
                }
            }
        });
    }

    View.OnClickListener top_move=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            ImageLoader.getInstance().displayImage(imageArray[viewPager.getCurrentItem()], image);
        }
    };
    View.OnClickListener bottom_move=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            ImageLoader.getInstance().displayImage(imageArray[viewPager.getCurrentItem()], image);
        }
    };
    private void fillViewPager() {
        for (int i = 0; i < 13; i++) {
            // 13개 생성
            fragments.add(new CommonFragment());
        }

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 13;
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public Fragment getItem(int position) {
                CommonFragment fragment = fragments.get(position % 10);
                fragment.bindData(textArray[position % textArray.length],textArray2[position%textArray.length]);
                ImageLoader.getInstance().displayImage(imageArray[viewPager.getCurrentItem()], image);

            // new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                value();
                kind.setText(textArray2[viewPager.getCurrentItem()]);
                name.setText(textArray[viewPager.getCurrentItem()]);

                viewPager.setClipToPadding(false);
                int dpValue = 65;
                float d = getResources().getDisplayMetrics().density;
                int margin = (int) (dpValue * d);
                viewPager.setPadding(0, margin, 0, (margin)+(int)(10*d));
                viewPager.setPageMargin(0);

                return fragment;
            }

        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
            @Override
            public void onPageSelected(int i) {
                ImageLoader.getInstance().displayImage(imageArray[viewPager.getCurrentItem()], image);
                kind.setText(textArray2[viewPager.getCurrentItem()]);
                name.setText(textArray[viewPager.getCurrentItem()]);

           //   new CustomTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            value();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
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
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs().build();

        // 2.싱글톤 imageloader 클래스 초기화
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }



 /*   class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        // doInBackground의 매개값이 문자열 배열인데요. 보낼 값이 여러개일 경우를 위해 배열로 합니다.
        protected String doInBackground(String... strings) {
            try {
                ArrayList<String> str;
                URL url = new URL("http://best54.cafe24.com/blu_android_jsp/product_amount.jsp");//보낼 jsp 주소를 ""안에 작성합니다.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "product_name=" + strings[0];    //보낼 정보인데요. GET방식으로 작성합니다. ex) "id=rain483&pwd=1234";
                //회원가입처럼 보낼 데이터가 여러 개일 경우 &로 구분하여 작성합니다.
                osw.write(sendMsg);//OutputStreamWriter에 담아 전송합니다.
                osw.flush();
                //jsp와 통신이 정상적으로 되었을 때 할 코드들입니다.
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String line;
                    String page = "";
                    // 라인을 받아와 합친다.
                    // 버퍼의 웹문서 소스를 줄 단위로 읽어(line), page에 저장함
                    while ((line = reader.readLine()) != null) {
                        page += line;

                    }
                    try {
                        // JSP에서 보낸 JSON 받아오자  JSONObject = siteDataMain
                        JSONObject json = new JSONObject(page);
                        JSONArray jArr = json.getJSONArray("list");

                        // JSON이 가진 크기만큼 데이터를 받아옴

                        for (int i = 0; i < jArr.length(); i++) {

                            json = jArr.getJSONObject(i);
                            amount.setText(json.getString("amount"));
                            origin.setText(json.getString("origin"));

                        }
                    } catch(Exception e){

                        e.printStackTrace();;

                    }

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                    // 통신이 실패했을 때 실패한 이유를 알기 위해 로그를 찍습니다.
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //jsp로부터 받은 리턴 값입니다.
            return null;
        }
    }*/

    //시본바 크기만큼 view를 늘려서 메뉴를 기본바 하단으로 이동
    private void dealStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int statusBarHeight = getStatusBarHeight();
            ViewGroup.LayoutParams lp = positionView.getLayoutParams();
            lp.height = statusBarHeight;
            positionView.setLayoutParams(lp);
        }
    }

    //핸드폰 상단 status bar 높이를 얻는 메소드
    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    }
