
package com.norika.android.library.base;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.fastjson.JSONArray;

public abstract class SimpleActivity extends BaseActivity {

    protected void removeFragment(String tag) {
        removeFragment(tag, false, NOANIM, NOANIM);
    }

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

    protected boolean saveString(String key, String value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    protected String fetchString(String key, String defValue) {
        return getPreferences(Context.MODE_PRIVATE).getString(key, defValue);
    }

    protected boolean saveBoolean(String key, boolean value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    protected boolean fetchBoolean(String key, boolean defValue) {
        return getPreferences(Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    protected boolean saveInt(String key, int value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    protected boolean saveLong(String key, long value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putLong(key, value).commit();
    }

    protected boolean saveFloat(String key, float value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putFloat(key, value).commit();
    }

    protected boolean saveStringSet(String key, Set<String> value) {
        return getPreferences(Context.MODE_PRIVATE).edit().putStringSet(key, value).commit();
    }

    protected boolean SaveList(String key, List<Object> list) {
        JSONArray ret = new JSONArray(list);
        return getPreferences(Context.MODE_PRIVATE).edit().putString(key, ret.toString()).commit();
    }

    private static final int NOANIM = -1;
}
