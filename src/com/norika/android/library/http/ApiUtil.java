
package com.norika.android.library.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;
import com.norika.android.library.IPhone;
import com.norika.android.library.utils.ITextUtil;
import com.norika.android.library.utils.MD5Util;

/**
 * API工具集
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class ApiUtil {

    private static final String PhoneInfo_NOT_INITIALIZE_ERROR_STRING = IPhone.class.getSimpleName()
            + " not initialize. Please run " + IPhone.class.getSimpleName() + ".initialize() first !";

    /**
     * 获取加密SIG（最好拖入NDK）
     * 
     * @param extraParams
     * @param params
     * @param sigUrl
     * @param privateKey
     * @return
     */
    public static String getSig(Map<String, String> extraParams, Map<String, String> params, String sigUrl,
            String privateKey) {
        if (!ITextUtil.isValidText(privateKey))
            throw new RuntimeException("privateKey is empty.");

        List<String> keyList = new ArrayList<String>();

        Iterator<Entry<String, String>> it = extraParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();
            if (value == null)
                value = "";
            keyList.add(key + "=" + value);
        }

        // ------不可或缺的排序-----
        Collections.sort(keyList);

        StringBuffer sb = new StringBuffer();
        for (String string : keyList) {
            if (sb.toString().length() == 0)
                sb.append(string);
            else
                sb.append("&" + string);
        }

        // get表单传递的参数为null，post表单传递的参数有效
        String paramStr = params != null ? JSON.toJSONString(params) : "";

        // -------添加了pirvateKey的url，作为被加密的原始字符串---------
        String url_key = sigUrl + sb.toString() + paramStr + privateKey;
        return MD5Util.md5Self(url_key);
    }

    public static String getExtraParams(String qtime) {
        StringBuffer retBuffer = new StringBuffer();

        HashMap<String, String> temp = getExtraParamsHashMap(qtime);
        for (String key : temp.keySet())
            retBuffer.append("&").append(key).append("=").append(strUrlEncode(temp.get(key)));

        return retBuffer.toString();
    }

    public static HashMap<String, String> getExtraParamsHashMap(String qtime) {
        HashMap<String, String> ret = new HashMap<String, String>();

        if (!IPhone.isPhoneOk())
            throw new RuntimeException(PhoneInfo_NOT_INITIALIZE_ERROR_STRING);

        ret.put("i", IPhone.getsDeviceID());
        ret.put("macid", IPhone.getsMD5MACID());
        ret.put("id", IPhone.getsPhoneId());
        ret.put("m", IPhone.getsModel());
        ret.put("o", IPhone.getsOSDesc());
        ret.put("v", IPhone.getsOSVer());
        ret.put("cv", IPhone.getsAppVer());
        ret.put("app", IPhone.getsAppName());
        ret.put("pm", IPhone.getsAppPM());
        ret.put("source", IPhone.getSsource());
        // 统一由外部指定同一时间 不然算sig会有问题
        if (qtime != null)
            ret.put("qtime", qtime);

        if (IPhone.getsUuid() != null)
            ret.put("uuid", IPhone.getsUuid());

        return ret;
    }

    public static String strUrlEncode(String str) {
        if (str == null)
            return "";

        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
