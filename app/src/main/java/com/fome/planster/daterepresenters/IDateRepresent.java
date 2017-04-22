package com.fome.planster.daterepresenters;

import android.view.View;

import com.fome.planster.models.Row;
import com.fome.planster.models.Time;
import com.fome.planster.models.Year;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public interface IDateRepresent {
    void setDates (Date minDate, Date selectedDate, Year year, Row row); // Sets date to be shown
    void showDate (Date date); // refresh row elements values
    void nextDate (); // scroll calendar forward
    void prevDate (); // scroll calendar backwards
    void onDateClicked (View v); // selects date
    void markDate (Date date);
    void deleteSelection();
    void setTime (Time time);
    View.OnClickListener getOnRightClickListener ();
    View.OnClickListener getOnLeftClickListener ();
    View.OnClickListener getOnDateClickListener ();
}
