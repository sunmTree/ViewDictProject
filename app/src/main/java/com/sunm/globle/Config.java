package com.sunm.globle;

import android.util.Log;

/**
 * Created by sunmeng on 2016/12/19.
 */

public class Config {
    public static final boolean DEBUG = true;

    public static final String TAG = "config_of_all";

    public static void i(String message){
        if (DEBUG){
            Log.i(TAG,message);
        }
    }

    public static void w(String message){
        if (DEBUG){
            Log.w(TAG,message);
        }
    }
}
