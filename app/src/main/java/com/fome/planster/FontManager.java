package com.fome.planster;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Alex on 14.03.2017.
 */
public class FontManager {

    public static final String ROOT = "fonts/";
    public static final String ICONFONT = ROOT + "fontawesome-webfont.ttf";
    public static final String BOLDFONT = ROOT + "DroidSans-Bold.ttf";
    public static final String LIGHTFONT = ROOT + "DroidSans.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    public static void setFont (Context context, View view, String font) { // Goes through all view's children and applies font to textviews and edittexts
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setFont(context, child, font);
            }
        } else if (view instanceof TextView || view instanceof EditText) {
            ((TextView) view).setTypeface(getTypeface(context, font));
        }
    }

}
