package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 14.03.2017.
 */
public class Day {
    private Date date; // Date, which lies in this day
    private int dayNum; // Number of this day in month
    private boolean isWeekend;
    private boolean anotherWeek; // Day belong to another week or month, comparing to certain week
    private Tasks tasks; // tasks in this day
    private boolean lastDayOfMonth;
    private Date zeroHours; // this day at 00:00
    private ArrayList<Hours> hours = new ArrayList<>();

    public Day (Date date, Tasks tasks, boolean lastDayOfMonth, boolean anotherWeek) {
        this.date = date;
        this.tasks = tasks;
        dayNum = DateManager.getDay(date);
        this.lastDayOfMonth = lastDayOfMonth;
        this.anotherWeek = anotherWeek;
        isWeekend = DateManager.isWeekend(date);
    }

    public Hours getHours (Date date) {

        if (hours.size() == 0) {
            calculateHours();
        }

        int hour = DateManager.getHours(date);
        int i;
        if (hour < 7) {
            i = 0;
        } else if (hour >= 7 && hour < 14) {
            i = 1;
        } else if (hour >= 14 && hour < 21) {
            i = 2;
        } else {
            i = 3;
        }

        return hours.get(i);

    }

    public void calculateHours () {
        zeroHours = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
        for (int i = 0; i < 4; i++) {
            hours.add(
                    new Hours(DateManager.getDateAfterHours(zeroHours, i * 7))
            );
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDayNum() {
        return dayNum;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public boolean isAnotherWeek() {
        return anotherWeek;
    }

    public boolean isLastDayOfMonth() {
        return lastDayOfMonth;
    }

    public ArrayList<Hours> getHours() {
        return hours;
    }

}
