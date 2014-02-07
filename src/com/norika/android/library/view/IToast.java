
package com.norika.android.library.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.norika.android.library.R;
import com.norika.android.library.base.BaseApplication;

/**
 * Toast弹出框
 * 
 * @author Norika
 * @since 1.0
 * @version 1.0
 */
public class IToast {
    private volatile static IToast instance;

    private final Toast toast;
    private final TextView tvDetail;

    /**
     * JDK1.5版本之后的单例支持
     * 
     * @param context 参数尽量调用context.getApplicationContext()
     * @return
     */
    public static IToast getInstance(Context context) {
        if (instance == null) {
            synchronized (IToast.class) {
                if (instance == null) {
                    instance = new IToast(context);
                }
            }
        }
        return instance;
    }

    public IToast(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.i_view_itoast, null);
        tvDetail = (TextView) view.findViewById(R.id.tv_popwindow);

        toast = new Toast(BaseApplication.getInstance());
        toast.setView(view);
    }

    public void showCenter(String text) {
        tvDetail.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void showBottom(String text) {
        tvDetail.setText(text);
        toast.setGravity(Gravity.BOTTOM, 0, 120);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void hideToast() {
        if (toast != null)
            toast.cancel();
    }

}
