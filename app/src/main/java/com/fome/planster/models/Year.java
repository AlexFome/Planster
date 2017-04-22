package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 20.03.2017.
 */
public class Year {

    private int num;
    private ArrayList<Months> months;

    public Year (Date date) {
        num = DateManager.getYear(date);
    }

    public Months getMonths (Date date) {
        if (months == null) {
            months = new ArrayList<>();
            Date q1 = DateManager.parseDate(num, 1, 1);
            Date q2 = DateManager.parseDate(num, 5, 1);
            Date q3 = DateManager.parseDate(num, 9, 1);
            months.add(new Months(q1));
            months.add(new Months(q2));
            months.add(new Months(q3));
        }

        int monthNum = DateManager.getMonthNum(date);
        if (monthNum <= 4) {
            return months.get(0);
        } else if (monthNum > 4 && monthNum <= 8) {
            return months.get(1);
        } else {
            return months.get(2);
        }
    }

    public void calculateDays () {
        months = new ArrayList<>();
        Date q1 = DateManager.parseDate(num, 1, 1);
        Date q2 = DateManager.parseDate(num, 5, 1);
        Date q3 = DateManager.parseDate(num, 9, 1);
        months.add(new Months(q1));
        months.add(new Months(q2));
        months.add(new Months(q3));

        for (int i = 0; i < months.size(); i++) {
            for (int m = 0; m < months.get(i).months.size(); m++) {
                months.get(i).months.get(m).calculateWeeks();
            }
        }

    }

    public int getNum() {
        return num;
    }

    public ArrayList<Months> getMonths() {
        return months;
    }
}
