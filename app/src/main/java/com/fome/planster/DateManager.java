package com.fome.planster;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 14.03.2017.
 */
public class DateManager {

    static Context context;

    static android.text.format.DateFormat df = new android.text.format.DateFormat();
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    static SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy");
    static SimpleDateFormat monthDateFormat = new SimpleDateFormat("MM");
    static SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd");
    static SimpleDateFormat dayNameDateFormat = new SimpleDateFormat("EEE");
    static SimpleDateFormat hourDateFormat = new SimpleDateFormat("HH");
    static SimpleDateFormat minutesDateFormat = new SimpleDateFormat("mm");

    public static void init (Context c) {
        context = c;
    }

    public static String formatDate (Date date, String format) {
        return df.format(format, date).toString();
    }

    public static int getYear (Date date) {
        return Integer.valueOf(yearDateFormat.format(date));
    }

    public static int getMonthNum(Date date) {
        return Integer.valueOf(monthDateFormat.format(date));
    }

    public static String getMonthName (Date date) {
        String name = "";
        switch (getMonthNum(date)) {
            case 1:
                name = context.getResources().getString(R.string.jan);
                break;
            case 2:
                name = context.getResources().getString(R.string.feb);
                break;
            case 3:
                name = context.getResources().getString(R.string.mar);
                break;
            case 4:
                name = context.getResources().getString(R.string.apr);
                break;
            case 5:
                name = context.getResources().getString(R.string.may);
                break;
            case 6:
                name = context.getResources().getString(R.string.jun);
                break;
            case 7:
                name = context.getResources().getString(R.string.jul);
                break;
            case 8:
                name = context.getResources().getString(R.string.aug);
                break;
            case 9:
                name = context.getResources().getString(R.string.sep);
                break;
            case 10:
                name = context.getResources().getString(R.string.oct);
                break;
            case 11:
                name = context.getResources().getString(R.string.nov);
                break;
            case 12:
                name = context.getResources().getString(R.string.dec);
                break;
        }
        return name;
    }

    public String getDayName (Date date) {
        return dayNameDateFormat.format(date);
    }

    public static int getDay (Date date) {
        return Integer.valueOf(dayDateFormat.format(date));
    }

    public static int getHours (Date date) {
        return Integer.valueOf(hourDateFormat.format(date));
    }

    public static int getMinutes (Date date) {
        return Integer.valueOf(minutesDateFormat.format(date));
    }

    public static Date getDateAfterMonths (Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add (Calendar.MONTH, months);
        return calendar.getTime();
    }

    public static Date getDateAfterDays (Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add (Calendar.DAY_OF_YEAR, days);
        return calendar.getTime();
    }

    public static Date getDateAfterHours (Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add (Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date getDateAfterMinutes (Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add (Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date parseDate (int year, int month, int day) {
        try {
            String date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
            return fullDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return true;
        } else {
            return false;
        }
    }

    public static int getClassicDayOfWeek (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 1:
                return 7;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            default:
                return 1;
        }
    }

    public static int compareMonths (Date date1, Date date2) {
        date1 = DateManager.parseDate(DateManager.getYear(date1), DateManager.getMonthNum(date1), 1);
        date2 = DateManager.parseDate(DateManager.getYear(date2), DateManager.getMonthNum(date2), 1);
        return date1.compareTo(date2);
    }

    public static int compareDays (Date date1, Date date2) {
        date1 = DateManager.parseDate(DateManager.getYear(date1), DateManager.getMonthNum(date1), DateManager.getDay(date1));
        date2 = DateManager.parseDate(DateManager.getYear(date2), DateManager.getMonthNum(date2), DateManager.getDay(date2));
        return date1.compareTo(date2);
    }

    public static int compareHours (Date date1, Date date2) {
        int d1 = DateManager.getHours(date1);
        int d2 = DateManager.getHours(date2);
        int result = d1 - d2;
        if (result == 0) {
            return 0;
        } else if (result > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public static Date parseFullDate (int year, int month, int day, int hour, int minutes, int seconds) {
        String date = year + "-" + month + "-" + day + " " + hour + ":" + minutes + ":" + seconds;
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getDifferenceMinutes (Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60));
    }

    public static int getDifferenceHours (Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60));
    }

    public static ArrayList<Date> getDaysBetweenDates(Date startDate, Date endDate)
    {
        ArrayList<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate))
        {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        dates.add(endDate);
        return dates;
    }

}
