package com.fome.planster.daterepresenters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fome.planster.DateManager;
import com.fome.planster.models.Month;
import com.fome.planster.models.Months;
import com.fome.planster.models.Row;
import com.fome.planster.models.RowElement;
import com.fome.planster.models.Time;
import com.fome.planster.models.Week;
import com.fome.planster.models.Year;
import com.fome.planster.R;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public class WeekRepresent extends DateRepresent implements IDateRepresent {

    Month displayedMonth;
    Week selectedWeek;

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
            onDateClicked (v);
        }
    };

    public WeekRepresent (Context context, DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

    @Override
    public void setDates (Date minDate, Date selectedDate, Year year,Row row) {
        this.minDate = minDate;
        this.row = row;
        this.year = year;
        showDate(selectedDate);
        row.getFirstElement().getView().setVisibility(View.VISIBLE);
        row.getLastElement().getView().setVisibility(View.VISIBLE);
    }

    @Override
    public void showDate(Date date) {
        Months months = year.getMonths(date);
        Month month = months.getMonth(date);
        displayedMonth = month;

        for (int i = 0; i < row.getDates().size(); i++) {
            RowElement rowElement = row.getDates().get(i);
            if (i < month.weeks.size()) {
                rowElement.getView().setVisibility(View.VISIBLE);

                Week week = month.weeks.get(i);
                rowElement.setValue(week.getDays().get(0).getDate());
                rowElement.getText().setText(String.valueOf(week.getDays().get(0).getDayNum() + "-" + week.getDays().get(week.getDays().size() - 1).getDayNum()));
                rowElement.disableSelector();

                if (selectedWeek != null && selectedWeek.getDays().get(0).getDate().equals(week.getDays().get(0).getDate()) || selectedWeek == null && week.getDays().get(0).getDate().compareTo(time.getDate()) <= 0 && time.getDate().compareTo(week.getDays().get(week.getDays().size() - 1).getDate()) <= 0) {
                    rowElement.enableSelector(time.getColor());
                    rowElement.getText().setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.white));
                } else if (DateManager.compareDays(displayedMonth.getWeek(rowElement.getValue()).getLastDay().getDate(), minDate) < 0) {
                    rowElement.hideSelector();
                    rowElement.getText().setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.light_grey_2));
                } else {
                    rowElement.hideSelector();
                    rowElement.getText().setTextColor(ContextCompat.getColor(context.getApplicationContext(), R.color.light_grey_3));
                }

            } else {
                rowElement.getView().setVisibility(View.GONE);
            }

            if (displayedMonth.weeks.get(0).getDays().get(0).getDate().after(DateManager.parseDate(DateManager.getYear(minDate), DateManager.getMonthNum(minDate), 1))) {
                row.getFirstElement().getText().setText(context.getResources().getString(R.string.arrow_left));
                row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
            } else {
                row.getFirstElement().getText().setText(context.getResources().getString(R.string.weeks));
                row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.small_text_size) / context.getResources().getDisplayMetrics().density);
            }

        }
    }

    @Override
    public void nextDate() {
        row.getFirstElement().getText().setText (context.getResources().getString(R.string.arrow_left));
        row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        Date nextDate = DateManager.getDateAfterDays(displayedMonth.date, 32);
        if (DateManager.getYear(nextDate) != year.getNum()) {
            datePicker.setDate(minDate, nextDate);
        } else {
            showDate(nextDate);
        }
    }

    @Override
    public void prevDate() {
        if (!displayedMonth.weeks.get(0).getDays().get(0).getDate().after(DateManager.parseDate(DateManager.getYear(minDate), DateManager.getMonthNum(minDate), 1))) return;

        Date prevDate = DateManager.getDateAfterDays(displayedMonth.weeks.get(0).getDays().get(0).getDate(), -7);
        if (DateManager.getYear(prevDate) != year.getNum()) {
            datePicker.setDate(minDate, prevDate);
        } else {
            showDate(prevDate);
        }
    }

    @Override
    public void onDateClicked(View v) {
        RowElement rowElement = null;
        for (int i = 0; i < row.getDates().size(); i++) {
            if (row.getDates().get(i).getView() == v) {
                rowElement = row.getDates().get(i);
            }
        }

        if (DateManager.compareDays(displayedMonth.getWeek(rowElement.getValue()).getLastDay().getDate(), minDate) < 0) {return;}

        datePicker.rows[0].getDateRepresent().showDate(rowElement.getValue());
        datePicker.rows[2].getDateRepresent().markDate(rowElement.getValue());
        datePicker.rows[2].getDateRepresent().showDate(rowElement.getValue());
        markDate(rowElement.getValue());
    }

    @Override
    public void markDate(Date date) {
        Year year = new Year(date);
        Months months = year.getMonths(date);
        displayedMonth = months.getMonth(date);
        selectedWeek = displayedMonth.getWeek(date);
        showDate(date);
    }

    @Override
    public void deleteSelection() {
        selectedWeek = null;
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
