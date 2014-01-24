
package com.norika.android.library.bitmapfun.base;

import android.app.Application;

import com.norika.android.library.BuildConfig;
import com.norika.android.library.bitmapfun.util.Utils;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG)
            Utils.enableStrictMode();
    }

}
