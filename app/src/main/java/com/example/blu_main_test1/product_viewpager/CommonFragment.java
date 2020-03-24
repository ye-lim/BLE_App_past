package com.example.blu_main_test1.product_viewpager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blu_main_test1.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

public class CommonFragment extends Fragment implements DragLayout.GotoDetailListener  {
    private String imageUrl, texturl;
    private ImageView imageView;
    private TextView address1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common, null);
        DragLayout dragLayout = (DragLayout) rootView.findViewById(R.id.drag_layout);
        imageView = (ImageView) dragLayout.findViewById(R.id.image);
        ImageLoader.getInstance().displayImage(imageUrl, imageView);
        address1=(TextView)dragLayout.findViewById(R.id.address1);
        address1.setText(texturl);

        dragLayout.setGotoDetailListener(this);

        return rootView;
    }

@Override
    public void gotoDetail() {

/*        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {*/
            switch (imageUrl){
                case "assets://ssanghwa.jpg" :
                    Intent ssanghwa = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%8C%8D%ED%99%94-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%8C%8D%ED%99%94-%EC%BA%A1%EC%8A%90%ED%8B%B0/21/category/32/display/1/"));
                    startActivity(ssanghwa);
                    break;
                case "assets://chegam.jpg" :
                    Intent chegam = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%B2%B4%EA%B0%90-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%B2%B4%EA%B0%90-%EC%BA%A1%EC%8A%90%ED%8B%B0/23/category/32/display/1/"));
                    startActivity(chegam);
                    break;
                case "assets://sipjeon.jpg" :
                    Intent sipjeon = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%8B%AD%EC%A0%84-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%8B%AD%EC%A0%84-%EC%BA%A1%EC%8A%90%ED%8B%B0/22/category/32/display/1/"));
                    startActivity(sipjeon);
                    break;
                case "assets://bugi.jpg" :
                    Intent bugi = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EB%B6%80%EA%B8%B0-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EB%B6%80%EA%B8%B0-%EC%BA%A1%EC%8A%90%ED%8B%B0/29/category/33/display/1/"));
                    startActivity(bugi);
                    break;
                case "assets://cheonggong.jpg" :
                    Intent cheonggong = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%B2%AD%EA%B3%B5-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%B2%AD%EA%B3%B5-%EC%BA%A1%EC%8A%90%ED%8B%B0/27/category/33/display/1/"));
                    startActivity(cheonggong);
                    break;
                case "assets://danggam.jpg" :
                    Intent danggam = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EB%8B%B9%EA%B0%90-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EB%8B%B9%EA%B0%90-%EC%BA%A1%EC%8A%90%ED%8B%B0/24/category/33/display/1/"));
                    startActivity(danggam);
                    break;
                case "assets://ongi.jpg" :
                    Intent ongi = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%98%A8%EA%B8%B0-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%98%A8%EA%B8%B0-%EC%BA%A1%EC%8A%90%ED%8B%B0/28/category/33/display/1/"));
                    startActivity(ongi);
                    break;
                case "assets://rooibos.jpg" :
                    Intent rooibos = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EB%A3%A8%EC%9D%B4%EB%B3%B4%EC%8A%A4-%EB%B8%94%EB%A0%8C%EB%93%9C-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EB%A3%A8%EC%9D%B4%EB%B3%B4%EC%8A%A4-%EC%BA%A1%EC%8A%90%ED%8B%B0/18/category/45/display/1/"));
                    startActivity(rooibos);
                    break;
                case "assets://chamomile.jpg" :
                    Intent chamomile = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%BA%90%EB%AA%A8%EB%A7%88%EC%9D%BC-%EB%B8%94%EB%A0%8C%EB%93%9C-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%BA%90%EB%AA%A8%EB%A7%88%EC%9D%BC-%EC%BA%A1%EC%8A%90%ED%8B%B0/20/category/45/display/1/"));
                    startActivity(chamomile);
                    break;
                case "assets://hibiscus.jpg" :
                    Intent hibiscus = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%ED%9E%88%EB%B9%84%EC%8A%A4%EC%BB%A4%EC%8A%A4-%EB%B8%94%EB%A0%8C%EB%93%9C-%ED%8B%B0%EC%BA%A1%EC%8A%90-%ED%9E%88%EB%B9%84%EC%8A%A4%EC%BB%A4%EC%8A%A4-%EC%BA%A1%EC%8A%90%ED%8B%B0/17/category/45/display/1/"));
                    startActivity(hibiscus);
                    break;
                case "assets://gawol.jpg" :
                    Intent gawol = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EA%B0%80%EC%9B%94-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EA%B0%80%EC%9B%94-%EC%BA%A1%EC%8A%90%ED%8B%B0/30/category/33/display/1/"));
                    startActivity(gawol);
                    break;
                case "assets://kwaecheong.jpg" :
                    Intent kwaecheong = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%EC%BE%8C%EC%B2%AD-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%EC%BE%8C%EC%B2%AD-%EC%BA%A1%EC%8A%90%ED%8B%B0/26/category/33/display/1/"));
                    startActivity(kwaecheong);
                    break;
                case "assets://huyuan.jpg" :
                    Intent huyuan = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EB%A9%94%EB%94%94%ED%94%84%EB%A0%88%EC%86%8C-%ED%9C%B4%EC%95%88-%ED%8B%B0%EC%BA%A1%EC%8A%90-10%EA%B0%9C%EC%9E%85%ED%9C%B4%EC%95%88-%EC%BA%A1%EC%8A%90%ED%8B%B0/25/category/33/display/1/"));
                    startActivity(huyuan);
                    break;
                case "assets://machine.png" :
                    Intent machine = new Intent(Intent.ACTION_VIEW, Uri.parse("http://medipresso.com/product/%EC%BB%A4%ED%94%BC%EC%B0%A8%EC%BA%A1%EC%8A%90%EB%A8%B8%EC%8B%A0medi-cntm01%ED%8B%B0%ED%8F%AC%ED%8A%B8%EC%BB%A4%ED%94%BC%EB%A8%B8%EC%8B%A0%EC%BA%A1%EC%8A%90%EC%BB%A4%ED%94%BC%EB%A8%B8%EC%8B%A0/13/category/24/display/2/"));
                    startActivity(machine);
                    break;
            }
           // }
        //});
    }


    public void bindData(String imageUrl, String texturl) {
        this.imageUrl = imageUrl;
        this.texturl=texturl;
    }

}
