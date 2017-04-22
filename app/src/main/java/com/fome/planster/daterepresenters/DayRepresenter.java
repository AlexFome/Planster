package com.fome.planster.daterepresenters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.fome.planster.DateManager;
import com.fome.planster.models.Day;
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
public class DayRepresenter extends DateRepresent implements IDateRepresent {

    Date minDate;
    Week displayedWeek; // Day representer displays 7 days

    public DayRepresenter (Context context, DatePicker datePicker) {
        this.context = context;
        this.datePicker = datePicker;
    }

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

    @Override
    public void setTime (Time time) {
        this.time = time;
    }

    @Override
    public void setDates(Date minDate, Date selectedDate, Year year, Row row) {
        this.year = year;
        this.minDate = minDate;
        this.row = row;
        showDate(selectedDate);
    }

    @Override
    public void showDate(Date date) {

        Months months = year.getMonths(date); // getting an array of quarters
        Month month = months.getMonth(date); // getting an array of monthes in a quarter
        Week week = month.getWeek(date); // getting a proper week
        displayedWeek = week;

        for (int i = 0; i < row.getDates().size(); i++) { // refreshing row elements values
            RowElement rowElement = row.getDates().get(i);
            rowElement.setValue(week.getDays().get(i).getDate());
            rowElement.disableSelector();

            Day day = week.getDays().get(i); // A day to be shown

            if (DateManager.parseDate(time.getYear(), time.getMonth(), time.getDay()).equals(DateManager.parseDate(DateManager.getYear(rowElement.getValue()), DateManager.getMonthNum(rowElement.getValue()), DateManager.getDay(rowElement.getValue())))) {
                // if it is a selected day
                rowElement.enableSelector(time.getColor());
                rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                // if it is not a selected day
                rowElement.hideSelector();
                if (day.isAnotherWeek() || DateManager.compareDays(rowElement.getValue(), minDate) < 0) { // if it is a day before margin date or another month's day
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_2));
                } else if (day.isWeekend()) { // if it is a weekend
                    rowElement.getText().setTextColor(time.getColor());
                } else { // if it is a regular day
                    rowElement.getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
                }
            }

            rowElement.getText().setText( // refresh value
                    String.valueOf(DateManager.getDay(rowElement.getValue()))
            );
        }

        if (week.getDays().get(0).getDate().after(minDate)) { // if calendar can be scrolled backwards
            row.getFirstElement().getText().setText(context.getString(R.string.arrow_left));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        } else {
            row.getFirstElement().getText().setTextColor(ContextCompat.getColor(context, R.color.light_grey_3));
            row.getFirstElement().getText().setText(context.getResources().getString(R.string.days));
            row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.small_text_size) / context.getResources().getDisplayMetrics().density);
        }

    }

    @Override
    public void nextDate() {
        row.getFirstElement().getText().setText (context.getResources().getString(R.string.arrow_left));
        row.getFirstElement().getText().setTextSize (context.getResources().getDimension(R.dimen.middle_text_size) / context.getResources().getDisplayMetrics().density);
        Date nextDate = DateManager.getDateAfterDays(displayedWeek.getDays().get(0).getDate(), 7); // Next date to be displayed
        if (DateManager.getYear(nextDate) != year.getNum()) { // if it is a next year
            datePicker.setDate(minDate, nextDate);
        } else {
            showDate(nextDate);
        }

        /*refreshing other calendar rows*/
        datePicker.rows[1].getDateRepresent().markDate(nextDate);
        datePicker.rows[2].getDateRepresent().markDate(nextDate);
        datePicker.rows[1].getDateRepresent().showDate(nextDate);
        datePicker.rows[2].getDateRepresent().showDate(nextDate);
    }

    @Override
    public void prevDate() {
        if (!displayedWeek.getDays().get(0).getDate().after(minDate)) return; // return if calendar can't be scrolled backwards

        Date prevDate = DateManager.getDateAfterDays(displayedWeek.getDays().get(0).getDate(), -1); // date to be shown
        if (DateManager.getYear(prevDate) != year.getNum()) { // if it is a previous year
            datePicker.setDate(minDate, prevDate);
        } else {
            showDate(prevDate);
        }

        /*refreshing other calendar rows*/
        datePicker.rows[1].getDateRepresent().markDate(prevDate);
        datePicker.rows[2].getDateRepresent().markDate(prevDate);
        datePicker.rows[1].getDateRepresent().showDate(prevDate);
        datePicker.rows[2].getDateRepresent().showDate(prevDate);
    }

    @Override
    public void onDateClicked(View v) {
        RowElement rowElement = null;
        /*looking for a row element object, whose view was pressed*/
        for (int i = 0; i < row.getDates().size(); i++) {
            if (row.getDates().get(i).getView() == v) {
                rowElement = row.getDates().get(i); // found
                break;
            }
        }

        if (DateManager.compareDays(rowElement.getValue(), minDate) < 0) {return;} // return if this date can't be selected

        // marking this row and refreshing others
        markDate(rowElement.getValue());
        datePicker.rows[1].getDateRepresent().markDate(rowElement.getValue());
        datePicker.rows[2].getDateRepresent().markDate(rowElement.getValue());

    }

    @Override
    public void markDate(Date date) {
        time.setYear(date);
        time.setMonth(date);
        time.setDay(date);

        if (DateManager.compareDays(date, minDate) == 0) {
            time.setHours(minDate);
            time.setMinutes(minDate);
        } else { // if selected day is not today - set default time for a task (13:00)
            time.setHours(time.getDefaultTime().getDate());
            time.setMinutes(time.getDefaultTime().getDate());
        }

        showDate(date);
    }

    @Override
    public void deleteSelection() {}

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
