package com.fome.planster.daterepresenters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fome.planster.DateManager;
import com.fome.planster.models.Day;
import com.fome.planster.models.Hours;
import com.fome.planster.models.Row;
import com.fome.planster.models.RowElement;
import com.fome.planster.models.Time;
import com.fome.planster.models.Year;
import com.fome.planster.R;

import java.util.Date;

/**
 * Created by Alex on 24.03.2017.
 */
public class HoursRepresent extends DateRepresent implements IDateRepresent {

    Date selectedHours;

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

    public HoursRepresent (Context context, DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

    @Override
    public void setDates(Date minDate, Date selectedDate, Year year, Row row) {
        this.minDate = minDate;
        this.row = row;
        this.year = year;
        showDate(selectedDate);
        row.getFirstElement().getView().setVisibility(View.GONE);
        row.getLastElement().getView().setVisibility(View.GONE);
    }

    @Override
    public void showDate(Date date) {

        Day day = new Day(date, null, false, false);
        day.calculateHours();

        for (int i = 0; i < row.getDates().size(); i++) {
            RowElement rowElement = row.getDates().get(i);
            rowElement.getView().setVisibility(View.VISIBLE);
            rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
            rowElement.disableSelector();

            if (i >= day.getHours().size()) {
                rowElement.getView().setVisibility(View.GONE);
            } else {
                Hours hours = day.getHours().get(i);
                rowElement.setValue(hours.getFirstHour());
                rowElement.getView().setVisibility(View.VISIBLE);
                rowElement.getText().setText(String.valueOf(DateManager.getHours(hours.getFirstHour())) + "-" + String.valueOf(DateManager.getHours(hours.getLastHour())));

                if (selectedHours == null && DateManager.getHours(hours.getFirstHour()) <= time.getHour() && DateManager.getHours(hours.getLastHour()) >= time.getHour() || selectedHours != null && hours.getFirstHour().equals(selectedHours)) {
                    rowElement.enableSelector(time.getColor());
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    rowElement.hideSelector();
                    if (hours.getFirstHour().compareTo(minDate) <= 0 && DateManager.getDifferenceMinutes(minDate, hours.getLastHour()) <= 45 || hours.getFirstHour().compareTo(minDate) >= 0) {
                        rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
                    } else {
                        rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
                    }
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

        Date d = DateManager.getDateAfterHours(rowElement.getValue(), 6);
        d = DateManager.getDateAfterMinutes(d, 45);
        if (minDate.after(d)) {return;}

        markDate(rowElement.getValue());
    }

    @Override
    public void markDate(Date date) {
        selectedHours = date;
        if (date.compareTo(minDate) <= 0) {
            int dif = DateManager.getHours(date) - DateManager.getHours(minDate);
            date = DateManager.getDateAfterHours(date, dif);
            dif = DateManager.getMinutes(date) - DateManager.getMinutes(minDate);
            date = DateManager.getDateAfterMinutes(date, dif);
        }
        showDate(date);
        datePicker.rows[0].getDateRepresent().showDate(date);
        datePicker.rows[2].getDateRepresent().showDate(date);

    }

    @Override
    public void deleteSelection() {
        selectedHours = null;
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
