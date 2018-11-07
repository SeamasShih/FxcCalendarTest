package com.honhai.foxconn.fxccalendar.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.activities.StartActivity;
import com.honhai.foxconn.fxccalendar.data.Event;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarLayout extends ConstraintLayout {

    private StartActivity activity;
    private GestureDetector gestureDetector;
    private CalendarWeekItem w1, w2, w3, w4, w5;

    private float moveDistance;
    private float x;

    private ValueAnimator animator = new ValueAnimator();

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

    public void distributeEvent() {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event(
                "E1", 2018, 10, 1, 16, 0,
                2018, 10, 3, 8, 0, Color.RED
        ));
        w1.setEvents(events);
        w2.setEvents(events);
        w3.setEvents(events);
        w4.setEvents(events);
        w5.setEvents(events);
    }

    public void findViews(){
        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        w3 = findViewById(R.id.w3);
        w4 = findViewById(R.id.w4);
        w5 = findViewById(R.id.w5);
    }

    public void setDays(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int[][] monthDays = getMonthDays(calendar);
        w1.setMonday(monthDays[0]);
        w2.setMonday(monthDays[1]);
        w3.setMonday(monthDays[2]);
        w4.setMonday(monthDays[3]);
        w5.setMonday(monthDays[4]);
        w1.setMonth(year, month);
        w2.setMonth(year, month);
        w3.setMonth(year, month);
        w4.setMonth(year, month);
        w5.setMonth(year, month);
        calendar.set(year, month, 1);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        w1.setWeek(1, week);
        w2.setWeek(2, week + 1);
        w3.setWeek(3, week + 2);
        w4.setWeek(4, week + 3);
        w5.setWeek(5, week + 4);
        calendar.set(year,month,dayOfMonth);
    }

    private int[][] getMonthDays(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int[][] r = new int[5][7];
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                r[i][j] = dayOfMonth - dayOfWeek + j + 1 + (-weekOfMonth + i + 1) * 7;
            }
        }
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < r[4].length; i++) {
            if (r[4][i] > max)
                r[4][i] -= max;
        }
        calendar.add(Calendar.MONTH, -1);
        max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < r[0].length; i++) {
            if (r[0][i] <= 0)
                r[0][i] += max;
        }
        calendar = Calendar.getInstance();
        return r;
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
