
package com.norika.android.library.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;

import com.norika.android.library.BuildConfig;

public class DebugUtil {
    private static final String defaltTag = DebugUtil.class.getName();

    private final static int DEBUG_SIGNATURE_HASH = -545290802;

    private DebugUtil() {

    }

    /**
     * ADT17以前可使用，要先初始化
     * 
     * @hide
     * @param context 建议Application启动调用
     * @return
     */
    public boolean isDebugBuild(Context context) {
        try {
            Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;
            for (int i = 0; i < sigs.length; i++) {
                Log.v(defaltTag, "sign[" + i + "] hash: " + sigs[i].hashCode());
                if (sigs[i].hashCode() == DEBUG_SIGNATURE_HASH) {
                    Log.d(defaltTag, "This is a debug build!");
                    return true;
                }
                Log.v("norika", " hashCode = " + sigs[i].hashCode());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void d(String text) {
        if (BuildConfig.DEBUG)
            Log.d(defaltTag, text);
    }

    public static void w(String text) {
        if (BuildConfig.DEBUG)
            Log.w(defaltTag, text);
    }

    public static void v(String text) {
        if (BuildConfig.DEBUG)
            Log.v(defaltTag, text);
    }

    public static void i(String text) {
        if (BuildConfig.DEBUG)
            Log.i(defaltTag, text);
    }

    public static void e(String text) {
        if (BuildConfig.DEBUG)
            Log.e(defaltTag, text);
    }

    public static void d(String tag, String text) {
        if (BuildConfig.DEBUG)
            Log.d(tag, text);
    }

    public static void v(String tag, String text) {
        if (BuildConfig.DEBUG)
            Log.v(tag, text);
    }

    public static void i(String tag, String text) {
        if (BuildConfig.DEBUG)
            Log.i(tag, text);
    }

    public static void e(String tag, String text) {
        if (BuildConfig.DEBUG)
            Log.e(tag, text);
    }

    public static void w(String tag, String text) {
        if (BuildConfig.DEBUG)
            Log.w(tag, text);
    }

}
