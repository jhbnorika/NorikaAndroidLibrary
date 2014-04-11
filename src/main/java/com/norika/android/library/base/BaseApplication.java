
package com.norika.android.library.base;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.norika.android.library.BuildConfig;
import com.norika.android.library.IPhone;
import com.norika.android.library.utils.DebugUtil;
import com.norika.android.library.utils.ITextUtil;
import com.norika.android.library.utils.SharedPreferencesUtil;
import com.norika.android.library.utils.Utils;

public abstract class BaseApplication<T extends BaseApplication<T>> extends Application {
    private RequestQueue mRequestQueue;

    private final static int THREAD_POOL_NUM = 5;
    /** AsyncTask 线程池 */
    public static final ExecutorService LIMITED_TASK_EXECUTOR = Executors.newFixedThreadPool(THREAD_POOL_NUM);

    @Override
    public void onCreate() {
        super.onCreate();

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        if (BuildConfig.DEBUG)
            Utils.enableStrictMode();

        SharedPreferencesUtil.create(getApplicationContext());

        IPhone.init(this, "iapp");

        // TODO 地图
    }

    public <K> void addRequest(Request<K> req, String tag) {
        req.setTag(ITextUtil.isValidText(tag) ? tag : "volleypatterns");
        VolleyLog.d("addRequest", "addRequest");
        mRequestQueue.add(req);
    }

    public <K> void addRequest(Request<K> req) {
        addRequest(req, null);
    }

    public void cancelPendingRequest(String tag) {
        if (tag != null)
            mRequestQueue.cancelAll(tag);
        else
            DebugUtil.e(this.getClass().getName() + ":a invalid tag name");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getApplication(Context context) {
        return (T) context.getApplicationContext();
    }
}
