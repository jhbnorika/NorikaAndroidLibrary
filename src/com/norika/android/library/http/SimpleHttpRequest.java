
package com.norika.android.library.http;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.norika.android.library.utils.DebugUtil;
import com.norika.android.library.utils.HttpUtil;

/**
 * 封装HTTP和HTTPS
 * <p>
 * 为兼容HTTP请求，设计成独立使用HTTP和HTTPS(即HTTP/TLS)，即请求时必须调用对应的HttpClient
 * <ul>
 * <li>待调整进行自适应</li>
 * <li>签名层不在该模块</li>
 * </ul>
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class SimpleHttpRequest {

    private SimpleHttpRequest() {

    }

    public static String getMethod(String url, HashMap<String, String> params, int connTimeOut, int soTimeOut)
            throws ClientProtocolException, IOException {
        return getMethod(url, params, true, null, connTimeOut, soTimeOut);
    }

    public static String getMethod(String url, HashMap<String, String> params, boolean isUseGzip,
            HashMap<String, String> header, int connTimeOut, int soTimeOut) throws ClientProtocolException,
            IOException {
        String urled = urlAssembledParams(url, params);
        HttpGet method = new HttpGet(urled);

        // gzip压缩
        if (isUseGzip)
            method.setHeader("Accept-Encoding", "gzip");

        // 自定义header
        if (header != null) {
            for (String key : header.keySet())
                method.setHeader(key, header.get(key));
        }

        debugVPrint("getMethod url:" + urled);
        HttpResponse response = getHttpClient(connTimeOut, soTimeOut).execute(method);
        HttpEntity entity = response.getEntity();
        String ret = EntityUtils.toString(entity);

        // 是否gzip压缩 处理不同
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null && contentEncoding.getValue().contains("gzip")) {
            GZIPInputStream gzip = new GZIPInputStream(entity.getContent());
            StringBuilder outString = new StringBuilder();
            byte[] b = new byte[4096];
            int n = gzip.read(b);
            while (n != -1) {
                outString.append(new String(b, 0, n));
                n = gzip.read(b);
            }
            ret = outString.toString();
            debugVPrint("getMethod gzip response:" + String.valueOf(ret));

            // 关闭流
            try {
                gzip.close();
                gzip = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ret = EntityUtils.toString(entity);
            debugVPrint("getMethod response:" + String.valueOf(ret));
        }

        return ret;
    }

    public static String getMethod(String url, HashMap<String, String> params) throws ClientProtocolException,
            IOException {
        return getMethod(url, params, true, null);
    }

    public static String getMethod(String url, HashMap<String, String> params, HashMap<String, String> header)
            throws ClientProtocolException, IOException {
        return getMethod(url, params, true, header);
    }

    public static String getMethod(String url, HashMap<String, String> params, boolean isUseGzip)
            throws ClientProtocolException, IOException {
        return getMethod(url, params, isUseGzip, null);
    }

    public static String getMethod(String url, boolean isUseGzip, HashMap<String, String> header)
            throws ClientProtocolException, IOException {
        return getMethod(url, null, isUseGzip, header);
    }

    public static String getMethod(String url) throws ClientProtocolException, IOException {
        return getMethod(url, null, true, null);
    }

    public static String getMethod(String url, boolean isUseGzip) throws ClientProtocolException, IOException {
        return getMethod(url, null, isUseGzip, null);
    }

    /**
     * Http Method请求
     * <p>
     * GZip压缩的开启，对连续联想有影响，原因不明
     * 
     * @param url 基本URL
     * @param params 参数
     * @param isUseGzip 是否压缩
     * @param header 头部
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getMethod(String url, HashMap<String, String> params, boolean isUseGzip,
            HashMap<String, String> header)
            throws ClientProtocolException, IOException {
        String urled = urlAssembledParams(url, params);
        HttpGet method = new HttpGet(urled);

        if (isUseGzip)
            method.setHeader("Accept-Encoding", "gzip");

        if (header != null) {
            for (String key : header.keySet())
                method.setHeader(key, header.get(key));
        }

        debugVPrint("getMethod url:" + urled);
        HttpResponse response = getHttpClient().execute(method);
        HttpEntity entity = response.getEntity();

        String ret = null;
        // 是否gzip压缩 处理不同
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null && contentEncoding.getValue().contains("gzip")) {
            GZIPInputStream gzip = new GZIPInputStream(entity.getContent());
            StringBuilder outString = new StringBuilder();
            byte[] b = new byte[4096];
            int n = gzip.read(b);
            while (n != -1) {
                outString.append(new String(b, 0, n));
                n = gzip.read(b);
            }
            ret = outString.toString();
            debugVPrint("getMethod gzip response:" + String.valueOf(ret));

            try {
                gzip.close();
                gzip = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ret = EntityUtils.toString(entity);
            debugVPrint("getMethod response:" + String.valueOf(ret));
        }

        return ret;
    }

    public static String httpsGetMethod(Context context, String url, boolean isUseGzip,
            HashMap<String, String> params) throws ClientProtocolException, IOException {
        return httpsGetMethod(context, url, isUseGzip, null, params);
    }

    /** https请求专用 */
    public static String httpsGetMethod(Context context, String url, boolean isUseGzip, HashMap<String, String> header,
            HashMap<String, String> params) throws ClientProtocolException, IOException {
        String urled = urlAssembledParams(url, params);
        HttpGet method = new HttpGet(urled);

        if (isUseGzip)
            method.setHeader("Accept-Encoding", "gzip");

        if (header != null) {
            for (String key : header.keySet()) {
                method.setHeader(key, header.get(key));
            }
        }

        debugVPrint("httpsGetMethod url:" + urled);
        HttpResponse response = getHttpsClient(context).execute(method);
        HttpEntity entity = response.getEntity();

        String ret = null;
        // 是否gzip压缩 处理不同
        Header contentEncoding = entity.getContentEncoding();
        if (contentEncoding != null && contentEncoding.getValue().contains("gzip")) {
            GZIPInputStream gzip = new GZIPInputStream(entity.getContent());
            StringBuilder outString = new StringBuilder();
            byte[] b = new byte[4096];
            int n = gzip.read(b);
            while (n != -1) {
                outString.append(new String(b, 0, n));
                n = gzip.read(b);
            }
            ret = outString.toString();
            debugVPrint("httpsGetMethod gzip response:" + String.valueOf(ret));

            try {
                gzip.close();
                gzip = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ret = EntityUtils.toString(entity);
            debugVPrint("httpsGetMethod response:" + String.valueOf(ret));
        }

        return ret;
    }

    public static String postMethod(String url, HashMap<String, String> params) throws ClientProtocolException,
            IOException {
        return postMethod(url, params, new HashMap<String, String>());
    }

    public static String postMethod(String url, HashMap<String, String> params, HashMap<String, String> header)
            throws ClientProtocolException, IOException {
        HttpPost method = new HttpPost(url);

        // 自定义header
        if (header != null) {
            for (String key : header.keySet())
                method.setHeader(key, header.get(key));
        }

        // TODO choose which way
        // 方式一：JSON
        if (params != null && !params.isEmpty()) {
            String paramsStr = JSON.toJSONString(params);
            debugVPrint("postMethod params:" + paramsStr);
            StringEntity se = new StringEntity(paramsStr, HTTP.UTF_8);
            se.setContentType("application/x-www-form-urlencoded");
            method.setEntity(se);
        }

        // 方式二：NameValuePair
        /*
         * if (params != null && params.size() > 0) { List<NameValuePair>
         * nameValuePairs = new ArrayList<NameValuePair>(); for
         * (Map.Entry<String, String> entry : params.entrySet())
         * nameValuePairs.add(new BasicNameValuePair(entry.getKey(),
         * entry.getValue())); method.setEntity(new
         * UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8)); }
         * debugVPrint("postMethod params:" + ITextUtil.map2Str(params));
         */

        // 方式三：StringBuilder自行拼接
        /*
         * StringBuilder sb = new StringBuilder(); if (params != null &&
         * !params.isEmpty()) { for (Entry<String, String> entry :
         * params.entrySet()) { sb.append(entry.getKey()) .append("=")
         * .append(URLEncoder.encode(entry.getValue(), HTTP.UTF_8)).append("&");
         * } sb.deleteCharAt(sb.lastIndexOf("&")); }
         */

        debugVPrint("postMethod url:" + url);
        HttpResponse response = getHttpClient().execute(method);
        String ret = EntityUtils.toString(response.getEntity());
        debugVPrint("postMethod response:" + String.valueOf(ret));

        return ret;
    }

    public static String httpsPostMethod(Context context, String url, String params, HashMap<String, String> header)
            throws ClientProtocolException, IOException {
        HttpPost method = new HttpPost(url);

        // 自定义header
        if (header != null) {
            for (String key : header.keySet()) {
                method.setHeader(key, header.get(key));
            }
        }

        if (params != null && params.trim().length() > 0) {
            StringEntity se = new StringEntity(params, HTTP.UTF_8);
            method.setEntity(se);
        }

        debugVPrint("httpsPostMethod params:" + params);
        debugVPrint("httpsPostMethod start url:" + url);
        HttpResponse response = getHttpsClient(context).execute(method);
        debugVPrint("httpsPostMethod end");

        String ret = EntityUtils.toString(response.getEntity());
        debugVPrint("httpsPostMethod response:" + String.valueOf(ret));

        return ret;
    }

    /**
     * 为http请求添加cookie
     * 
     * @param method http请求方法：HttpGet，HttpPost
     * @param url http请求的url
     * @param apiName 需要添加cookie的api名
     * @param cookieKey
     * @param cookieValue
     */
    @SuppressWarnings("unused")
    private static void addCookieMethod(HttpRequestBase method, String url, String
            apiName, String cookieKey, String cookieValue) {
        if (url.contains(apiName)) {
            Cookie cookie = new BasicClientCookie(cookieKey, cookieValue);
            CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
            List<Cookie> cookies = new ArrayList<Cookie>();
            cookies.add(cookie);
            cookieSpecBase.formatCookies(cookies);
            method.setHeader(cookieSpecBase.formatCookies(cookies).get(0));
        }
    }

    public static String postMethod(String url, HashMap<String, String> params, String filePath, int disconnectTime)
            throws FileNotFoundException {
        return postMethod(url, params, "file", new File(filePath), disconnectTime);
    }

    public static String postMethod(String url, HashMap<String, String> params, String filePath)
            throws FileNotFoundException {
        return postMethod(url, params, "file", new File(filePath));
    }

    public static String postMethod(String actionUrl, HashMap<String, String> params, String paramsFilename, File file)
            throws FileNotFoundException {
        return postMethod(actionUrl, params, paramsFilename, file, 20 * 1000);
    }

    public static String postMethod(String actionUrl, HashMap<String, String> params, String paramsFilename, File file,
            int disconnectTime) throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(file);

        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String res = null;

        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", HTTP.UTF_8);
            con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            con.setConnectTimeout(10 * 1000);

            // 在wifi连接到cmcc网络(网络可以访问，并会将你重定向到他们自己的登陆页)下测试时，.setReadTimeout及下面手写超时终止均无效
            // 待更改，更改文件上传方式，不使用HttpURLConnection
            con.setReadTimeout(20 * 1000);
            // .setReadTimeout()无用，手写设置超时终止
            new Thread(new InterruptThread(con, disconnectTime)).start();

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(twoHyphens + boundary + end);
                sb.append("Content-Disposition:form-data;name=" + entry.getKey() + end + end + entry.getValue() + end);
            }
            sb.append(twoHyphens + boundary + end);
            sb.append("Content-Disposition:form-data;" + "name=\"" + paramsFilename + "\";filename=\"" + file.getName()
                    + "\"" + end);
            sb.append(end);

            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.write(sb.toString().getBytes());

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            fis.close();
            ds.flush();

            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }

            res = b.toString().trim();
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 基本参数组装URL
     * 
     * @param url
     * @param params
     * @return
     */
    private static String urlAssembledParams(String url, HashMap<String, String> params) {
        if (params == null || params.size() <= 0)
            return url;

        StringBuilder paramSb = new StringBuilder("");
        for (String key : params.keySet()) {
            String value = params.get(key);
            paramSb.append("&" + key + "=" + ApiUtil.strUrlEncode(value));
        }
        if (url.endsWith("?"))
            return url + paramSb.toString();
        else
            return url + "?" + paramSb.toString();
    }

    public static class InterruptThread implements Runnable {
        HttpURLConnection con;
        int time;

        public InterruptThread(HttpURLConnection con, int time) {
            this.con = con;
            this.time = time;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(time);
                con.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到httpclient对象，每次都是新对象
     * <p>
     * 用于HTTP请求
     * </p>
     * 
     * @param connTimeOut 连接超时时间 单位秒
     * @param soTimeOut 读取超时时间 单位秒
     * @return
     */
    private static HttpClient getHttpClient(int connTimeOut, int soTimeOut) {
        HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time
        // anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds. Tweak to
        // taste.
        HttpConnectionParams.setConnectionTimeout(params, connTimeOut * 1000);
        HttpConnectionParams.setSoTimeout(params, soTimeOut * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        // Don't handle redirects -- return them to the caller. Our code
        // often wants to re-POST after a redirect, which we must do
        // ourselves.

        // DOES WE NEED REDIRECTING?
        HttpClientParams.setRedirecting(params, true);

        // Set the specified user agent and register standard protocols.
        HttpProtocolParams.setUserAgent(params, HttpUtil.class.getName());
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // schemeRegistry.register(new Scheme("https",
        // SSLSocketFactory.getSocketFactory(), 443));
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(manager, params);
    }

    /**
     * 得到httpclient对象，每次都是新对象
     * <p>
     * 用于HTTPS请求
     * </p>
     * 
     * @param context 上下文
     * @param connTimeOut 连接超时时间 单位秒
     * @param soTimeOut 读取超时时间 单位秒
     * @return
     */
    private static HttpClient getHttpsClient(Context context, int connTimeoutSec, int soTimeoutSec) {
        return HttpUtil.createHttpsClient(context, connTimeoutSec, soTimeoutSec);
    }

    /**
     * 得到httpclient对象，每次返回第一次建的对象
     * <ul>
     * <li>只能进行HTTP请求</li>
     * <li>HTTPS请求调用{@link #getHttpsClient(Context)}</li>
     * </ul>
     * 
     * @return
     */
    public synchronized static HttpClient getHttpClient() {
        if (_httpClient == null)
            _httpClient = getHttpClient(20, 20);

        return _httpClient;
    }

    /**
     * 得到httpclient对象，每次返回第一次建的对象
     * <ul>
     * <li>只能进行HTTPS请求</li>
     * <li>HTTP请求调用{@link #getHttpClient()}</li>
     * </ul>
     * 
     * @param context 上下文
     * @return
     */
    public synchronized static HttpClient getHttpsClient(Context context) {
        if (_httpsClient == null) {
            _httpsClient = getHttpsClient(context, 20, 20);
        }

        return _httpsClient;
    }

    private static void debugVPrint(String msg) {
        DebugUtil.v("http_request", msg);
    }

    private static HttpClient _httpClient;
    private static HttpClient _httpsClient;
}
