package com.example.blu_main_test1.amount_change_viewpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blu_main_test1.R;

public class CommonFragment extends Fragment {

    private String  texturl;
    private TextView number_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.page, null);
     //   MainActivity main = new MainActivity();
        //ImageLoader.getInstance().displayImage(image, main.image);


        return rootView;
    }

    public void bindData(String texturl) {
        this.texturl = texturl;

    }

}
