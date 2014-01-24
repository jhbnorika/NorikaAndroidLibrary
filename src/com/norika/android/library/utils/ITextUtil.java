
package com.norika.android.library.utils;

import java.util.regex.Pattern;

public class ITextUtil {

    public static final String SEP_MIDDLE = "-";
    public static final String SEP_POINT = ".";
    public static final String SEP_DOWN = "_";

    private static final String REGULAR_DATE = "^[\\d]*[-](([0]?[1-9])|([1][0-2]))[-](([3][0-1])|(([1-2])?[0-9])|([0]?[1-9]))[ ](([0-1]?[0-9])|([2][0-3]))([:]([0-5]?[0-9])){2}$";
    private static final String REGULAR_EMAIL = "^[\\w][\\w-]*([.][\\w-]+){0,3}@[\\w-]+([.][\\w-]+){0,1}([.][\\w]{2,3}){1,3}$";
    private static final String REGULAR_PASSWORD = "^[\\w!#$%^*()`~][\\w-!@#$%^*()`~]{5,15}$";
    private static final String REGULAR_USERNAME = "^[\\w-]{5,15}$";
    private static final String REGULAR_NUMERIC = "[0-9]*";
    /** 简易年月正则 */
    private static final String REGULAR_YYYY_MM = "^[0-9]{4}[-][0-9]{2}$";
    /** 简易年月日正则 */
    private static final String REGULAR_YYYY_MM_DD = "^[0-9]{4}[-][0-9]{2}[-][0-9]{2}";

    /**
     * Determine whether it is a valid charSequence
     * 
     * @param value
     * @return {@code true} if not {@code null} and the value's length is over 0
     *         (not include the blank), or {@code false}
     */
    public static boolean isValidText(CharSequence value) {
        return value != null && value.toString().trim().length() > 0;
    }

    /**
     * Determine whether it is valid date
     * <p>
     * <li>Do not distinguish the Average year and the Leap year, so the
     * "2012-02-31 12:12:12" is right</li>
     * <li>Do not distinguish the Otsuki and the Satsuki, so the
     * "2012-04-31 12:12:12" is right</li>
     * <li>Do not consider the synchronize and unsynchronized thread, so the
     * performance needs to be paid attentions by yourself</li>
     * </p>
     * <p>
     * <table border="2" width="85%" align="center" cellpadding="5">
     * <thead>
     * <tr>
     * <th>Example</th>
     * <th>Result</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td rowspan="1">"002014-4-31 23:59:59"</td>
     * <td>{@code true}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">"000-02-31 0:0:0"</td>
     * <td>{@code true}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">"2013-03-06 24:00:00"</td>
     * <td>{@code false}</td>
     * </tr>
     * </tbody>
     * <tr>
     * <td rowspan="1">"2013-03-06 23:60:00"</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">"2013-03-06 23:00:60"</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">"2013-003-006 23:00:00"</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">"9999-12-31 23:59:59"</td>
     * <td>{@code true}</td>
     * </tr>
     * </table>
     * </p>
     * 
     * @param strDate
     * @return {@code true} <var>strDate</var> is valid data, or {@code false}
     */
    public static boolean isValidDate(String date) {
        return matchRegular(date, REGULAR_DATE);
    }

    /**
     * Verify the email
     * <p>
     * <table border="1" width="85%" align="center" cellpadding="5">
     * <thead>
     * <tr>
     * <th>Example</th>
     * <th>Result</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td rowspan="1">""</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">null</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abc.def@sina.com.cn</td>
     * <td>{@code true}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abc.def@sina.flags.com.cn</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">.def@sina.com.cn</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abc@.com.cn</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abc@126.com</td>
     * <td>{@code true}</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * 
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        return matchRegular(email, REGULAR_EMAIL);
    }

    /**
     * Verify the password
     * <p>
     * <li>Do not contain blank</li>
     * <li>Only contain the character such as "A-Z","a-z","-","_","0-9"</li>
     * <li>Length:6-16</li>
     * </p>
     * <p>
     * <table border="1" width="85%" align="center" cellpadding="5">
     * <thead>
     * <tr>
     * <th>Example</th>
     * <th>Result</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td rowspan="1">""</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">null</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abcdef</td>
     * <td>{@code true}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">a bcdef</td>
     * <td>{@code false}</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * 
     * @param password
     * @return
     */
    public static boolean isValidPassword(String pwd) {
        return matchRegular(pwd, REGULAR_PASSWORD);
    }

    /**
     * Verify the password
     * <p>
     * <li>Do not contain blank</li>
     * <li>Only contain the character such as "A-Z","a-z","-","_","0-9"</li>
     * <li>Length:5-15</li>
     * </p>
     * <p>
     * <table border="1" width="85%" align="center" cellpadding="5">
     * <thead>
     * <tr>
     * <th>Example</th>
     * <th>Result</th>
     * </tr>
     * </thead> <tbody>
     * <tr>
     * <td rowspan="1">""</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">null</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abcde</td>
     * <td>{@code true}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">abcdefghijklmno</td>
     * <td>{@code false}</td>
     * </tr>
     * <tr>
     * <td rowspan="1">a bcdef</td>
     * <td>{@code false}</td>
     * </tr>
     * </tbody>
     * </table>
     * </p>
     * 
     * @param username
     * @return
     */
    public static boolean isValidUserName(String name) {
        return matchRegular(name, REGULAR_USERNAME);
    }

    public static boolean isDate_YYYY_MM(String date) {
        return matchRegular(date, REGULAR_YYYY_MM);
    }

    public static boolean isDate_YYYY_MM_DD(String date) {
        return matchRegular(date, REGULAR_YYYY_MM_DD);
    }

    /**
     * 判断字符串是否是数字
     * 
     * @param numeric
     * @return
     */
    public static boolean isNumeric(String numeric) {
        return matchRegular(numeric, REGULAR_NUMERIC);
    }

    public static int parseInt(String iStr, int defInt) {
        try {
            return Integer.parseInt(iStr);
        } catch (NumberFormatException e) {
            return defInt;
        }
    }

    public static double parseDouble(String dStr, double defDou) {
        try {
            return Double.parseDouble(dStr);
        } catch (NumberFormatException e) {
            return defDou;
        }
    }

    private static boolean matchRegular(String param, String regular) {
        if (isValidText(param))
            return false;

        Pattern pattern = Pattern.compile(regular);
        return pattern.matcher(param).matches();
    }

    public static RegularFeedBack isRegexEmail(String email) {
        if (!isValidText(email))
            return new RegularFeedBack(false, "邮箱不能为空");

        if (email.length() < 6 || email.length() > 40)
            return new RegularFeedBack(false, "邮箱长度6-40个字符，请重新输入");

        if (email.startsWith("-"))
            return new RegularFeedBack(false, "邮箱不能以\"-\"开头");

        if (!isValidText(email))
            return new RegularFeedBack(false, "邮箱格式有误或含有非法字符");

        return new RegularFeedBack(true, "");
    }

    public static class RegularFeedBack {
        private boolean isValid;
        private String error;

        public RegularFeedBack(boolean isValid, String error) {
            this.isValid = isValid;
            this.error = error;
        }

        public boolean isValid() {
            return isValid;
        }

        public void setValid(boolean isValid) {
            this.isValid = isValid;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
