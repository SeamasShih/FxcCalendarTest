package com.honhai.foxconn.fxccalendar.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.honhai.foxconn.fxccalendar.data.Event;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarWeekItem extends View {

    private int mWidth;
    private int mHeight;
    private float dayWidth;
    private float weekHeight;
    private float eventHeight;

    private int year;
    private int month;
    private int maxOfMonth;
    private int weekOfMonth;
    private int weekOfYear;
    private int[] weekdays = new int[7];
    private ArrayList<Event> events = new ArrayList<>();

    private Paint paintWeek = new Paint();
    private float adjPaintWeek;
    private Paint paintEvent = new Paint();
    private float adjPaintEvent;
    private Paint paintBackground = new Paint();

    private GestureDetector gestureDetector;
    private int press = -1;

    public CalendarWeekItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBackground.setAntiAlias(true);
        paintEvent.setColor(Color.WHITE);
        gestureDetector = new GestureDetector(context, new MyGestureListener());
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void addEvents(Event event) {
        events.add(event);
    }

    private void setTextPaint(Paint paint, float textSize) {
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        dayWidth = ((float) mWidth) / 7;
        weekHeight = ((float) mHeight) / 5;
        eventHeight = ((float) mHeight) / 7;
        setTextPaint(paintWeek, weekHeight / 2);
        setTextPaint(paintEvent, eventHeight / 2);
        Paint.FontMetrics metrics = paintWeek.getFontMetrics();
        adjPaintWeek = (metrics.descent - metrics.ascent) / 2 - metrics.descent;
        metrics = paintEvent.getFontMetrics();
        adjPaintEvent = (metrics.descent - metrics.ascent) / 2 - metrics.descent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void refreshPress() {
        press = -1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShadow(canvas);
        drawWeek(canvas);
        canvas.translate(0, weekHeight);
        drawEvent(canvas);
    }

    private void drawShadow(Canvas canvas) {
        if (press <= 6 && press >= 0) {
            Paint shadow = new Paint();
            shadow.setColor(Color.argb(50, 0, 0, 0));
            shadow.setAntiAlias(true);
            canvas.drawRect(press * dayWidth, 0,(press + 1) * dayWidth , mHeight, shadow);
        }
    }

    private void drawEvent(Canvas canvas) {
        if (events.size() != 0) {
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                int start = checkStart(event);
                int end = checkEnd(event);
                if (start == -1 || end == -1) continue;
                paintBackground.setColor(event.color);
                canvas.save();
                canvas.drawRoundRect(start * dayWidth, 0, (end + 1) * dayWidth, eventHeight, eventHeight / 5, eventHeight / 5, paintBackground);
                canvas.drawText(event.context, (start * dayWidth + (end + 1) * dayWidth) / 2, eventHeight / 2 + adjPaintEvent, paintEvent);
                canvas.restore();
            }
        }
    }

    private int checkEnd(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(event.endYear, event.endMonth, event.endDayOfMonth);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int endWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (event.endYear > year) {
            if (endWeek != 1)
                return 6;
            else {
                calendar.set(year, 12, 31);
                endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            }
        } else if (event.endYear < year) {
            calendar.set(event.endYear, 12, 31);
            if (endWeek != calendar.get(Calendar.WEEK_OF_YEAR))
                return -1;
        }

        if (endWeek < weekOfYear)
            return -1;
        else if (endWeek > weekOfYear)
            return 6;
        else
            return endWeekday - 1;
    }

    private int checkStart(Event event) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(event.startYear, event.startMonth, event.startDayOfMonth);
        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        int startWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        if (event.startYear > year) {
            if (startWeek != 1)
                return -1;
            else {
                calendar.set(year, 12, 31);
                startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            }
        } else if (event.startYear < year) {
            calendar.set(event.startYear, 12, 31);
            if (startWeek != calendar.get(Calendar.WEEK_OF_YEAR))
                return 0;
        }

        if (startWeek < weekOfYear)
            return 0;
        else if (startWeek > weekOfYear)
            return -1;
        else
            return startWeekday - 1;
    }

    private void drawWeek(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 7; i++) {
            int colorInMonth = Color.BLACK;
            int colorOutMonth = Color.GRAY;
            switch (weekOfMonth) {
                case 1:
                    paintWeek.setColor(colorOutMonth);
                    if (weekdays[i] < 10)
                        paintWeek.setColor(colorInMonth);
                    canvas.drawText(String.valueOf(weekdays[i]), dayWidth / 2, weekHeight / 2 + adjPaintWeek, paintWeek);
                    canvas.translate(dayWidth, 0);
                    break;
                case 5:
                    paintWeek.setColor(colorInMonth);
                    if (weekdays[i] < 10)
                        paintWeek.setColor(colorOutMonth);
                    canvas.drawText(String.valueOf(weekdays[i]), dayWidth / 2, weekHeight / 2 + adjPaintWeek, paintWeek);
                    canvas.translate(dayWidth, 0);
                    break;
                default:
                    paintWeek.setColor(colorInMonth);
                    canvas.drawText(String.valueOf(weekdays[i]), dayWidth / 2, weekHeight / 2 + adjPaintWeek, paintWeek);
                    canvas.translate(dayWidth, 0);
                    break;
            }

        }
        canvas.restore();
    }

    public void setWeek(int weekOfMonth, int weekOfYear) {
        this.weekOfMonth = weekOfMonth;
        this.weekOfYear = weekOfYear;
    }

    public void setMonday(int[] days) {
        weekdays = days;
    }

    public void setMonth(int year, int month) {
        this.month = month;
        this.year = year;
        if (month == Calendar.FEBRUARY) {
            if (year % 4 == 0)
                maxOfMonth = 29;
            else
                maxOfMonth = 28;
        } else if (month == Calendar.JANUARY || month == Calendar.MARCH || month == Calendar.MAY ||
                month == Calendar.JULY || month == Calendar.AUGUST || month == Calendar.OCTOBER ||
                month == Calendar.DECEMBER) {
            maxOfMonth = 31;
        } else {
            maxOfMonth = 30;
        }
    }

    private int checkClickRegion(float x) {
        return (int) (x / dayWidth);
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.d("Seamas", "onDoubleTap");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("Seamas", "onLongPress");
        }

        @Override
        public boolean onDown(MotionEvent e) {
            press = checkClickRegion(e.getX());
            invalidate();
            return true;
        }
    }
}
