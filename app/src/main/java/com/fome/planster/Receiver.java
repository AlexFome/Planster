package com.fome.planster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.fome.planster.models.Task;
import com.google.gson.Gson;

import java.util.Date;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TasksManager.init(context);
        SharedPreferencesManager.init(context);
        FirebaseAnalyticsManager.init(context);
        AlarmManager.init(context);

        if (intent.getAction().equals(AlarmManager.FILTER)) { // If received message sent by our app
            WakeLocker.acquire(context);
            Gson gson = new Gson();
            Task task = gson.fromJson(intent.getExtras().getString("task"), Task.class);
            if (task.isNotificationEnabled()) {
                NotificationManager.createNotification(context, task.getId(), task.getName(), task.getNotes(), R.mipmap.ic_launcher, null, task.getStartDate().getTime());
                if (!task.isAlertEnabled() && Settings.isNotificationSoundEnabled()) {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                }
            }
            if (task.isAlertEnabled()) { // if task needs to be alerted
                Intent i = new Intent(context, DayActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("alert", gson.toJson(task));
                context.startActivity(i); // We're starting an app and alerting a task
            }

            Date currentDate = new Date();
            if (task.getRepeatType() == TasksManager.RepeatType.WEEKDAYS) { // If task should be repeated every weekday
                for (int i = 0; i < 4; i++) { // check today and next 3 days to find a proper time for a task
                    Date date = DateManager.getDateAfterDays(currentDate, i);
                    if (!DateManager.isWeekend(date)) {
                        if (i > 0) { // if its not today -> set time to 00:00
                            date = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
                        }
                        task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(task.getStartDate()), DateManager.getMinutes(task.getStartDate()), 0));
                        if (task.getStartDate().after(date)) { // if date fits
                            Date startDate = task.getStartDate();
                            task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(startDate), DateManager.getMinutes(startDate), 0));
                            AlarmManager.setAlarm(task);
                            return;
                        }
                    }
                }
            } else if (task.getRepeatType() == TasksManager.RepeatType.WEEKENDS) {
                for (int i = 0; i < 4; i++) {
                    Date date = DateManager.getDateAfterDays(currentDate, i);
                    if (DateManager.isWeekend(date)) {
                        if (i > 0) {
                            date = DateManager.parseDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date));
                        }
                        task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(task.getStartDate()), DateManager.getMinutes(task.getStartDate()), 0));
                        if (task.getStartDate().after(date)) {
                            Date startDate = task.getStartDate();
                            task.setStartDate(DateManager.parseFullDate(DateManager.getYear(date), DateManager.getMonthNum(date), DateManager.getDay(date), DateManager.getHours(startDate), DateManager.getMinutes(startDate), 0));
                            AlarmManager.setAlarm(task);
                            return;
                        }
                    }
                }
            }

        } else if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) { // if it is a reboot signal
            TasksManager.refreshTasksAlarms();
        }
    }
}
