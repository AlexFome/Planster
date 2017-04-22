package com.fome.planster;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.fome.planster.models.Task;
import com.google.gson.Gson;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Alex on 12.04.2017.
 */

public class AlarmManager {

    static String FILTER = "com.fome.planster";
    static android.app.AlarmManager alarmManager;
    static Context context;
    static Gson gson;

    public static void init (Context c) {
        context = c;
        alarmManager = (android.app.AlarmManager) context.getSystemService(ALARM_SERVICE);
        gson = new Gson();
    }

    public static void setAlarm (Task task) {
        Intent intent = new Intent(FILTER);
        intent.putExtra("task", gson.toJson(task));

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP,
                task.getStartDate().getTime() - (task.getNotificateBefore() * 1000 * 60) + 5,
                alarmIntent);
    }

    public static void removeAlarm (Task task) {
        Intent intent = new Intent(FILTER);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(alarmIntent);
    }

}
