package com.honhai.foxconn.fxccalendar.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.honhai.foxconn.fxccalendar.activities.StartActivity;

import java.util.Calendar;

public class CalendarLayout extends ConstraintLayout {

    StartActivity activity;
    GestureDetector gestureDetector;

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (StartActivity) context;
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("Seamas", "DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("Seamas", "MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.d("Seamas", "UP");
                break;
        }
        performClick();
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
            refreshChild();
        return gestureDetector.onTouchEvent(ev);
    }

    private void refreshChild() {
        for (int i = 0; i < getChildCount(); i++) {
            ((CalendarWeekItem) getChildAt(i)).refreshPress();
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > 5;
        }
    }
}
