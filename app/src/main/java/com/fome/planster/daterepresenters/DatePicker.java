package com.fome.planster.daterepresenters;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fome.planster.FontManager;
import com.fome.planster.models.Row;
import com.fome.planster.models.RowElement;
import com.fome.planster.models.Time;
import com.fome.planster.models.Year;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public class DatePicker {

    public Row[] rows; // Date rows

    Year year; // A year, which would be displayed

    IDateRepresent dayRepresent; // Days representing object
    IDateRepresent weekRepresent; // Weeks representing object
    IDateRepresent monthsRepresent; // Months representing object
    IDateRepresent hourRepresent; // Hour representing object
    IDateRepresent hoursRepresent; // Hours representing object
    IDateRepresent minutesRepresent; // Minutes representing object

    public DatePicker(LinearLayout[] rowsLayouts, Context context) {
        rows = new Row[rowsLayouts.length]; // initializing Row array
        for (int i = 0; i < rowsLayouts.length; i++) { // creating tow objects and setting layout references
            rows[i] = new Row(); // initializing new Row object
            Row r = rows[i];
            r.setRow(rowsLayouts[i]); // setting reference to layout container
            for (int e = 0; e < r.getRow().getChildCount(); e++) { // initializing row elements' objects and setting reference to layouts
                RelativeLayout layout = (RelativeLayout) r.getRow().getChildAt(e);
                r.getDates().add(new RowElement(
                        context,
                        layout,
                        new Date(),
                        layout.getChildCount() > 1 ? layout.getChildAt(0) : null,
                        layout.getChildCount() > 1 ? (TextView) layout.getChildAt(1) : (TextView) layout.getChildAt(0)
                ));
            }
            r.setFirstElement(r.getDates().get (0));
            r.setLastElement(r.getDates().get (r.getDates().size() - 1));
            r.getLastElement().getText().setTypeface(FontManager.getTypeface(context, FontManager.ICONFONT));
            // Removing first and last row elements from array as it's not dates, but arrow buttons
            r.getDates().remove(r.getDates().size() - 1);
            r.getDates().remove(0);
        }
        rows[2].getFirstElement().getText().setTypeface(FontManager.getTypeface(context, FontManager.ICONFONT));

        // Initializing date representers
        dayRepresent = new DayRepresenter(context, this);
        weekRepresent = new WeekRepresent(context, this);
        monthsRepresent = new MonthRepresent(context, this);
        hourRepresent = new HourRepresent(context, this);
        hoursRepresent = new HoursRepresent(context, this);
        minutesRepresent = new MinutesRepresent(context, this);

    }

/*  setting date to be shown at all rows.
    minDate - margin date, dates before it can't be selected.
    selectedDate - date, which would be marked as selected   */
    public void setDate (Date minDate, Date selectedDate) {

        year = new Year(selectedDate);
        year.calculateDays();

        for (int i = 0; i < rows.length; i++) {
            rows[i].getDateRepresent().setDates(minDate, selectedDate, year, rows[i]);
        }
    }

    /*called when user switches calendar to date pick.*/
    public void switchToDate (Time time) {
        rows[0].setDateRepresenter(dayRepresent);
        rows[1].setDateRepresenter(weekRepresent);
        rows[2].setDateRepresenter(monthsRepresent);

        for (int i = 0; i < rows.length; i++) {
            rows[i].getDateRepresent().setTime(time);
        }
    }

    /*called when user switches calendar to time pick*/
    public void switchToTime (Time time) {
        rows[0].setDateRepresenter(hourRepresent);
        rows[1].setDateRepresenter(hoursRepresent);
        rows[2].setDateRepresenter(minutesRepresent);

        for (int i = 0; i < rows.length; i++) {
            rows[i].getDateRepresent().setTime(time);
        }
    }

    public void deleteSelections () {
        for (int i = 0; i < rows.length; i++) {
            rows[i].getDateRepresent().deleteSelection();
        }
    }

}
