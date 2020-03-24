package com.example.blu_main_test1.Main_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.blu_main_test1.Main_page.sampledata;
import com.example.blu_main_test1.R;


import java.util.ArrayList;

public class listadapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<sampledata> sample;

    public listadapter(Context context, ArrayList<sampledata> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public sampledata getItem(int position) {
        return sample.get(position);
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.draw_listview, null);


        TextView grade = (TextView)view.findViewById(R.id.grade);

        grade.setText(sample.get(position).getGrade());

        return view;
    }
}