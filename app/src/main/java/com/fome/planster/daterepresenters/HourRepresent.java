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
 * Created by Alex on 23.03.2017.
 */
public class HourRepresent extends DateRepresent implements IDateRepresent {

    public Date selectedHour;
    public Date displayedHour;

    boolean lastHours;
    boolean firstHours;

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

    public HourRepresent (Context context, DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

    @Override
    public void setDates(Date minDate, Date selectedDate, Year year,  Row row) {
        this.minDate = minDate;
        this.year = year;
        this.row = row;
        showDate(selectedDate);
    }

    @Override
    public void showDate(Date date) {
        displayedHour = date;
        Day day = new Day(date, null, false, false); // initializing a day, which would be shown
        Hours hours = day.getHours(date);

        for (int i = 0; i < row.getDates().size(); i++) {

            RowElement rowElement = row.getDates().get(i);
            rowElement.disableSelector();

            if (i >= hours.getHours().size()) {
                row.getLastElement().getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
                rowElement.getText().setText("");
                lastHours = true;
            } else {
                lastHours = false;

                rowElement.setValue(hours.getHours().get(i).getDate());

                if (selectedHour == null && time.getHour() == DateManager.getHours(rowElement.getValue()) && time.getDay() == DateManager.getDay(rowElement.getValue()) || selectedHour != null && DateManager.getHours(selectedHour) == DateManager.getHours(rowElement.getValue()) && DateManager.getDay(date) == DateManager.getDay(selectedHour)) {
                    rowElement.enableSelector(time.getColor());
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.white));
                } else {
                    rowElement.hideSelector();
                    if (DateManager.getDifferenceMinutes(minDate, rowElement.getValue()) < 45) {
                        rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
                    } else {
                        rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
                    }
                }

                rowElement.getText().setText(
                        String.valueOf(DateManager.getHours(rowElement.getValue()))
                );
                row.getLastElement().getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
            }
        }

        if (DateManager.getHours(row.getDates().get(0).getValue()) != 0) {
            firstHours = false;
            row.getFirstElement().getText().setText(context.getString(R.string.arrow_left));
            row.getFirstElement().getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        } else {
            firstHours = true;
            row.getFirstElement().getText().setText(context.getResources().getString(R.string.sun));
            row.getFirstElement().getText().setTextColor(ContextCompat.getColor(context, R.color.yellow));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        }

    }

    @Override
    public void nextDate() {
        if (lastHours) return;
        showDate(DateManager.getDateAfterHours(displayedHour, DateManager.getHours(displayedHour) == 20 ? 2:7));
    }

    @Override
    public void prevDate() {
        if (firstHours) return;
        showDate(DateManager.getDateAfterHours(displayedHour, -7));
    }

    @Override
    public void onDateClicked(View v) {
        RowElement rowElement = null;
        for (int i = 0; i < row.getDates().size(); i++) {
            if (row.getDates().get(i).getView() == v) {
                rowElement = row.getDates().get(i);
            }
        }

        if (DateManager.getDifferenceMinutes(minDate, rowElement.getValue()) >= 45) {return;}

        markDate(rowElement.getValue());

    }

    @Override
    public void markDate(Date date) {
        selectedHour = date;
        time.setHours(date);
        if (DateManager.getHours(date) != DateManager.getHours(minDate)) {
            Date minutes = new Date();
            minutes.setTime(0);
            time.setMinutes(minutes);
        } else {
            time.setMinutes(minDate);
        }
        Date d = time.getDate();
        showDate(date);
        datePicker.rows[1].getDateRepresent().markDate(d);
        datePicker.rows[2].getDateRepresent().markDate(d);
    }

    @Override
    public void deleteSelection() {
        selectedHour = null;
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
