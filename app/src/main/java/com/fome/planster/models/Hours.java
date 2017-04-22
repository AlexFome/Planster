package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 24.03.2017.
 */
public class Hours {
    private ArrayList<Hour> hours = new ArrayList<>();
    private int year;
    private int month;
    private int day;
    private Date firstHour;
    private Date lastHour;

    public Hours (Date date) {

        firstHour = date;

        year = DateManager.getYear(date);
        month = DateManager.getMonthNum(date);
        day = DateManager.getDay(date);

        for (int i = 0; i < 7; i++) {
            Date hour = DateManager.getDateAfterHours(date, i);
            if (DateManager.getHours(hour) < DateManager.getHours(firstHour)) {
                break;
            }
            hours.add(new Hour(hour));
            lastHour = hour;
        }
    }

    public ArrayList<Hour> getHours() {
        return hours;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public Date getFirstHour() {
        return firstHour;
    }

    public Date getLastHour() {
        return lastHour;
    }
}
