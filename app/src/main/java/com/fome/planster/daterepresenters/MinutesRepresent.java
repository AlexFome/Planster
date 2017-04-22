package com.fome.planster.daterepresenters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fome.planster.DateManager;
import com.fome.planster.models.Hour;
import com.fome.planster.models.Row;
import com.fome.planster.models.RowElement;
import com.fome.planster.models.Time;
import com.fome.planster.models.Year;
import com.fome.planster.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public class MinutesRepresent extends DateRepresent implements IDateRepresent {

    Date selectedMinutes;

    View.OnClickListener onRightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nextDate();
        }
    };
    View.OnClickListener onLeftClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            prevDate();
        }
    };
    View.OnClickListener onDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onDateClicked(v);
        }
    };

    public MinutesRepresent (Context context, DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

    @Override
    public void setDates(Date minDate, Date selectedDate, Year year, Row row) {
        this.minDate = minDate;
        this.year = year;
        this.row = row;

        row.getFirstElement().getView().setVisibility(View.GONE);
        row.getLastElement().getView().setVisibility(View.GONE);
        showDate(selectedDate);
    }

    @Override
    public void showDate(Date date) {

        Hour hour = new Hour(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(date), 0, 0));
        ArrayList<Date> minutes = hour.getMinutes();

        for (int i = 0; i < row.getDates().size(); i++) {
            RowElement rowElement = row.getDates().get(i);
            rowElement.setValue(minutes.get(i));
            rowElement.disableSelector();

            rowElement.getText().setText(":" + String.valueOf(DateManager.getMinutes(rowElement.getValue())));
            if (selectedMinutes == null && DateManager.getMinutes(rowElement.getValue()) - time.getMinutes() < 15 && DateManager.getMinutes(rowElement.getValue()) - time.getMinutes() >= 0 || selectedMinutes == null && DateManager.getMinutes(rowElement.getValue()) >= 45 && time.getMinutes() > 45 || selectedMinutes != null && DateManager.getMinutes(selectedMinutes) >= DateManager.getMinutes(rowElement.getValue()) && DateManager.getMinutes(selectedMinutes) < DateManager.getMinutes(rowElement.getValue()) + 15) {
                rowElement.enableSelector(time.getColor());
                rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                rowElement.hideSelector();
                if (DateManager.getDifferenceMinutes(rowElement.getValue(), minDate) >= 0) {
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
                } else {
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
                }
            }
        }

    }

    @Override
    public void nextDate() {

    }

    @Override
    public void prevDate() {

    }

    @Override
    public void onDateClicked(View v) {
        RowElement rowElement = null;
        for (int i = 0; i < row.getDates().size(); i++) {
            if (row.getDates().get(i).getView() == v) {
                rowElement = row.getDates().get(i);
            }
        }

        if (rowElement.getValue().compareTo(minDate) < 0) {return;}

        markDate(rowElement.getValue());
    }

    @Override
    public void markDate(Date date) {
        selectedMinutes = date;
        time.setMinutes(date);
        showDate(date);
    }

    @Override
    public void deleteSelection() {
        selectedMinutes = null;
    }

    @Override
    public void setTime(Time time) {
        this.time = time;
    }

    @Override
    public View.OnClickListener getOnRightClickListener() {
        return onRightClickListener;
    }

    @Override
    public View.OnClickListener getOnLeftClickListener() {
        return onLeftClickListener;
    }

    @Override
    public View.OnClickListener getOnDateClickListener() {
        return onDateClickListener;
    }
}
