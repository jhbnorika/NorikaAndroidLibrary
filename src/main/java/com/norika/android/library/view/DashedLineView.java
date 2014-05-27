
package com.norika.android.library.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

import com.norika.android.library.R;

/**
 * 虚线
 * 
 * @author Norika
 * @since 1.0
 * @version 1.1
 */
public class DashedLineView extends View {

    /** 代码中初始化调用 */
    public DashedLineView(Context context) {
        this(context, null);
    }

    /** XML定义 */
    public DashedLineView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.DropDownStyle);
    }

    /** 不调用，除非显示调用或系统调用 */
    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final Resources.Theme theme = context.getTheme();
        TypedArray a = theme.obtainStyledAttributes(
                attrs, R.styleable.DashedLineView, defStyle, 0);

        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DashedLineView_lineColor:
                    lineColor = a.getColor(attr, 0xffDEDEDE);
                    break;
                default:
                    break;
            }
        }
        a.recycle();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(lineColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        path.moveTo(0, getHeight() / 2);
        path.lineTo(getWidth(), getHeight() / 2);
        paint.reset();
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }

    private int lineColor = 0xffDEDEDE;

    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final PathEffect effects = new DashPathEffect(new float[] {
            5, 5, 5, 5
    }, 2);

    private static final String TAG = DashedLineView.class.getSimpleName();
}
