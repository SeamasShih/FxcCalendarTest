package com.honhai.foxconn.fxccalendar.views.add.item;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

public abstract class SampleView extends ConstraintLayout {
    public SampleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void setColor(int color);

    public abstract void findViews();
}
