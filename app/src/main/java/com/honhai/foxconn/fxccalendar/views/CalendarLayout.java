package com.honhai.foxconn.fxccalendar.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;

import com.honhai.foxconn.fxccalendar.activities.StartActivity;

import java.util.Calendar;

public class CalendarLayout extends ConstraintLayout {

    StartActivity activity;
    GestureDetector gestureDetector;

    private float moveDistance;
    private float x;

    ValueAnimator animator = new ValueAnimator();

    public CalendarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        activity = (StartActivity) context;
        gestureDetector = new GestureDetector(context, new MyGestureListener());

        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(300);
        animator.addUpdateListener(animation -> {
            moveDistance = (float) animation.getAnimatedValue();
            selfInvalidate();
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animator.isRunning())
            return true;
        performClick();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                moveDistance = event.getX() - x;
                selfInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(moveDistance) > getWidth() / 2) {
                    animator.setFloatValues(moveDistance, (moveDistance > 0 ? getWidth() : -getWidth()));
                    animator.start();
                } else {
                    animator.setFloatValues(moveDistance, 0);
                    animator.start();
                }
                break;
        }
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

    public void selfInvalidate() {
        super.invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (animator.isRunning())
            return true;
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
            refreshChild();
        return gestureDetector.onTouchEvent(ev);
    }

    private void refreshChild() {
        for (int i = 0; i < getChildCount(); i++) {
            ((CalendarWeekItem) getChildAt(i)).refreshPress();
        }
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        canvas.translate(moveDistance, 0);
        super.dispatchDraw(canvas);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (Math.abs(distanceX) > 5) {
                x = e2.getX();
                refreshChild();
                return true;
            }
            return false;
        }
    }
}
