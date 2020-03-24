package com.example.blu_main_test1.BLE_connect;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.blu_main_test1.R;

import java.util.ArrayList;

public class QuadroListViewAdapter extends BaseAdapter
{
    private LayoutInflater inflater;
    private ArrayList<QuadroUart> data;
    private int layout;
    public QuadroListViewAdapter(Context context, int layout, ArrayList<QuadroUart> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getUuid();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        QuadroUart listviewitem=data.get(position);
        TextView name=(TextView)convertView.findViewById(R.id.textView);
        name.setText(listviewitem.getUuid());
        return convertView;
    }

    public ArrayList<QuadroUart> getData() {
        return data;
    }

    public void CountData(int position)
    {
        data.get(position).CountScanCount();
    }

    public void UpdateList(int gizun)
    {
        ArrayList<QuadroUart> temp = new ArrayList<QuadroUart>();
        for(int i = 0;i < data.size();i++)
        {
            if(data.get(i).getScanCount() >= gizun)
            {
                data.get(i).setScanCount(0);
                temp.add(data.get(i));
            }
        }
        data = temp;
    }
}
