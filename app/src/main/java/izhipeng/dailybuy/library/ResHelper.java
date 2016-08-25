package izhipeng.dailybuy.library;

import android.graphics.drawable.Drawable;


import izhipeng.dailybuy.DailyBuyApplication;


public class ResHelper {

    public static String getString(int id) {
        return DailyBuyApplication.get().getResources().getString(id);
    }

    public static String getString(int id, Object... formatArgs) {
        return DailyBuyApplication.get().getString(id, formatArgs);
    }

    public static int getColor(int id) {
        return DailyBuyApplication.get().getResources().getColor(id);
    }

    public static Drawable getDrawable(int id) {
        return DailyBuyApplication.get().getResources().getDrawable(id);
    }

}
