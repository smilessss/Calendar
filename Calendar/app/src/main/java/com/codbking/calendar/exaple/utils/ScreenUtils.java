package com.codbking.calendar.exaple.utils;

import android.content.Context;

/**
 * Created by smile on 2019/1/3.
 */

public class ScreenUtils {
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }
    /**
     * 获取屏幕宽度(px)
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}
