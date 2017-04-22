package com.fome.planster.models;

import java.util.ArrayList;

/**
 * Created by Alex on 30.03.2017.
 */
public class List {

    private ArrayList<ListItem> items;

    public List () {
        initEmptySpaces(); // Creates 4 empty list items for user to fill in with text
    }

    public boolean isEmpty () { // Checks if list has no information
        boolean isEmpty = true;
        for (int i = 0; i < items.size(); i++) {
            if (getItem(i).getText() != null && !getItem(i).getText().equals("")) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public int getSize () {
        return items.size();
    }

    public ListItem getItem (int num) {
        return items.get(num);
    }

    public void setItemText (int num, String text) {
        items.get(num).setText (text);
    }

    public void addItem (ListItem newListItem) {
        items.add(newListItem);
    }

    public void removeItem (int num) {
        items.remove(num);
    }

    public void format () {
        for (int i = 0; i < items.size(); i++) {
            if (getItem(i).getText().equals("")) {
                removeItem(i);
                i--;
            }
        }
    }

    public void initEmptySpaces () {
        items = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            items.add(new ListItem());
        }
    }

}
