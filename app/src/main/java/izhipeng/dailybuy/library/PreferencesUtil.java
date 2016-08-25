package izhipeng.dailybuy.library;

import android.content.SharedPreferences;

import izhipeng.dailybuy.DailyBuyApplication;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class PreferencesUtil {

    public static <T> void putPreferences(String key, T value) {
        SharedPreferences.Editor editor = DailyBuyApplication.preferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> void put(String key, T value) {
        SharedPreferences.Editor editor = DailyBuyApplication.preferences.edit();
        if (value instanceof String) {
            editor.putString(key, value.toString());
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            editor.putInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            editor.putFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            editor.putLong(key, ((Long) value).longValue());
        }
        editor.commit();
    }

    public static <T> T get(String key, T value) {
        Object o = null;
        if (value instanceof String) {
            o = DailyBuyApplication.preferences.getString(key, value.toString());
        } else if (value instanceof Boolean) {
            o = DailyBuyApplication.preferences.getBoolean(key, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            o = DailyBuyApplication.preferences.getInt(key, ((Integer) value).intValue());
        } else if (value instanceof Float) {
            o = DailyBuyApplication.preferences.getFloat(key, ((Float) value).floatValue());
        } else if (value instanceof Long) {
            o = DailyBuyApplication.preferences.getLong(key, ((Long) value).longValue());
        }
        T t = (T) o;
        return t;
    }
}
