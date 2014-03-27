
package com.norika.android.library.utils;

import android.os.AsyncTask;
import android.widget.TextView;

import com.norika.android.library.base.BaseApplication;

/**
 * 系统级缺陷的工具包
 * 
 * @version 6.3
 * @since 6.3
 * @author Norika
 */
public class SysMakeUpUtil {

    private SysMakeUpUtil() {

    }

    public <T, K, V> void exeute(AsyncTask<T, K, V> a, T... params) {
        if (Utils.hasHoneycomb())
            a.executeOnExecutor(BaseApplication.LIMITED_TASK_EXECUTOR, params);
        else
            a.execute(params);
    }

    /**
     * 低版本系统需要设置才有效
     * 
     * @param tv 指定{@linkplain TextView}
     */
    public static void FakeBoldText(TextView tv) {
        // 低版本强制设置
        if (!Utils.hasHoneycomb())
            tv.getPaint().setFakeBoldText(true);

        // 高版本未设置的设置下该属性
        if (!tv.getPaint().isFakeBoldText())
            tv.getPaint().setFakeBoldText(true);
    }
}
