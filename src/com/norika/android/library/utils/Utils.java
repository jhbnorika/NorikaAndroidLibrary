/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norika.android.library.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;

/**
 * Class containing some static utility methods.
 * <ul>
 * <li>线程策略方面，它能够发现UI线程读写DISK，UI线程访问网络，自己写的速度慢的代码。</li>
 * <li>VM策略方面，它能够发现 Activity内存泄露， SQL 对象内存泄露， 资源未释放，能够限定某个类的最大对象数。</li>
 * </ul>
 */
public class Utils {

    private Utils() {

    }

    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode() {
        if (Utils.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = // 线程策略
            new StrictMode.ThreadPolicy.Builder()
                    .detectAll() // 发现所有策略的违反行为
                    .penaltyLog(); // 发现违反策略，打印log。还有其他的方式，参考API
            StrictMode.VmPolicy.Builder vmPolicyBuilder = // VM策略
            new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog();

            if (Utils.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen(); // 发现违反策略的行为，是屏幕闪烁
                // FIXME:这里限制 XClass 的实例最多只有一个，否则违反策略。
                // vmPolicyBuilder
                // .setClassInstanceLimit(Activity1.class, 1)
                // .setClassInstanceLimit(Activity2.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    /**
     * SDK 9 Android 2.3
     * 
     * @return
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    /**
     * SDK 11 Android 3.0
     * 
     * @return
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    /**
     * SDK 12 Android 3.1
     * 
     * @return
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * SDK 16 Android 4.1
     * 
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    /**
     * SDK 19 Android 4.4
     * 
     * @return
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }
}
