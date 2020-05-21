package com.example.blu_main_test1.Intro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blu_main_test1.R;

public class mIntro02 extends Fragment {

    public mIntro02() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle b) {
        super.onActivityCreated(b);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.mintro02, container, false);

    }

}

