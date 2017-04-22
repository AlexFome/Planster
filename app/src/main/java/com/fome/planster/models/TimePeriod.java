package com.fome.planster.models;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.fome.planster.DateManager;
import com.fome.planster.R;
import com.fome.planster.SharedPreferencesManager;

import java.util.Date;

/**
 * Created by Alex on 25.03.2017.
 */
public class TimePeriod {
    private Time startTime;
    private Time endTime;
    private Time defaultTime;

    public TimePeriod(Context context) {

        int m = DateManager.getMinutes(getActualTime());
        int dif;

        if (m < 15) {
            dif = 15 - m;
        } else if (m >= 15 && m < 30) {
            dif = 30 - m;
        } else if (m >= 30 && m < 45) {
            dif = 45 - m;
        } else {
            dif = 60 - m;
        }
        Date startDate = DateManager.getDateAfterMinutes(getActualTime(), dif);

        startTime = new Time(startDate, this, ContextCompat.getColor(context, R.color.red));
        endTime = new Time(DateManager.getDateAfterMinutes(startTime.getDate(), 15), this, ContextCompat.getColor(context, R.color.blue));

        Date dt = (Date) SharedPreferencesManager.getObject("defaultTime", Date.class);
        defaultTime = new Time(dt != null ? dt : DateManager.parseFullDate(0, 0, 0, 13, 0, 0), this, ContextCompat.getColor(context, R.color.red));
    }

    public void checkPeriod () {
        if (this.endTime.getDate().compareTo(this.startTime.getDate()) < 0) {
            this.endTime.setTime(DateManager.getDateAfterMinutes(this.startTime.getDate(), 15));
        }
    }

    public Date getActualTime () {
        return new Date ();
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Time getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(Time defaultTime) {
        this.defaultTime = defaultTime;
    }
}
