package com.fome.planster.models;

/**
 * Created by Alex on 30.03.2017.
 */
public class ListItem {

    private String text;

    public ListItem () {
        text = "";
    }

    public String getText() {
        return text;
    }

    public void setText (String text) {
        this.text = text;
    }

}
