package com.fome.planster;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Alex on 14.03.2017.
 */
public class PermissionManager {

    public static boolean hasPermission(Activity activity, String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void askForPermission(String permission, Integer PERMISSIONS_REQUEST_READ_CONTACTS, Context context, Activity activity) {

        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission)) {

                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }

    }
}
