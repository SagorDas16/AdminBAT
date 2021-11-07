package com.example.adminbat.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class TeacherAdapter extends BaseAdapter {
    ArrayList<String> namelist = new ArrayList<>();
    ArrayList<String> designationlist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    TeacherAdapter(Context context, ArrayList<String> namelist, ArrayList<String> designationlist){
        this.context = context;
        this.namelist = namelist;
        this.designationlist = designationlist;
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
            convertView = inflater.inflate(R.layout.teacher_teacher_listview,parent,false);
        }
        TextView nametv = convertView.findViewById(R.id.teachernameID);
        TextView designationtv = convertView.findViewById(R.id.teacherdesignationID);

        nametv.setText(namelist.get(position));
        designationtv.setText(designationlist.get(position));

        return convertView;
    }
}
