
package com.norika.android.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 虚线
 * 
 * @author Norika
 * @since 1.0
 * @version 1.0
 */
public class DashedLineView extends View {
    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final PathEffect effects = new DashPathEffect(new float[] {
            5, 5, 5, 5
    }, 2);

    public DashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DashedLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public DashedLineView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#ffDEDEDE"));
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
}
