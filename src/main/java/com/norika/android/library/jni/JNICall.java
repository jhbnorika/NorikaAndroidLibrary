
package com.norika.android.library.jni;

import com.norika.android.library.base.BaseActivity;

public class JNICall extends BaseActivity {

    static {
        System.loadLibrary("norika");
    }

    public native String jniFromC();
}
