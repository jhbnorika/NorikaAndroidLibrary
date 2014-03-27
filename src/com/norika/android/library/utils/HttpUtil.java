
package com.norika.android.library.utils;

import java.io.InputStream;
import java.security.KeyStore;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.norika.android.library.R;

public class HttpUtil {

    /**
     * 创建{@link HttpClient}对象
     * <p>
     * 用于HTTP请求
     * 
     * @param connTimeoutSec 连接超时时间 单位秒
     * @param soTimeoutSec 读取超时时间 单位秒
     * @return
     */
    public static HttpClient createHttpClient(int connTimeoutSec, int soTimeoutSec) {
        HttpParams params = buildHttpParams(connTimeoutSec, soTimeoutSec);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // schemeRegistry.register(new Scheme("https",
        // SSLSocketFactory.getSocketFactory(), 443));
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        // 使用线程安全的连接管理来创建HttpClient
        /*
         * ThreadSafeClientConnManager 是一个很复杂的实现，
         * 它管理一个客户连接池，服务多个执行线程的连接请求，连接被每个路由放入池中。
         * 连接池中可用的已经存在持久连接的路由请求由池中租借的连接进行服务，而不是创建一个新的连接分支。
         * ThreadSafeClientConnManager在每个路由中维持一个最大的连接限制。
         * 缺省的应用对每个路由创建仅仅2个concurrent连接，总数不超过20个连接。
         * 对于很多现实的应用，这些限制可能出现太多的限制，特别是如果他们使用HTTP作为一个传输协议进行服务。
         * 连接限制.然而，可以通过HTTP参数进行调整。
         */
        // Increase max total connection to 200
        // ConnManagerParams.setMaxTotalConnections(params, 200);
        // Increase default max connection per route to 20
        // ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(manager, params);
    }

    /**
     * 创建{@link HttpClient}对象
     * <p>
     * 用于HTTPS请求
     * 
     * @param context 上下文
     * @param connTimeoutSec 连接超时时间 单位秒
     * @param soTimeoutSec 读取超时时间 单位秒
     * @return
     */
    public static HttpClient createHttpsClient(Context context, int connTimeoutSec, int soTimeoutSec) {
        HttpParams params = buildHttpParams(connTimeoutSec, soTimeoutSec);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        // Register for port 443 our SSLSocketFactory with our keystore
        // to the ConnectionManager
        schemeRegistry.register(new Scheme("https", newSslSocketFactory(context), 443));

        // 使用线程安全的连接管理来创建HttpClient
        /*
         * ThreadSafeClientConnManager 是一个很复杂的实现，
         * 它管理一个客户连接池，服务多个执行线程的连接请求，连接被每个路由放入池中。
         * 连接池中可用的已经存在持久连接的路由请求由池中租借的连接进行服务，而不是创建一个新的连接分支。
         * ThreadSafeClientConnManager在每个路由中维持一个最大的连接限制。
         * 缺省的应用对每个路由创建仅仅2个concurrent连接，总数不超过20个连接。
         * 对于很多现实的应用，这些限制可能出现太多的限制，特别是如果他们使用HTTP作为一个传输协议进行服务。
         * 连接限制.然而，可以通过HTTP参数进行调整。
         */
        // Increase max total connection to 200
        // ConnManagerParams.setMaxTotalConnections(params, 200);
        // Increase default max connection per route to 20
        // ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(manager, params);
    }

    private static HttpParams buildHttpParams(int connTimeoutSec, int soTimeoutSec) {
        HttpParams params = new BasicHttpParams();

        /*
         * Turn off stale checking. Our connections break all the time anyway,
         * and it's not worth it to pay the penalty of checking every time.
         * 检测陈旧的连接是否可用，否重新建连接 true:每次send建 false:不建 暂时没有用
         */
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // 设置超时
        // 从连接池中取连接的超时时间
        ConnManagerParams.setTimeout(params, 1000);
        // Default connection and socket timeout of 20 seconds. Tweak to taste.
        // 建立连接的超时时间（以毫秒为单位）当值为0被解释成一个无限超时，如果此参数不设置，连接操作不会超时（无限超时）。
        HttpConnectionParams.setConnectionTimeout(params, connTimeoutSec * 1000);
        // 以毫秒为单位定义套接字超时（SO_TIMEOUT）。当值为0被解释成一个无限的暂停，如果此参数不设置，读操作不会超时（无限暂停）。
        HttpConnectionParams.setSoTimeout(params, soTimeoutSec * 1000);
        // 接收/传输HTTP消息时，确定socket内部缓冲区缓存数据的大小，如果此参数不设置，HttpClient将分配8192字节socket缓冲区
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        /*
         * Don't handle redirects -- return them to the caller. Our code often
         * wants to re-POST after a redirect, which we must do ourselves. DOES
         * WE NEED REDIRECTING? 重定向
         */
        HttpClientParams.setRedirecting(params, true);

        // Set the specified user agent and register standard protocols.
        HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                + "AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");

        // 定义使用HTTP协议版本，如果不设置此参数将使用HTTP/1.1
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        // 定义字符集用于每个内容体的默认编码，如果不设置此参数将使用ISO-8859-1
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        /*
         * ExpectContinue是http协议1.1中的header属性
         * 设置了ExpectContinue意味着客户端在向服务器发送数据的时候，
         * 需要先向服务器发起一个请求看服务器是否愿意接受客户端将要发送的数据（我们这里可以认为是http body，往往是较大的数据块）。
         * 因为ExpectContinue会导致客户端在向服务器发送数据是进行两次请求，
         * 这样对通信的性能方面将会受到一定的影响，但这种情况在做验证或者给予curl的服务器是很常见的，这也是该属性的主要应用场合。
         * 介于以上原因，我们不能滥用该属性，并且在不支持http 1.1的协议的服务器也会产生一定的问题。
         * HttpProtocolParams.setUseExpectContinue(params, true);
         */

        return params;
    }

    private static SSLSocketFactory newSslSocketFactory(Context context) {
        try {
            // Get an instance of the Bouncy Castle KeyStore format
            KeyStore trusted = KeyStore.getInstance("BKS");
            // Get the raw resource, which contains the keystore with
            // your trusted certificates (root and any intermediate certs)
            InputStream in = null;
            try {
                // Initialize the keystore with the provided trusted
                // certificates
                // Also provide the password of the keystore
                in = context.getResources().openRawResource(R.raw.anjukeapikeystore);
                trusted.load(in, "anjukeapi".toCharArray());
            } finally {
                if (in != null)
                    in.close();
            }
            // Pass the keystore to the SSLSocketFactory. The factory is
            // responsible
            // for the verification of the server certificate.
            SSLSocketFactory sf = new SSLSocketFactory(trusted);
            // Hostname verification from certificate
            // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d4e506
            sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sf;
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
