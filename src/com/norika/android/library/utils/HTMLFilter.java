
package com.norika.android.library.utils;

import android.text.TextUtils;

public class HTMLFilter implements Filter {

    @Override
    public String doFilter(String msg) {
        if (TextUtils.isEmpty(msg))
            return msg;

        // 处理HMTL tag
        return msg.replace("<", "[").replace(">", "]");
    }

}
