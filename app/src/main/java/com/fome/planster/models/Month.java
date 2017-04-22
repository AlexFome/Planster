package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 18.03.2017.
 */
public class Month {

    public Date date;
    public int year;
    public int num;
    public String name;
    int length;
    public ArrayList<Week> weeks;

    public Month (Date date) {
        this.date = date;
        year = DateManager.getYear(date);
        num = DateManager.getMonthNum(date);
        name = DateManager.getMonthName(date);
    }

    void calculateWeeks () {
        weeks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        length = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Date firstDayOfMonth = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), 1);
        calendar.setTime(firstDayOfMonth);
        int daysInserted = 0;
        int dayOfWeek = DateManager.getClassicDayOfWeek(firstDayOfMonth);
        int firstWeekExtraDays = dayOfWeek - 1;
        Date firstExtraDate = DateManager.getDateAfterDays(firstDayOfMonth, -firstWeekExtraDays);

        Week firstWeek = new Week(0);
        weeks.add(firstWeek);

        for (int i = 0; i < 7; i++) {
            Date d = DateManager.getDateAfterDays(firstExtraDate, i);
            Day day = new Day(
                    d,
                    null,
                    false,
                    i < firstWeekExtraDays
            );
            firstWeek.getDays().add(day);

            daysInserted++;
        }

        int w = 0;
        while (daysInserted < length + firstWeekExtraDays) {
            w++;
            Week week = new Week(w);
            for (int i = 0; i < 7; i++) {
                Date d = DateManager.getDateAfterDays(weeks.get(weeks.size() - 1).getDays().get(weeks.get(weeks.size() - 1).getDays().size() - 1).getDate(), i + 1);
                daysInserted++;
                Day day = new Day(
                        d,
                        null,
                        daysInserted == length,
                        daysInserted > length + firstWeekExtraDays
                );

                week.getDays().add(day);
            }

            for (int i = 0; i < week.getDays().size(); i++) {
                if (week.getDays().get(i).isLastDayOfMonth()) {
                    week.setLastWeekOfMonth(true);
                    break;
                }
            }

            weeks.add(week);
        }
    }

    public Week getWeek (Date date) {
        if (weeks == null) {
            calculateWeeks();
        }

        Week w = null;
        Date d = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
        for (Week week: weeks) {
            for (Day day: week.getDays()) {
                if (day.getDate().equals(d)) {
                    return week;
                }
            }
        }
        return w;
    }

    public ArrayList<Week> getWeeks () {
        if (weeks == null) {
            calculateWeeks();
        }

        return weeks;
    }

    public Date getFirstDay () {
        return DateManager.parseDate(year, num, 1);
    }

    public Date getLastDay () {
        return DateManager.parseDate(year, num, length);
    }

    public ArrayList<Day> getDays () {
        ArrayList<Day> days = new ArrayList<>();
        for (int i = 0; i < getWeeks().size(); i++) {
            for (int d = 0; d < getWeeks().get(i).getDays().size(); d++) {
                Day day = getWeeks().get(i).getDays().get(d);
                if (!day.isAnotherWeek())
                days.add(day);
            }
        }
        return days;
    }

}
