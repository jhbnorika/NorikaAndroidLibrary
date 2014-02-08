
package com.norika.android.library.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final int DAY = 60 * 60 * 24;
    private static final int HOUR = 60 * 60;
    private static final int MINUTE = 60;

    public static final String FROMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FROMAT_YMD = "yyyy-MM-dd";
    public static final String FROMAT_YMDHMS = "yyyyMMddHHmmss";

    /**
     * 根据时间戳，返回时间差描述
     * 
     * @param preTimeMs
     * @param sufTimeMs
     * @return 时间差描述
     */
    public static String getDuration(long preTimeMs, long sufTimeMs) {
        long sub = Math.abs(sufTimeMs - preTimeMs) / 1000l;
        int d = (int) (sub / DAY);

        sub -= d * DAY;
        int h = (int) (sub / HOUR);

        sub -= h * HOUR;
        int m = (int) (sub / MINUTE);

        sub -= m * MINUTE;
        int s = (int) sub;

        return String.format("%1$d天%2$d小时%3$d分%4$d秒", d, h, m, s);
    }

    public static Calendar string2Calendar(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date d = null;
        try {
            d = sdf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        return cal;
    }

    public static String formatTime(long dateMs, String format) {
        Calendar canlendar = Calendar.getInstance();
        canlendar.setTimeInMillis(dateMs);
        return formatTime(canlendar.getTime(), format);
    }

    public static String formatTime(Date dateValue, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(dateValue);
    }

}
