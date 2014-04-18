
package com.norika.android.library.core;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.norika.android.library.utils.NetworkUtil;

/**
 * 核心请求
 * 
 * @author Norika
 */
public abstract class AbstractCoreRequest {
    private final Context mContext;

    private static final int DELAY_TIME_MS = 3000;
    private static final int DEFAULT_MAX_TRY_TIMES = 3;
    /** 最大重试次数，以最大可能的规避超时或其他（可避免的）异常 */
    private int max_try_times = DEFAULT_MAX_TRY_TIMES;
    private int requestTimes = 0;

    private final Semaphore semp = new Semaphore(0);
    // TODO 延迟加载有问题，待解决
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            semp.release();
            super.handleMessage(msg);
        }
    };

    public AbstractCoreRequest(Context context) {
        this(context, DEFAULT_MAX_TRY_TIMES);
    }

    public AbstractCoreRequest(Context context, int times) {
        mContext = context;
        max_try_times = times;
        if (max_try_times < 0)
            max_try_times = 0;
    }

    public boolean runOnLocal() {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            try {
                semp.acquire();
                return runOnLocal();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }

        try {
            request();
        } catch (IOException e) {
            e.printStackTrace();
            requestTimes++;
            if (requestTimes < max_try_times)
                return runOnLocal();
            else
                return false;
        }

        return false;
    }

    public Context getContext() {
        return mContext;
    }

    protected abstract boolean request() throws IOException;

}
