package com.fome.planster.models;

import java.util.ArrayList;

/**
 * Created by Alex on 18.03.2017.
 */
public class Week {
    private int weekNum;
    private ArrayList<Day> days;
    private boolean lastWeekOfMonth;

    public Week (int num) {
        weekNum = num;
        days = new ArrayList<>();
    }

    public Day getFirstDay () {
        return days.get(0);
    }

    public Day getLastDay () {
        return days.get(days.size() - 1);
    }

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public boolean isLastWeekOfMonth() {
        return lastWeekOfMonth;
    }

    public void setLastWeekOfMonth(boolean lastWeekOfMonth) {
        this.lastWeekOfMonth = lastWeekOfMonth;
    }
}
