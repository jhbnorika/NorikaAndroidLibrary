
package com.norika.android.library.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class DevUtil {
    /**
     * 判断是否模拟器
     * 
     * @param context
     * @return
     */
    public static boolean isEmulator(Context context) {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (imei != null && imei.equals("000000000000000"))
                return true;

            return (Build.MODEL.equals("sdk")) || (Build.MODEL.equals("google_sdk"));
        } catch (Exception ioe) {
            return false;
        }
    }
}
