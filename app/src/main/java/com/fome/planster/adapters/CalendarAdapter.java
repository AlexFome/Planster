package com.fome.planster.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fome.planster.DateManager;
import com.fome.planster.FontManager;
import com.fome.planster.models.Day;
import com.fome.planster.models.Month;
import com.fome.planster.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 14.03.2017.
 */
public class CalendarAdapter extends BaseAdapter {

    Month month;
    ArrayList<Day> days;
    Context context;

    public CalendarAdapter (Context context, Month month) {
        this.context = context;
        this.month = month;
        days = month.getDays ();
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Day day = days.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.day, null);
            FontManager.setFont(context, convertView, FontManager.BOLDFONT);
        }

        TextView dayNum = (TextView) convertView.findViewById(R.id.dayNum);
        dayNum.setText(String.valueOf(days.get(position).getDayNum()));

        if (DateManager.compareDays(day.getDate(), new Date()) == 0) { // If this day is today
            dayNum.setTextColor(ContextCompat.getColor(context, R.color.white));
            ((View)dayNum.getParent()).setBackgroundResource(R.drawable.today);
        } else if (day.isAnotherWeek()) { // if it belongs to another month
            ((View)dayNum.getParent()).setBackgroundResource(R.drawable.day);
            dayNum.setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
        } else if (day.isWeekend()) { // if it is a weekend
            ((View)dayNum.getParent()).setBackgroundResource(R.drawable.day);
            dayNum.setTextColor(ContextCompat.getColor(context, R.color.red));
        } else { // if it is a regular day
            ((View)dayNum.getParent()).setBackgroundResource(R.drawable.day);
            dayNum.setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
        }

        return convertView;
    }
}
