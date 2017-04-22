package com.fome.planster;

/**
 * Created by Alex on 18.04.2017.
 */

public class Settings {

    public static boolean isNotificationSoundEnabled () {
        return SharedPreferencesManager.getBoolean("notification_sound_enabled", true);
    }

    public static void setNotificationSound (boolean value) {
        SharedPreferencesManager.saveBoolean ("notification_sound_enabled", value);
    }

}
