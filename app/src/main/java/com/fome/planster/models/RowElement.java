package com.fome.planster.models;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator;
import com.fome.planster.R;
import com.fome.planster.UIManager;

import java.util.Date;

/**
 * Created by Alex on 23.03.2017.
 */
public class RowElement {
    private View view; // Wrapping layout
    private Date value; // Displayed value
    private View selector; // View, which marks element as selected
    private TextView text;

    private int height; // selectors height
    private int width; // selectors width

    public RowElement(Context context, View view, Date value, View selector, TextView text) {
        this.view = view;
        this.value = value;
        this.selector = selector;
        this.text = text;
        height = (int) (context.getResources().getDimension(R.dimen.calendar_circle_size) / context.getResources().getDisplayMetrics().density);
        width = (int) (context.getResources().getDimension(R.dimen.calendar_circle_size) / context.getResources().getDisplayMetrics().density);
    }

    public void disableSelector () {
        ViewPropertyObjectAnimator.animate(selector).width(0).setDuration(300).start();
        ViewPropertyObjectAnimator.animate(selector).height(0).setDuration(300).start();
    }

    public void enableSelector (int color) {
        UIManager.setBackgroundShapeColor(selector.getBackground(), color);
        ViewPropertyObjectAnimator.animate(selector).width(width).setDuration(300).start();
        ViewPropertyObjectAnimator.animate(selector).height(height).setDuration(300).start();
    }

    public void hideSelector () {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) selector.getLayoutParams();
        layoutParams.height = 0;
        layoutParams.width = 0;
        selector.setLayoutParams(layoutParams);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    public TextView getText() {
        return text;
    }

    public void setText(TextView text) {
        this.text = text;
    }
}
