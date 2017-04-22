package com.fome.planster;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Alex on 14.03.2017.
 */
public class SharedPreferencesManager {

    static String SHARED_PREFERENCES_KEY = "SharedPrefs";
    static SharedPreferences sharedPreferences;
    static Context context;
    static Gson gson;

    public static void init(Context c) {
        context = c;
        gson = new Gson();
        sharedPreferences = c.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static boolean isFirstLaunch () {
        String KEY = "firstLaunch";
        boolean firstLaunch = sharedPreferences.getBoolean(KEY, true);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(KEY, false);
        edit.commit();
        return firstLaunch;
    }

    public static Object getObject (String key, Class c) {
        return gson.fromJson(sharedPreferences.getString(key, null), c);
    }

    public static void saveInt (String key, int num) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, num);
        editor.commit();
    }

    public static int getInt (String key, int min) {
        return sharedPreferences.getInt(key, min);
    }

    public static boolean getBoolean (String key, boolean defaultValue) { return sharedPreferences.getBoolean(key, defaultValue);}

    public static void saveBoolean (String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void saveObject (String key, Object o) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(o));
        editor.commit();
    }

    public static void clearSharedPreferences () {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean contains (String key) {
        return sharedPreferences.contains(key);
    }

}
