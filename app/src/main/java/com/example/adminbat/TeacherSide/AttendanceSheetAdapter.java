package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class AttendanceSheetAdapter extends BaseAdapter {

    ArrayList<String> rolllist = new ArrayList<>();
    ArrayList<String> statuslist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    AttendanceSheetAdapter(Context context, ArrayList<String> rolllist, ArrayList<String> statuslist){
        this.context = context;
        this.rolllist = rolllist;
        this.statuslist = statuslist;
    }

    @Override
    public int getCount() {
        return rolllist.size();
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
            convertView = inflater.inflate(R.layout.teachersideattendancesheetlistview,parent,false);
        }
        TextView rolltv = convertView.findViewById(R.id.studentrollID);
        TextView statustv = convertView.findViewById(R.id.studentstatusID);

        rolltv.setText(rolllist.get(position));
        statustv.setText(statuslist.get(position));

        return convertView;
    }
}
