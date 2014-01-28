
package com.norika.android.library.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * 可自动保存Intent中Bundle信息的Activity基类，子类只需调用getIntentExtras()来获取上一个Activity传来的参数即可 <br>
 * 用于解决Activity重新启动后，原有的Intent中的参数丢失问题
 * 
 * @author Norika
 * @version 1.0
 * @since 1.0
 */
public class BaseActivity extends FragmentActivity {

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

    protected void removeFragment(String tag) {
        removeFragment(tag, false, NOANIM, NOANIM);
    }

    /**
     * 移除Fragment
     * 
     * @param tag
     * @param enter 进入动画
     * @param exit 退出动画
     */
    protected void removeFragment(String tag, int enter, int exit) {
        removeFragment(tag, false, enter, exit);
    }

    protected void removeFragmentAllowingStateLoss(String tag) {
        removeFragment(tag, true, NOANIM, NOANIM);
    }

    private void removeFragment(String tag, boolean allowStateLoss, int enter, int exit) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (enter == NOANIM || exit == NOANIM)
            fragmentTransaction.setCustomAnimations(enter, exit);

        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null)
            fragmentTransaction.remove(prev);

        // fragmentTransaction.addToBackStack(null);
        if (allowStateLoss)
            fragmentTransaction.commitAllowingStateLoss();
        else
            fragmentTransaction.commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment, String tag) {
        replaceFragment(containerViewId, fragment, tag, false);
    }

    protected void replaceFragmentAllowingStateLoss(int containerViewId, Fragment fragment, String tag) {
        replaceFragment(containerViewId, fragment, tag, true);
    }

    private void replaceFragment(int containerViewId, Fragment fragment, String tag, boolean allowStateLoss) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment, tag);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (allowStateLoss)
            fragmentTransaction.commitAllowingStateLoss();
        else
            fragmentTransaction.commit();
    }

    private Bundle mSavedState;
    private static final int NOANIM = -1;
}
