package com.honhai.foxconn.fxccalendar.views.add.popup.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class ColorChoiceDot extends View {
    private int color;
    private boolean isChosen = false;

    public ColorChoiceDot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(6);
        canvas.drawCircle(width / 2, height / 2, width / 6, paint);

        paint.setStrokeWidth(1);
        canvas.drawCircle(width / 2, height / 2, width / 12, paint);

        if (isChosen) {
            paint.setColor(color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width / 2, height / 2, width / 12, paint);
        }
    }
}
