package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {

    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> rolllist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    StudentAdapter(Context context, ArrayList<String> namelist, ArrayList<String> rolllist){
        this.context = context;
        this.namelist = namelist;
        this.rolllist = rolllist;
    }

    @Override
    public int getCount() {
        return namelist.size();
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
            convertView = inflater.inflate(R.layout.teachersidestudentlistview,parent,false);
        }
        TextView nametv = convertView.findViewById(R.id.studentnameID);
        TextView rolltv = convertView.findViewById(R.id.studentrollID);

        nametv.setText(namelist.get(position));
        rolltv.setText(rolllist.get(position));

        return convertView;
    }
}
