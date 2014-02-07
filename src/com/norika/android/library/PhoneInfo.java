
package com.norika.android.library;

import java.lang.reflect.Method;
import java.util.UUID;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.norika.android.library.utils.ITextUtil;
import com.norika.android.library.utils.MD5Util;
import com.norika.android.library.utils.SharedPreferencesUtil;

public class PhoneInfo {

    /** APP名字 */
    public static String sAppName;
    /** APP版本 */
    public static String sAppVer;
    /** APP内部版本 */
    public static int sVersionCode;
    /** APP平台 */
    public static final String sAppPlatform = "android";
    /** 安装包渠道号 */
    public static String sAppPM;

    /** UMeng key */
    public static String sUmengKey;

    /**
     * 设备ID <br>
     * 需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String sDeviceID;
    /** MD5化MACID */
    public static String sMD5MACID;
    /** AndroidId */
    public static String sAndroidID;

    /** 设备机型 */
    public static String sModel;
    /** OS版本 */
    public static String sOSVer;
    /** 系统ro.build.description */
    public static String sOSDesc;

    /**
     * UUID APP安装一次重新生成个id <br>
     * 已经安装的使用线程从SharedPreferences中读取，uuid默认为null。
     * PhoneInfo.init()后大概100ms不到后会设置该值
     */
    public static String sUuid;

    /**
     * 用于唯一标识设备的编号，介于IMEI、MAC地址、AndroidId均有可能获取不到，这里统一逻辑最大程度上获取唯一标示手机的编码 <br>
     * <br>
     * 算法依次取:IMEI、AndroidId、md5后的MAC地址 <br>
     * <br>
     * 取IMEI时需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String sPhoneId;

    /**
     * 本机的手机号 不一定获取得到 <br>
     * 需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String sPhoneNum;

    public static void init(Context context, String appname) {
        PackageManager manager = context.getPackageManager();

        // app名字：appName
        if (ITextUtil.isValidText(appname)) {
            sAppName = appname;
        } else {
            try {
                ApplicationInfo ai = manager.getApplicationInfo(context.getPackageName(), 0);
                sAppName = (String) manager.getApplicationLabel(ai);
            } catch (Exception e) {

            }
        }

        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {

        }
        if (info != null) {
            // app版本： (AppVer)
            sAppVer = info.versionName;
            // app内部版本： (versionCode)
            sVersionCode = info.versionCode;
        }

        // 安装包渠道号：(AppPM)
        try {
            PackageManager localPackageManager = context.getPackageManager();
            ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            sAppPM = (String) localApplicationInfo.metaData.get("UMENG_CHANNEL");
            sUmengKey = (String) localApplicationInfo.metaData.get("UMENG_APPKEY");
        } catch (Exception e) {

        }

        // 设备ID，每台设备唯一 (DeviceID)（即设备的IMEI，平板可能没有）
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyMgr != null) {
            sDeviceID = mTelephonyMgr.getDeviceId();
            sPhoneId = sDeviceID;
        }

        try {
            sAndroidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        if (!ITextUtil.isValidText(sPhoneId))
            sPhoneId = sAndroidID;

        // MD5后的MAC地址
        try {
            sMD5MACID = MD5Util.MD5(getLocalMacAddress(context));
        } catch (Exception e) {

        }
        if (!ITextUtil.isValidText(sPhoneId))
            sPhoneId = sMD5MACID;

        // 设备机型：如，iphone4，i9000 (Model)
        sModel = "Android-" + android.os.Build.MODEL;

        // OS版本：如，iOS5.0，android2.3.7 (OSVer)
        sOSVer = android.os.Build.VERSION.RELEASE;

        sOSDesc = getBuildDescription();

        // UUID APP安装一次重新生成个id
        new Thread(new Runnable() {
            @Override
            public void run() {
                sUuid = getInstallID();
                if (sDeviceID == null || sDeviceID.trim().length() < 8)
                    sDeviceID = sUuid;
            }
        }).start();

        // 获取本机的手机号 有几率获取不到
        if (mTelephonyMgr != null)
            sPhoneNum = mTelephonyMgr.getLine1Number();

    }

    private static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info != null ? info.getMacAddress() : "";
    }

    public static String getBuildDescription() {
        String desc = "unknown";
        try {
            Class<?> clazz = Class.forName("android.os.Build");
            Class<?> paraTypes = Class.forName("java.lang.String");
            Method method = clazz.getDeclaredMethod("getString", paraTypes);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            Build b = new Build();
            desc = (String) method.invoke(b, "ro.build.description");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return desc;
    }

    /**
     * 一个app安装一次生成一个id
     * 
     * @return
     */
    private static String getInstallID() {
        String KEY = "uuid-aedcrasdfen";// key填充字符串 防止重复

        String uuid = SharedPreferencesUtil.getString(KEY);
        if (!ITextUtil.isValidText(uuid)) {
            uuid = new String(UUID.randomUUID().toString().getBytes());
            SharedPreferencesUtil.saveString(KEY, uuid);
        }

        return uuid;
    }

    /**
     * 是否是平板
     * 
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}
