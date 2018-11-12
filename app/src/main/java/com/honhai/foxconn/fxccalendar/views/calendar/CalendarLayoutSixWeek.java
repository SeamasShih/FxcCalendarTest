package com.honhai.foxconn.fxccalendar.views.calendar;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.activities.StartActivity;

import java.util.Calendar;

public class CalendarLayoutSixWeek extends ConstraintLayout {

    private StartActivity activity;
    private GestureDetector gestureDetector;
    private CalendarWeekItem w1, w2, w3, w4, w5,w6;

    private float x;

    public CalendarLayoutSixWeek(Context context, AttributeSet attrs) {
        super(context, attrs);

        activity = (StartActivity) context;
        gestureDetector = new GestureDetector(context, new MyGestureListener());

    }

    public void findViews(){
        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        w3 = findViewById(R.id.w3);
        w4 = findViewById(R.id.w4);
        w5 = findViewById(R.id.w5);
        w6 = findViewById(R.id.w6);
    }

    public void setDays(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int[][] monthDays = getMonthDays(calendar);
        w1.setMonday(monthDays[0]);
        w2.setMonday(monthDays[1]);
        w3.setMonday(monthDays[2]);
        w4.setMonday(monthDays[3]);
        w5.setMonday(monthDays[4]);
        w6.setMonday(monthDays[5]);
        w1.setMonth(year, month);
        w2.setMonth(year, month);
        w3.setMonth(year, month);
        w4.setMonth(year, month);
        w5.setMonth(year, month);
        w6.setMonth(year, month);
        calendar.set(year, month, 1);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        w1.setWeek(-1, week);
        w2.setWeek(0, week + 1);
        w3.setWeek(0, week + 2);
        w4.setWeek(0, week + 3);
        w5.setWeek(0, week + 4);
        w6.setWeek(1, week + 5);
        calendar.set(year,month,dayOfMonth);
        Calendar now = Calendar.getInstance();
        int nowYear = now.get(Calendar.YEAR);
        int nowMonth = now.get(Calendar.MONTH);
        if (nowYear == year && nowMonth == month){
            int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
            int weekOfMonth = now.get(Calendar.WEEK_OF_MONTH);
            switch (weekOfMonth){
                case 1:
                    w1.setToday(dayOfWeek-1);
                    break;
                case 2:
                    w2.setToday(dayOfWeek-1);
                    break;
                case 3:
                    w3.setToday(dayOfWeek-1);
                    break;
                case 4:
                    w4.setToday(dayOfWeek-1);
                    break;
                case 5:
                    w5.setToday(dayOfWeek-1);
                    break;
                case 6:
                    w6.setToday(dayOfWeek-1);
                    break;
            }
        }
    }

    private int[][] getMonthDays(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int[][] r = new int[6][7];
        for (int i = 0; i < r.length; i++) {
            for (int j = 0; j < r[0].length; j++) {
                r[i][j] = dayOfMonth - dayOfWeek + j + 1 + (-weekOfMonth + i + 1) * 7;
            }
        }
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < r[0].length; i++) {
            if (r[5][i] > max)
                r[5][i] -= max;
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

    public void selfInvalidate() {
        super.invalidate();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
            refreshChild();
        return gestureDetector.onTouchEvent(ev);
    }

    private void refreshChild() {
        for (int i = 1; i < getChildCount(); i++) {
            ((CalendarWeekItem) getChildAt(i)).refreshPress();
        }
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
