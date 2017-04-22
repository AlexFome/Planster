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
import com.fome.planster.models.Year;
import com.fome.planster.R;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public class MonthRepresent extends DateRepresent implements IDateRepresent {

    Months displayedMonths;
    Month selectedMonth;

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

    public MonthRepresent (Context context,DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

    @Override
    public void setDates(Date minDate, Date selectedDate, Year year, Row row) {
        this.minDate = minDate;
        this.row = row;
        this.year = year;

        row.getFirstElement().getView().setVisibility(View.VISIBLE);
        row.getLastElement().getView().setVisibility(View.VISIBLE);
        showDate(selectedDate);
    }

    @Override
    public void showDate(Date date) {

        Months months = year.getMonths(date);
        displayedMonths = months;

        for (int i = 0; i < row.getDates().size(); i++) {
            RowElement rowElement = row.getDates().get(i);
            Month month = months.months.get(i);
            rowElement.setValue(month.date);
            rowElement.disableSelector();
            rowElement.getText().setText(String.valueOf(months.months.get(i).name));

            if (selectedMonth == null && month.num == time.getMonth() && month.year == time.getYear() || selectedMonth != null && selectedMonth.num == month.num && month.year == selectedMonth.year) {
                rowElement.enableSelector(time.getColor());
                rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.white));
            } else if (DateManager.compareMonths(rowElement.getValue(), minDate) < 0) {
                rowElement.hideSelector();
                rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
            } else {
                rowElement.hideSelector();
                rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
            }

        }

        if (months.months.get(0).getFirstDay().after(minDate)) {
            row.getFirstElement().getText().setText(context.getResources().getString(R.string.arrow_left));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        } else {
            row.getFirstElement().getText().setText(context.getResources().getString(R.string.months));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.small_text_size) / context.getResources().getDisplayMetrics().density);
        }

    }

    @Override
    public void nextDate() {
        row.getFirstElement().getText().setText (context.getResources().getString(R.string.arrow_left));
        row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);

        Date nextDate = DateManager.getDateAfterMonths(displayedMonths.months.get(displayedMonths.months.size() - 1).getLastDay(), 1);
        if (DateManager.getYear(nextDate) != year.getNum()) {
            datePicker.setDate(minDate, nextDate);
        } else {
            showDate(nextDate);
        }
    }

    @Override
    public void prevDate() {
        if (!displayedMonths.months.get(0).getFirstDay().after(minDate)) return;

        Date prevDate = DateManager.getDateAfterDays(displayedMonths.months.get(0).getFirstDay(), -1);
        if (DateManager.getYear(prevDate) != year.getNum()) {
            datePicker.setDate(minDate, prevDate);
        } else {
            showDate(prevDate);
        }

        showDate(prevDate);
    }

    @Override
    public void onDateClicked(View v) {
        RowElement rowElement = null;
        for (int i = 0; i < row.getDates().size(); i++) {
            if (row.getDates().get(i).getView() == v) {
                rowElement = row.getDates().get(i);
            }
        }

        if (DateManager.compareMonths(rowElement.getValue(), minDate) < 0) {return;}

        datePicker.rows[1].getDateRepresent().markDate(rowElement.getValue());
        datePicker.rows[0].getDateRepresent().showDate(rowElement.getValue());
        datePicker.rows[1].getDateRepresent().showDate(rowElement.getValue());
        markDate(rowElement.getValue());
    }

    @Override
    public void markDate(Date date) {
        selectedMonth = new Month(date);
        showDate(date);
    }

    @Override
    public void deleteSelection() {
        selectedMonth = null;
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
