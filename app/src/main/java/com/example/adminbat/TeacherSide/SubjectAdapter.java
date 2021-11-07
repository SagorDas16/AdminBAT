package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class SubjectAdapter extends BaseAdapter {

    ArrayList<String> coursenamelist = new ArrayList<>();
    ArrayList<String> coursecodelist = new ArrayList<>();
    ArrayList<String> yearlist = new ArrayList<>();
    Context context;
    private LayoutInflater inflater;

    SubjectAdapter(Context context, ArrayList<String> coursenamelist, ArrayList<String> coursecodelist, ArrayList<String> yearlist){
        this.context = context;
        this.coursenamelist = coursenamelist;
        this.coursecodelist = coursecodelist;
        this.yearlist = yearlist;
    }

    @Override
    public int getCount() {
        return coursenamelist.size();
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
            convertView = inflater.inflate(R.layout.teachersidesubjectlistview,parent,false);
        }
        TextView subtv = convertView.findViewById(R.id.subjectnameID);
        TextView codtv = convertView.findViewById(R.id.subjectcodeID);
        TextView yeartv = convertView.findViewById(R.id.subjectyearID);

        subtv.setText(coursenamelist.get(position));
        codtv.setText(coursecodelist.get(position));
        yeartv.setText(yearlist.get(position));

        return convertView;
    }
}
