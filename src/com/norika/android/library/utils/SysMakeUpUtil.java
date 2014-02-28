
package com.norika.android.library.utils;

import android.os.AsyncTask;

import com.norika.android.library.base.BaseApplication;

/**
 * 系统级缺陷的工具包
 * 
 * @version 6.3
 * @since 6.3
 * @author Norika
 */
public class SysMakeUpUtil {

    public <T, K, V> void exeute(AsyncTask<T, K, V> a, T... params) {
        if (Utils.hasHoneycomb())
            a.executeOnExecutor(BaseApplication.LIMITED_TASK_EXECUTOR, params);
        else
            a.execute(params);
    }
}
