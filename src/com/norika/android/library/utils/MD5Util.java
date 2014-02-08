
package com.norika.android.library.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    /**
     * MD5加密，32位(最好拖入NDK)
     * 
     * @param str
     * @return
     */
    public static String md5(String str) {
        if (!ITextUtil.isValidText(str))
            return "";

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);

        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = (md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

    /**
     * MD5变换(最好拖入NDK)
     * <p>
     * Modify the method by Norika in 2012.6.21 11:11
     * <ol>
     * <li>Change StringBuffer to StringBuilder
     * <p>
     * The method doesn't use no static filed, so it not exists thread-safe
     * problem;</li>
     * <li>Change compute ways
     * <p>
     * Bit computing instead of Math.</li>
     * <li>Modify this, run with more speed.</li>
     * <li>Consider of the string which contains some blank.</li>
     * </ol>
     * 
     * @param str
     * @return
     */
    public static String md5Self(String str) {
        if (!ITextUtil.isValidText(str))
            return "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            char[] HEX = {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
            };
            byte[] md5Byte = md5.digest(str.getBytes("UTF8"));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < md5Byte.length; i++) {
                sb.append(HEX[(md5Byte[i] & 0xf0) >>> 4]);
                sb.append(HEX[md5Byte[i] & 0x0f]);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
