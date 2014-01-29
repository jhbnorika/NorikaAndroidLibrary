
package com.norika.android.library.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 可自动保存Intent中Bundle信息的Activity基类，子类只需调用getIntentExtras()来获取上一个Activity传来的参数即可 <br>
 * 用于解决Activity重新启动后，原有的Intent中的参数丢失问题
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
            mSavedState = getIntent().getExtras();
        else
            mSavedState = savedInstanceState;
    }

    /**
     * 获取Intent传过来的参数，用于替代方法getIntent().getExtras() <br>
     * 用于解决Activity重新启动后，原有的Intent中的参数丢失问题
     * 
     * @return
     */
    protected final Bundle getIntentExtras() {
        if (mSavedState == null)
            mSavedState = new Bundle();
        return mSavedState;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSavedState != null)
            outState.putAll(mSavedState);
    }

    private Bundle mSavedState;
}
