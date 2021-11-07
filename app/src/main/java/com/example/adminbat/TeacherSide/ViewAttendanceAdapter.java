package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class ViewAttendanceAdapter extends BaseAdapter {
    ArrayList<String> datelist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    ViewAttendanceAdapter(Context context, ArrayList<String> datelist){
        this.context = context;
        this.datelist = datelist;
    }
    @Override
    public int getCount() {
        return datelist.size();
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
            convertView = inflater.inflate(R.layout.teachersideviewattendancelistview,parent,false);
        }
        TextView datetv = convertView.findViewById(R.id.viewattendateID);

        datetv.setText(datelist.get(position));

        return convertView;
    }
}
