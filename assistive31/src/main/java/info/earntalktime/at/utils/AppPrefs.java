package info.earntalktime.at.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPrefs {

    private SharedPreferences preferences = null;
    ///i choihc
    private static AppPrefs instance = null;

    private AppPrefs(Context ctx) {
        preferences = ctx.getSharedPreferences(SharedPreferencesName.TAG_ETT, Context.MODE_PRIVATE);
    }

    public static AppPrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new AppPrefs(ctx);
        }
        return instance;
    }

    public void commitIntValue(String key, int value) {
        Editor editor = preferences.edit();
        editor.putInt(key, value).commit();
        editor.commit();
    }

    public int getIntValue(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public void commitStringValue(String key, String value) {
        Editor editor = preferences.edit();
        editor.putString(key, value).commit();
        editor.commit();
    }

    public String getStringValue(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void commitBooleanValue(String key, Boolean value) {
        Editor editor = preferences.edit();
        editor.putBoolean(key, value).commit();
        editor.commit();
    }

    public Boolean getBooleanValue(String key, Boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

}