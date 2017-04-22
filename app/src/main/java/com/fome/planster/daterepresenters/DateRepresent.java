package com.fome.planster.daterepresenters;

import android.content.Context;

import com.fome.planster.models.Row;
import com.fome.planster.models.Time;
import com.fome.planster.models.Year;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */

/*Parent class of date representers*/
public class DateRepresent {
    protected Date minDate; // Margin date. Dates before it can't be selected
    protected Row row; // Calendar row, which would be controlled by this object
    protected Context context;
    protected Time time; // Time object represents which date we are setting (Start date or end date)
    protected Year year; // Year, which would be displayed
    protected DatePicker datePicker; // Refference to a date picker
}
