
package com.norika.android.library.view;

import android.widget.TextView;

import com.norika.android.library.utils.Utils;

public class OffsetUtils {

    private OffsetUtils() {

    }

    /**
     * 低版本系统需要设置才有效
     * 
     * @param tv 目标{@linkplain TextView}
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
