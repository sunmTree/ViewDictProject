package com.sunm.utils;

import android.content.Context;

/**
 * Created by sunmeng on 2016/12/13.
 */

public class CommonUtils {

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

}
