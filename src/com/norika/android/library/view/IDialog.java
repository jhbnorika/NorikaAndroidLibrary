
package com.norika.android.library.view;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.norika.android.library.R;
import com.norika.android.library.utils.ITextUtil;
import com.norika.android.library.utils.OffsetUtils;

public class IDialog extends Dialog implements OnClickListener, CheckBox.OnCheckedChangeListener {
    private final TextView btnOk, btnCancel, tvDetail, tvTitle;
    private final View sepLine;
    private android.view.View.OnClickListener okListener, cancelListener;
    private final CheckBox cb;
    private CheckBox.OnCheckedChangeListener cbListener;

    public IDialog(Context context) {
        super(context);
        // super(context, R.style.BeautyDialog);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.i_view_idialog);

        tvTitle = (TextView) findViewById(R.id.dialog_title);
        OffsetUtils.FakeBoldText(tvTitle);
        sepLine = findViewById(R.id.dialog_margin);
        tvDetail = (TextView) findViewById(R.id.dialog_detail);
        // 超过一定行数，自动滚动
        tvDetail.setMovementMethod(ScrollingMovementMethod.getInstance());

        btnOk = (TextView) findViewById(R.id.dialog_ok);
        btnCancel = (TextView) findViewById(R.id.dialog_cancel);
        cb = (CheckBox) findViewById(R.id.dialog_checkbox);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        cb.setOnCheckedChangeListener(this);

        setCanceledOnTouchOutside(false);
    }

    private void setOnOKClick(android.view.View.OnClickListener okListener) {
        this.okListener = okListener;
    }

    private void setOnCancelClick(android.view.View.OnClickListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    private void setOnCheckedChangedClick(CheckBox.OnCheckedChangeListener cbListener) {
        this.cbListener = cbListener;
    }

    private void showCheckBox() {
        cb.setVisibility(View.VISIBLE);
    }

    private void setPositiveAndNegativeText(CharSequence textOk, CharSequence textCancel) {
        int i = 0;// 二进制 11
        if (ITextUtil.isValidText(textOk))
            i = i | 2;// 二进制10
        if (ITextUtil.isValidText(textCancel))
            i = i | 1;// 二进制01

        switch (i) {
            case 0:
            case 2:
                btnCancel.setVisibility(View.GONE);
                btnOk.setText(textOk);
                break;
            case 1:
                btnOk.setVisibility(View.GONE);
                btnCancel.setText(textCancel);
                break;
            case 3:
                btnOk.setText(textOk);
                btnCancel.setText(textCancel);
                break;
            default:
                break;
        }
    }

    private void setTips(CharSequence tips) {
        tvDetail.setText(tips);
    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    private void hideTitle() {
        tvTitle.setVisibility(View.GONE);
        sepLine.setVisibility(View.GONE);
    }

    public boolean isCheckBox() {
        return cb.isChecked();
    }

    @Override
    public void onBackPressed() {
        if (btnCancel.getVisibility() == View.VISIBLE) {
            if (cancelListener != null)
                cancelListener.onClick(btnCancel);
        } else if (btnOk.getVisibility() == View.VISIBLE) {
            if (okListener != null)
                okListener.onClick(btnOk);
        }
        dismiss();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dialog_ok) {
            if (okListener != null)
                okListener.onClick(v);
            dismiss();
        } else if (v.getId() == R.id.dialog_cancel) {
            if (cancelListener != null)
                cancelListener.onClick(v);
            dismiss();
        }
    }

    /**
     * 显示弹出框
     * 
     * @param context 上下文
     * @param title 标题
     * @param tips 详情
     * @param positiveText 目标动作文本
     * @param positiveListener 目标动作监听
     * @param negativeText 取消动作文本
     * @param negativeListener 取消动作监听
     * @param cbListener CheckBox监听器
     * @return
     */
    public static IDialog showBeautyDialog(Context context, CharSequence title, CharSequence tips,
            CharSequence positiveText,
            android.view.View.OnClickListener positiveListener, CharSequence negativeText,
            android.view.View.OnClickListener negativeListener,
            CheckBox.OnCheckedChangeListener cbListener) {
        IDialog dialog = new IDialog(context);
        dialog.setTips(tips);
        if (ITextUtil.isValidText(title))
            dialog.setTitle(title);
        else
            dialog.hideTitle();
        dialog.setPositiveAndNegativeText(positiveText, negativeText);
        dialog.setOnOKClick(positiveListener);
        dialog.setOnCancelClick(negativeListener);
        if (cbListener != null) {
            dialog.setOnCheckedChangedClick(cbListener);
            dialog.showCheckBox();
        }
        dialog.show();
        return dialog;
    }

    /**
     * 显示弹出框
     * 
     * @param context 上下文
     * @param title 标题
     * @param tips 详情
     * @param positiveText 目标动作文本
     * @param positiveListener 目标动作监听
     * @param negativeText 取消动作文本
     * @param negativeListener 取消动作监听
     * @return
     */
    public static IDialog showBeautyDialog(Context context, CharSequence title, CharSequence tips,
            CharSequence positiveText,
            android.view.View.OnClickListener positiveListener, CharSequence negativeText,
            android.view.View.OnClickListener negativeListener) {
        return showBeautyDialog(context, title, tips, positiveText, positiveListener, negativeText, negativeListener,
                null);
    }

    /**
     * 显示弹出框
     * 
     * @param context 上下文
     * @param title 标题
     * @param tips 详情
     * @param positiveText 目标动作文本
     * @param positiveListener 目标动作监听
     * @return
     */
    public static IDialog showBeautyDialog(Context context, CharSequence title, CharSequence tips,
            CharSequence positiveText,
            android.view.View.OnClickListener positiveListener) {
        return showBeautyDialog(context, title, tips, positiveText, positiveListener, null, null, null);
    }

    /**
     * 显示弹出框
     * 
     * @param context 上下文
     * @param tips 详情
     * @param positiveText 目标动作文本
     * @param positiveListener 目标动作监听
     * @param negativeText 取消动作文本
     * @param negativeListener 取消动作监听
     * @return
     */
    public static IDialog showBeautyDialog(Context context, CharSequence tips, CharSequence positiveText,
            android.view.View.OnClickListener positiveListener, CharSequence negativeText,
            android.view.View.OnClickListener negativeListener) {
        return showBeautyDialog(context, null, tips, positiveText, positiveListener, negativeText, negativeListener,
                null);
    }

    /**
     * 显示弹出框
     * 
     * @param context 上下文
     * @param tips 详情
     * @param positiveText 目标动作文本
     * @param positiveListener 目标动作监听
     * @param negativeText 取消动作文本
     * @param negativeListener 取消动作监听
     * @return
     */
    public static IDialog showBeautyDialog(Context context, CharSequence tips, CharSequence positiveText,
            android.view.View.OnClickListener positiveListener) {
        return showBeautyDialog(context, null, tips, positiveText, positiveListener, null, null, null);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.dialog_checkbox) {
            if (cbListener != null)
                cbListener.onCheckedChanged(buttonView, isChecked);
        }
    }
}
