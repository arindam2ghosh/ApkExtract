package com.duongkk.apkextract.Utils;

import android.util.Log;

import com.duongkk.apkextract.BuildConfig;


/**
 * Created by DuongKK on 3/24/2016.
 */
public class LogX {
    public static final String TAG = LogX.class.getSimpleName();
    private static boolean DEBUG = BuildConfig.DEBUG;
    //	private static boolean  DEBUG = true;
    public LogX() {

    }

    public static void d(String msg) {
        if(DEBUG) Log.d(TAG, "-------> " + msg);
    }
    public static void d(String tag, String msg) {
        if(DEBUG) Log.d(tag,"---------> " + msg);
    }

    public static void i(String msg) {
        if(DEBUG) Log.i(TAG, msg);
    }
    public static void i(String tag, String msg) {
        if(DEBUG) Log.i(tag, msg);
    }

    public static void e(String msg) {
        if(DEBUG) Log.e(TAG, msg);
    }
    public static void e(String tag, String msg) {
        if(DEBUG) Log.e(tag, msg);
    }

    public static void v(String msg) {
        if(DEBUG) Log.v(TAG, msg);
    }
    public static void v(String tag, String msg) {
        if(DEBUG) Log.v(tag, msg);
    }

    public static void w(String msg) {
        if(DEBUG) Log.w(TAG, msg);
    }
    public static void w(String tag, String msg) {
        if(DEBUG) Log.w(tag, msg);
    }
    public static void printStackTrace(Exception e){
        if(DEBUG){
            e.printStackTrace();
        }
    }
}
