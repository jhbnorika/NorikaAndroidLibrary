
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
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.norika.android.library.share.CommValue;
import com.norika.android.library.utils.DebugUtil;
import com.norika.android.library.utils.ITextUtil;
import com.norika.android.library.utils.MD5Util;
import com.norika.android.library.utils.SharedPreferencesUtil;
import com.norika.android.library.utils.SharedPreferencesUtil.SPKeys;

/**
 * 手机信息
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class IPhone {

    /** APP名字 */
    private static String sAppName;
    /** APP版本 */
    private static String sAppVer;
    /** APP内部版本 */
    private static int sVersionCode;
    /** APP平台 */
    private static final String sAppPlatform = "android";

    private static final String sSource = "mobile";

    /** 安装包渠道号 */
    private static String sAppPM;
    /** UMeng Key */
    private static String sUmengKey;

    /**
     * 设备ID
     * <p>
     * 需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>
     */
    private static String sDeviceID;
    /** MD5化MACID */
    private static String sMD5MACID;
    /** AndroidId */
    private static String sAndroidID;

    /** 设备机型 */
    private static String sModel;
    /** OS版本 */
    private static String sOSVer;
    /** 系统ro.build.description */
    private static String sOSDesc;

    /**
     * UUID APP安装一次重新生成新id
     * <p>
     * 已安装的从SharedPreferences中读取，UUID默认为{@code null} ,{@link #init()}
     * 后约100ms可取得该值
     */
    private static String sUuid;

    /**
     * <h1>用于唯一标识设备的编号</h1>
     * <ul>
     * <li>基于IMEI、MAC地址、AndroidId可能获取不到，统一逻辑以最大程度上获取唯一标示手机的编码</li>
     * <li>取值逻辑:IMEI > AndroidId > MD5后的MAC地址</li>
     * <li>取IMEI时需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/></li>
     * </ul>
     */
    private static String sPhoneId;

    /**
     * 本机的手机号 不一定获取得到 <br>
     * 需要增加权限&#60;uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/>
     */
    private static String sPhoneNum;

    private static boolean isPhoneOk;

    public static void init(Context context, String appname) {
        sAppName = getAppName(context, appname);

        PackageInfo info = getAppInfo(context);
        if (info != null) {
            // APP版本(AppVer)
            sAppVer = info.versionName;
            // APP内部版本号(VersionCode)
            sVersionCode = info.versionCode;
        }

        Bundle metadata = getMetaData(context);
        if (metadata != null) {
            // 安装包渠道号(AppPM)
            sAppPM = (String) metadata.get("UMENG_CHANNEL");
            // UMeng Key
            sUmengKey = (String) metadata.get("UMENG_APPKEY");
        }

        sDeviceID = getDeviceID(context);
        sAndroidID = getAndroidID(context);
        sMD5MACID = MD5Util.md5(getLocalMacAddress(context));

        sPhoneId = getOneDeviceId(sDeviceID, sAndroidID, sMD5MACID);

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
            }
        }, "thread_uuid").start();

        sPhoneNum = getPhoneNum(context);

        isPhoneOk = true;

        log();
    }

    /**
     * 获取APPNAME
     * 
     * @param c
     * @param appname
     * @return
     */
    private static String getAppName(Context c, String appname) {
        PackageManager manager = c.getPackageManager();

        if (ITextUtil.isValidText(appname))
            return appname;

        try {
            ApplicationInfo ai = manager.getApplicationInfo(c.getPackageName(), 0);
            return (String) manager.getApplicationLabel(ai);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * APP信息
     * 
     * @param context
     * @return
     */
    private static PackageInfo getAppInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            return manager.getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Bundle getMetaData(Context context) {
        try {
            PackageManager localPackageManager = context.getPackageManager();
            ApplicationInfo localApplicationInfo = localPackageManager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            return localApplicationInfo != null ? localApplicationInfo.metaData : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 设备ID，每台设备唯一 (DeviceID)（即设备的IMEI，纯平板可能没有）
     * 
     * @return
     */
    private static String getDeviceID(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr != null ? mTelephonyMgr.getDeviceId() : "";
    }

    private static String getAndroidID(Context context) {
        try {
            return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取本机的手机号 有几率获取不到
     * 
     * @param context
     * @return
     */
    private static String getPhoneNum(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr != null ? mTelephonyMgr.getLine1Number() : "";
    }

    /**
     * 获取唯一的设备标识
     * <p>
     * 匹配规则：若deviceID，则获取该值，否则androidID，否则md5macID，否则""
     * 
     * @param deviceID
     * @param androidID
     * @param md5macID
     * @return
     */
    private static String getOneDeviceId(String deviceID, String androidID, String md5macID) {
        if (ITextUtil.isValidText(deviceID))
            return deviceID;
        if (ITextUtil.isValidText(androidID))
            return androidID;
        if (ITextUtil.isValidText(md5macID))
            return md5macID;
        return "";
    }

    /**
     * 获取设备MAC地址（未MD5）
     * 
     * @param context
     * @return
     */
    private static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifi == null)
            return "";

        WifiInfo info = wifi.getConnectionInfo();
        return info != null ? info.getMacAddress() : "";
    }

    private static String getBuildDescription() {
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
     * APP安装后生成一个ID
     * <p>
     * 最好放到线程中获取
     * 
     * @return
     */
    private static String getInstallID() {
        String uuid = SharedPreferencesUtil.getString(SPKeys.KEY_UUID);

        if (ITextUtil.isValidText(uuid))
            return uuid;

        uuid = new String(UUID.randomUUID().toString().getBytes());
        SharedPreferencesUtil.saveString(SPKeys.KEY_UUID, uuid);

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

    public static String getsAppName() {
        return sAppName;
    }

    public static String getsAppVer() {
        return sAppVer;
    }

    public static int getsVersionCode() {
        return sVersionCode;
    }

    public static String getSappplatform() {
        return sAppPlatform;
    }

    public static String getsAppPM() {
        return sAppPM;
    }

    public static String getsUmengKey() {
        return sUmengKey;
    }

    public static String getsDeviceID() {
        return sDeviceID;
    }

    public static String getsMD5MACID() {
        return sMD5MACID;
    }

    public static String getsAndroidID() {
        return sAndroidID;
    }

    public static String getsModel() {
        return sModel;
    }

    public static String getsOSVer() {
        return sOSVer;
    }

    public static String getsOSDesc() {
        return sOSDesc;
    }

    public static String getsUuid() {
        return sUuid;
    }

    public static String getsPhoneId() {
        return sPhoneId;
    }

    public static String getsPhoneNum() {
        return sPhoneNum;
    }

    public static boolean isPhoneOk() {
        return isPhoneOk;
    }

    public static String getSsource() {
        return sSource;
    }

    private static void log() {
        int length = sUmengKey.length();
        int start = length >= 6 ? length - 6 : 0;
        String umeng = sUmengKey.substring(start, length);

        String umengVer = "内部版本号：" + sVersionCode + "\r\n渠道号："
                + sAppPM + "\r\n友盟: " + umeng + "\r\napi版本号："
                + "暂未设定";
        DebugUtil.d(CommValue.INNER_LOG_TAG, umengVer);
    }
}
