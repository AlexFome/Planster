package com.fome.planster;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Alex on 12.04.2017.
 */

public class FirebaseAnalyticsManager {

    private static FirebaseAnalytics firebaseAnalytics;
    static Context context;

    public static void init (Context c) {
        context = c;
        if (InternetConnection.isOnline(context))
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public static void logEvent(String id, String name, String content) {
        if (!InternetConnection.isOnline(context)) return;
        if (firebaseAnalytics == null) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT, content);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
