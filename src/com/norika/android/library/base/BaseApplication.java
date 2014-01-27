
package com.norika.android.library.base;

import android.app.Application;

import com.norika.android.library.BuildConfig;
import com.norika.android.library.utils.Utils;

public class BaseApplication extends Application {

    private volatile static Application _instance;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG)
            Utils.enableStrictMode();
    }

    public static Application getInstance() {
        return _instance;
    }

}
