
package com.norika.android.library.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Log;

import com.norika.android.library.BuildConfig;

public class DebugUtil {
    private static final String DEFAULT_TAG = DebugUtil.class.getSimpleName();

    private final int DEBUG_SIGNATURE_HASH = -545290802;
    private final int ONLINE_SIGNATURE_HASH = -972500024;
    /** 签名是否合法（包含DEBUG和线上版） */
    private boolean isAllowedKey = false;

    private boolean isDebug = false;

    private static final boolean DEBUG = Log.isLoggable(DEFAULT_TAG, Log.VERBOSE);

    private DebugUtil() {

    }

    /**
     * ADT17以前可使用，要先初始化
     * 
     * @hide
     * @param context 建议Application启动调用
     * @return
     */
    public void debugAccess(Context context) {
        try {
            Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES).signatures;
            int i = 0;
            for (Signature sig : sigs) {
                Log.v(DEFAULT_TAG, "sign[" + i++ + "] hash: " + sig.hashCode());
                if (sig.hashCode() == DEBUG_SIGNATURE_HASH) {
                    Log.d(DEFAULT_TAG, "This is a debug build!");
                    isDebug = true;
                    isAllowedKey = true;
                    continue;
                }

                if (sig.hashCode() == ONLINE_SIGNATURE_HASH) {
                    isAllowedKey = true;
                    continue;
                }
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public boolean isAllowedKey() {
        return isAllowedKey;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public static void d(String text) {
        if (BuildConfig.DEBUG)
            Log.d(DEFAULT_TAG, text);
    }

    public static void w(String text) {
        if (BuildConfig.DEBUG)
            Log.w(DEFAULT_TAG, text);
    }

    public static void v(String text) {
        if (BuildConfig.DEBUG)
            Log.v(DEFAULT_TAG, text);
    }

    public static void i(String text) {
        if (BuildConfig.DEBUG)
            Log.i(DEFAULT_TAG, text);
    }

    public static void e(String text) {
        if (BuildConfig.DEBUG)
            Log.e(DEFAULT_TAG, text);
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

    public static void iv(String tag, String text) {
        if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE))
            Log.v(tag, text);
    }

}
