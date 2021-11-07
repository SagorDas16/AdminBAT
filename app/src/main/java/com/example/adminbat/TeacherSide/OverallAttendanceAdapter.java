package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class OverallAttendanceAdapter extends BaseAdapter {

    ArrayList<String> uprolllist = new ArrayList<>();
    ArrayList<String> attendancelist = new ArrayList<>();
    ArrayList<String> percentagelist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    OverallAttendanceAdapter(Context context, ArrayList<String> uprolllist, ArrayList<String> attendancelist, ArrayList<String> percentagelist) {
        this.context = context;
        this.uprolllist = uprolllist;
        this.attendancelist = attendancelist;
        this.percentagelist = percentagelist;
    }

    @Override
    public int getCount() {
        return uprolllist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.teachersideoverallattendancelistview,parent,false);
        }
        TextView rolltv = convertView.findViewById(R.id.rollID);
        TextView attendancetv = convertView.findViewById(R.id.AttendanceID);
        TextView percentagetv = convertView.findViewById(R.id.percentageID);

        rolltv.setText(uprolllist.get(position));
        attendancetv.setText(attendancelist.get(position));
        percentagetv.setText(percentagelist.get(position));

        return convertView;
    }
}
