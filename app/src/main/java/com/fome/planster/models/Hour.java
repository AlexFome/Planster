package com.fome.planster.models;

import com.fome.planster.DateManager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 24.03.2017.
 */
public class Hour {
    private Date date;
    private ArrayList<Date> minutes = new ArrayList();

    public Hour (Date date) {
        this.date = date;
    }

    public ArrayList<Date> getMinutes () {
        if (minutes.size() == 0) {
            calculateMinutes ();
        }

        return minutes;
    }

    void calculateMinutes () {
        for (int i = 0; i < 4; i++) {
            minutes.add(
                    DateManager.getDateAfterMinutes(date, i * 15)
            );
        }
    }

    public Date getDate() {
        return date;
    }
}
