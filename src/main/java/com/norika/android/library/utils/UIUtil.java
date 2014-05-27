package com.norika.android.library.utils;

import android.content.Context;
import android.util.TypedValue;

public class UIUtil {
    
    public static float DimenSp2Dx(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
    }

    public static float DimenDp2Dx(Context context, float value) {
        return TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }
}
