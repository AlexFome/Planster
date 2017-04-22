package com.fome.planster;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by Alex on 15.04.2017.
 */

public class WakeLocker {
    private static PowerManager.WakeLock wakeLock;
    private static String TAG = "wake_lock_tag";

    public static void acquire(Context ctx) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, TAG);
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) wakeLock.release(); wakeLock = null;
    }
}
