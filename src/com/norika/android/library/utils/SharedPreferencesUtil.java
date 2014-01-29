
package com.norika.android.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 保证线程安全，而非进程安全
 * <p>
 * <a href=
 * "http://stackoverflow.com/questions/4693387/sharedpreferences-and-thread-safety"
 * >Safety Thread, not Safety Process</a>
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class SharedPreferencesUtil {
    private static SharedPreferencesUtil instance;
    private static SharedPreferences spDefaultGlobal;
    private static SharedPreferences spGlobal;

    private static final String DEFAULT_SP_NAME = "i_sp_name";

    /**
     * @param context {@link getApplicationContext()}
     */
    private SharedPreferencesUtil(Context context) {
        spGlobal = PreferenceManager.getDefaultSharedPreferences(context);
        spGlobal = context.getSharedPreferences(DEFAULT_SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 当前Activity唯一SharedPreferences
     * 
     * @param activity
     * @param key
     * @param value
     */
    public static boolean saveString(Activity activity, String key, String value) {
        SharedPreferences sp = activity.getPreferences(Context.MODE_PRIVATE);
        return sp.edit().putString(key, value).commit();
    }

    /**
     * 全局SharedPreferences
     * 
     * @param key
     * @param value
     * @return
     */
    public static boolean saveString(String key, String value) {
        return spGlobal.edit().putString(key, value).commit();
    }
}
