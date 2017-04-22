package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 20.03.2017.
 */
public class Months {

    int year;
    public ArrayList <Month> months;

    public Months(Date date) {
        year = DateManager.getYear(date);
        months = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Month month = new Month(DateManager.getDateAfterMonths(date, i));
            months.add(month);
        }
    }

    public Month getMonth (Date date) {
        for (int i = 0; i < months.size(); i++) {
            if (months.get(i).num == DateManager.getMonthNum(date)) {
                return months.get(i);
            }
        }
        return null;
    }

}
