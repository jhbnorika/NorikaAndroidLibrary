
package com.norika.android.library.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkUtil {

    /** 得到IP地址 */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                        return inetAddress.getHostAddress().toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            DebugUtil.e(LOG_TAG, String.valueOf(e));
        }
        return null;
    }

    public static String getNetIsWifiOr3G(Context context) {
        // 获取系统的连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取网络的连接情况
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo == null)
            return "none";
        else if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            return "2G3G";
        else
            return "unknown";
    }

    private final static String LOG_TAG = NetworkUtil.class.getName();

    /** 判断设备网络是否可用 */
    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null)
            return false;

        NetworkInfo info[] = manager.getAllNetworkInfo();
        if (info == null)
            return false;

        for (int i = 0; i < info.length; i++) {
            if (info[i].getState() == NetworkInfo.State.CONNECTED)
                return true;
        }

        return false;
    }

    /** 是否有SIM卡的存在 */
    public static Boolean isSimPresent(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (manager.getSimState()) {
            case TelephonyManager.SIM_STATE_READY:
                return true;
            case TelephonyManager.SIM_STATE_ABSENT:
            default:
                return false;
        }
    }

    /**
     * get network type
     * <ul>
     * <li>0：No Connection</li>
     * <li>1：WIFI</li>
     * <li>2：2G/3G/Other</li>
     * </ul>
     * 
     * @param inContext
     * @return 0、1、2
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info == null || !info.isConnected())
            return 0;
        else if (info.getTypeName().toUpperCase().equals("WIFI"))
            return 1;
        else
            return 2;
    }

    /**
     * 获取网络类型
     * <ul>
     * <li>NETWORK_TYPE_CDMA 网络类型为CDMA</li>
     * <li>NETWORK_TYPE_EDGE 网络类型为EDGE</li>
     * <li>NETWORK_TYPE_EVDO_0 网络类型为EVDO0</li>
     * <li>NETWORK_TYPE_EVDO_A 网络类型为EVDOA</li>
     * <li>NETWORK_TYPE_GPRS 网络类型为GPRS</li>
     * <li>NETWORK_TYPE_HSDPA 网络类型为HSDPA</li>
     * <li>NETWORK_TYPE_HSPA 网络类型为HSPA</li>
     * <li>NETWORK_TYPE_HSUPA 网络类型为HSUPA</li>
     * <li>NETWORK_TYPE_UMTS 网络类型为UMTS</li>
     * </ul>
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO
     * 
     * @param context
     * @return <ul>
     *         <li>无网络："NOCONNECTION"</li>
     *         <li>联通3G||电信3G：3G</li>
     *         <li>三大运营商2G：2G</li>
     *         <li>位置类型（比如移动3G）:2Gor3G</li>
     *         </ul>
     */
    public static String getNetWorkTypeStr(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "NOCONNECTION";

        switch (info.getType()) {
            case ConnectivityManager.TYPE_WIFI:
                return "WIFI";
            case ConnectivityManager.TYPE_MOBILE:
                if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSDPA
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_HSPAP
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_UMTS
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_0
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EVDO_A) {
                    return "3G";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_CDMA
                        || info.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE) {
                    return "2G";
                } else if (info.getSubtype() == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                    return "2Gor3G";
                }
            default:
                return "UNKOWN";
        }
    }

    public static String getNetworkName(Context appContext) {
        ConnectivityManager connectivity = (ConnectivityManager) appContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();

        if (info == null || !info.isConnected())
            return "NOCONNECTION";

        if (info.getTypeName().toUpperCase().equals("WIFI"))
            return info.getExtraInfo();

        return info.getSubtypeName();
    }

    /**
     * 通过MNC方式获得移动运营商名字
     * <p>
     * 获取SIM卡的IMSI码 SIM卡唯一标识：IMSI 国际移动用户识别码（IMSI：International Mobile Subscriber
     * Identification Number）是区别移动用户的标志，
     * 储存在SIM卡中，可用于区别移动用户的有效信息。IMSI由MCC、MNC、MSIN组成，其中MCC为移动国家号码，由3位数字组成，
     * 唯一地识别移动客户所属的国家，我国为460；MNC为网络id，由2位数字组成，
     * 用于识别移动客户所归属的移动网络，中国移动为00，中国联通为01,中国电信为03；MSIN为移动客户识别码，采用等长11位数字构成。
     * 唯一地识别国内GSM移动通信网中移动客户 。所以要区分是移动还是联通，只需取得SIM卡中的MNC字段即可
     * <p>
     * 移动网络编号46000下的IMSI已用完，故虚拟了46002编号，134/159号段使用了此编号
     * <p>
     * "getNetworkOperatorName=" + telManager.getNetworkOperatorName()+"\n"
     * 直接获取移动运营商名称
     * 
     * @param context
     * @return 中国移动/中国联通/中国电信/未知运营商
     */
    public static String getWirelessCarriers(Context context) {
        TelephonyManager telManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telManager.getSubscriberId();
        if (imsi == null)
            return "未知运营商";

        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007"))
            return "中国移动";
        else if (imsi.startsWith("46001"))
            return "中国联通";
        else if (imsi.startsWith("46003"))
            return "中国电信";

        return "未知运营商";
    }
}
