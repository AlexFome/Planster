package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.Date;

/**
 * Created by Alex on 16.03.2017.
 */
public class Time {
    private Date date;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minutes;
    private TimePeriod timePeriodManager;
    private int color;

    public Time (Date date, TimePeriod timePeriodManager, int color) {
        this.date = date;
        this.year = DateManager.getYear(date);
        this.month = DateManager.getMonthNum(date);
        this.day = DateManager.getDay(date);
        this.hour = DateManager.getHours(date);
        this.minutes = DateManager.getMinutes(date);
        this.timePeriodManager = timePeriodManager;
        this.color = color;
    }

    public void setTime (Date date) {
        this.date = date;
        this.year = DateManager.getYear(date);
        this.month = DateManager.getMonthNum(date);
        this.day = DateManager.getDay(date);
        this.hour = DateManager.getHours(date);
        this.minutes = DateManager.getMinutes(date);
        timePeriodManager.checkPeriod();
    }

    public void setYear (Date date) {
        this.year = DateManager.getYear(date);
    }

    public void setMonth (Date date) {
        this.month = DateManager.getMonthNum(date);
    }

    public void setDay (Date date) {
        this.day = DateManager.getDay(date);
        timePeriodManager.checkPeriod();
    }

    public void setHours (Date hour) {
        this.hour = DateManager.getHours(hour);
        timePeriodManager.checkPeriod();
    }

    public void setMinutes (Date minutes) {
        this.minutes = DateManager.getMinutes(minutes);
        timePeriodManager.checkPeriod();
    }

    public Date getDate () {
        return DateManager.parseFullDate(year, month, day, hour, minutes, 0);
    }

    public Time getDefaultTime () {
        return timePeriodManager.getDefaultTime();
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

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public TimePeriod getTimePeriodManager() {
        return timePeriodManager;
    }

    public int getColor() {
        return color;
    }
}
