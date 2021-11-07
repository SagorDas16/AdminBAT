package com.example.adminbat.TeacherSide;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminbat.R;

import java.util.ArrayList;

public class AtAdapter extends RecyclerView.Adapter<AtAdapter.AtViewHolder> {
    private static ClickListener clickListener;
    Context context;
    ArrayList<String> namelist,rolllist,statuslist;


    public AtAdapter(Context context, ArrayList<String> namelist, ArrayList<String> rolllist, ArrayList<String> statuslist) {
        this.context = context;
        this.namelist = namelist;
        this.rolllist = rolllist;
        this.statuslist = statuslist;
    }

    @NonNull
    @Override
    public AtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.sample_attendence,parent,false);
        return new AtViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AtViewHolder holder, int position) {
        holder.rolltextview.setText(rolllist.get(position));
        holder.nametextview.setText(namelist.get(position));
        holder.statustextview.setText(statuslist.get(position));

        holder.cardView.setCardBackgroundColor(getcolor(position));
    }

    private int getcolor(int position) {
        String status = statuslist.get(position);
        if(status.equals("P")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        }
        else if(status.equals("A")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));
        }
        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.white)));
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    class AtViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView rolltextview,nametextview,statustextview;
        CardView cardView;

        public AtViewHolder(@NonNull View itemView) {
            super(itemView);
            rolltextview = itemView.findViewById(R.id.samplerollID);
            nametextview = itemView.findViewById(R.id.samplenameID);
            statustextview = itemView.findViewById(R.id.samplestatusID);
            cardView = itemView.findViewById(R.id.attendencecardviewID);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.OnItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.OnItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }
    public interface ClickListener{
        void OnItemClick(int Position, View view);
        void OnItemLongClick(int Position, View view);
    }
    public void setOnItemClickListener(ClickListener clickLIstener){

        AtAdapter.clickListener = clickLIstener;
    }



}
