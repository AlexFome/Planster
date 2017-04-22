package com.fome.planster;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Alex on 10.04.2017.
 */
public class NotificationManager {

    public static void createNotification (Context context, int id, String title, String text, int smallIcon, Bitmap bigIcon, long time) {

        Notification notification = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(smallIcon)
                .setLargeIcon(bigIcon)
                .setWhen(time)
                .build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(id, notification);
    }

    public static void removeNotification (Context context, int id) {
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.cancel (id);
    }

}
