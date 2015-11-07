package com.sablegmail.masta.stromy.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sablegmail.masta.stromy.R;
import com.sablegmail.masta.stromy.weather.Day;

import java.util.ArrayList;

/**
 * Created by xsobolx on 05.11.2015.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private Day[] mDays;

    public DayAdapter(Day[] days) {
        mDays = days;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_list_item, parent, false);
        DayViewHolder viewHolder = new DayViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        holder.bindDay(mDays[position]);
    }

    @Override
    public int getItemCount() {
        return mDays.length;
    }

    public class DayViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconImageView;
        private TextView dayNameLabel;
        private TextView temperatureLabel;

        public DayViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
            dayNameLabel = (TextView) itemView.findViewById(R.id.dayNameLabel);
            temperatureLabel = (TextView) itemView.findViewById(R.id.temperatureDayLabel);
        }

        public void bindDay(Day day){
            iconImageView.setImageResource(day.getIconId());
            dayNameLabel.setText(day.getDayOfTheWeek());
            temperatureLabel.setText(day.getTemperatureMax() + "");
        }
    }

}
