package com.fome.planster.models;

import android.widget.LinearLayout;

import com.fome.planster.daterepresenters.IDateRepresent;

import java.util.ArrayList;

/**
 * Created by Alex on 23.03.2017.
 */
public class Row {
    private LinearLayout row; // container layout
    private RowElement firstElement; // first element in a row
    private ArrayList<RowElement> dates = new ArrayList<>(); // row elements array
    private RowElement lastElement; // last element in a row
    private IDateRepresent dateRepresent; // Date representer, which controls row elements' values

    public void setDateRepresenter (IDateRepresent dateRepresent) {
        this.dateRepresent = dateRepresent;
        firstElement.getView().setOnClickListener(dateRepresent.getOnLeftClickListener());
        lastElement.getView().setOnClickListener(dateRepresent.getOnRightClickListener());
        for (int i = 0; i < dates.size(); i++) {
            dates.get(i).getView().setOnClickListener(dateRepresent.getOnDateClickListener());
        }
    }

    public LinearLayout getRow() {
        return row;
    }

    public void setRow(LinearLayout row) {
        this.row = row;
    }

    public RowElement getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(RowElement firstElement) {
        this.firstElement = firstElement;
    }

    public ArrayList<RowElement> getDates() {
        return dates;
    }

    public void setDates(ArrayList<RowElement> dates) {
        this.dates = dates;
    }

    public RowElement getLastElement() {
        return lastElement;
    }

    public void setLastElement(RowElement lastElement) {
        this.lastElement = lastElement;
    }

    public IDateRepresent getDateRepresent() {
        return dateRepresent;
    }

    public void setDateRepresent(IDateRepresent dateRepresent) {
        this.dateRepresent = dateRepresent;
    }
}
