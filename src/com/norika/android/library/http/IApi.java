
package com.norika.android.library.http;

import java.util.Map;

import com.norika.android.library.utils.DateUtil;

public class IApi {

    private static final String HOST_URL = "http://fake_url";
    private static final String PRIVATE_KEY = "private_key";
    private static final String API_KEY = "api_key";

    private static final String VERSION;

    private static final String ONLINE_VERSION = "/x.x/";
    private static final String TEST_VERSION = "/x.x_aaaaaa/";

    private static final String ROUTE1 = "route";

    static {
        VERSION = TEST_VERSION;
    }

    public static void httpGet(String url, ChainMap params, ICallback callback) {
        wrapGet(url, params.map(), callback);
    }

    public static void httpGet(String url, ICallback callback) {
        httpGet(url, ChainMap.create(), callback);
    }

    private static void wrapGet(String url, Map<String, String> params, HttpCallback<?> callback) {
        String qtime = DateUtil.formatTime(System.currentTimeMillis(), DateUtil.FROMAT_YMDHMS);
        callback.url(getUrl(url, qtime, params));
        String sig = ApiUtil.getSig(ApiUtil.getExtraParamsHashMap(qtime), params, getSigUrl(url),
                PRIVATE_KEY);
        putHeaders(callback, sig);
    }

    /**
     * @param key_url
     * @param qtime (格式为：年月日时分秒，例如 20130407222809)
     * @return
     */
    private static String getUrl(String key_url, String qtime, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(HOST_URL);
        builder.append(VERSION);
        builder.append(key_url);
        builder.append("?");

        String extraParams = ApiUtil.getExtraParams(qtime);
        if (extraParams.startsWith("&"))
            extraParams = extraParams.replaceFirst("&", "");
        builder.append(extraParams);

        if (params != null) {
            for (String key : params.keySet())
                builder.append("&").append(key).append("=").append(ApiUtil.strUrlEncode(params.get(key)));
        }

        return builder.toString();
    }

    private static void putHeaders(HttpCallback<?> callback, String sig) {
        callback.header("sig", sig);
        callback.header("key", API_KEY);
        callback.header("Accept", "application/json");
        callback.header("Content-type", "application/json");
    }

    private static String getSigUrl(String url) {
        return VERSION + url;
    }
}
