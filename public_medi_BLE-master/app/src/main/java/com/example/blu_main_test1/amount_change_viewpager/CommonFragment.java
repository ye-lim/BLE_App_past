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

   private String  texturl,s_text;
   public static String image;
    private TextView number_tv,s_number_tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_common_amount_ch, null);
     //   MainActivity main = new MainActivity();
        //ImageLoader.getInstance().displayImage(image, main.image);
        number_tv=(TextView)rootView.findViewById(R.id.number_tv);
        s_number_tv=(TextView)rootView.findViewById(R.id.s_number_tv);
        number_tv.setText(texturl);
        s_number_tv.setText(s_text);
        return rootView;
    }

    public void bindData(String texturl,String s_text) {
        this.texturl = texturl;
        this.s_text = s_text;

    }

}
