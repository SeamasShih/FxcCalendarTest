package com.honhai.foxconn.fxccalendar.views.calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.activities.AddEventActivity;
import com.honhai.foxconn.fxccalendar.data.Data;
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
    private ArrayList<Event> events = Data.getInstance().events;

    private Paint paintWeek = new Paint();
    private float adjPaintWeek;
    private Paint paintEvent = new Paint();
    private float adjPaintEvent;
    private Paint paintBackground = new Paint();
    private Paint paintLine = new Paint();

    private GestureDetector gestureDetector;
    private int press = -1;
    private int today = -1;
    private boolean[][] eventSeat = new boolean[7][];

    private Context context;

    public CalendarWeekItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBackground.setAntiAlias(true);
        paintEvent.setColor(Color.WHITE);
        gestureDetector = new GestureDetector(context, new MyGestureListener());
        for (int i = 0; i < eventSeat.length; i++) {
            eventSeat[i] = new boolean[4];
        }
        paintLine.setColor(Color.DKGRAY);

        this.context = context;
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
        drawLine(canvas);
        drawShadow(canvas);
        drawWeek(canvas);
        canvas.translate(0, weekHeight);
        drawEvent(canvas);
    }

    private void drawLine(Canvas canvas) {
        canvas.drawLine(0, 0, mWidth, 0, paintLine);
        if (weekOfMonth == 5)
            canvas.drawLine(0, mHeight - 1, mWidth, mHeight - 1, paintLine);
    }

    private void drawShadow(Canvas canvas) {
        if (press <= 6 && press >= 0) {
            Paint shadow = new Paint();
            shadow.setColor(Color.argb(50, 0, 0, 0));
            shadow.setAntiAlias(true);
            canvas.drawRect(press * dayWidth, 0, (press + 1) * dayWidth, mHeight, shadow);
        }
    }

    private void emptyEventSeat() {
        for (int i = 0; i < eventSeat.length; i++) {
            for (int j = 0; j < eventSeat[0].length; j++) {
                eventSeat[i][j] = false;
            }
        }
    }

    private int checkDrawSeatOrder(int start, int end) {
        int r = -1;
        for (int j = 0; j < eventSeat[0].length; j++) {
            boolean available = true;
            for (int i = start; i <= end; i++) {
                if (eventSeat[i][j]) {
                    available = false;
                    break;
                }
            }
            if (available) {
                r = j;
                break;
            }
        }
        return r;
    }

    private void occupySeat(int start, int end, int order) {
        for (int i = start; i <= end; i++) {
            eventSeat[i][order] = true;
        }
    }

    private void drawEvent(Canvas canvas) {
        if (events.size() != 0) {
            emptyEventSeat();
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                int start = checkStart(event);
                int end = checkEnd(event);
                if (start == -1 || end == -1) continue;
                int order = checkDrawSeatOrder(start, end);
                if (order == -1) continue;
                occupySeat(start, end, order);
                paintBackground.setColor(event.color);
                canvas.save();
                canvas.translate(0, eventHeight * (order + 1) / 10);
                canvas.drawRoundRect(start * dayWidth + 5, order * eventHeight, (end + 1) * dayWidth - 5, (order + 1) * eventHeight, eventHeight / 5, eventHeight / 5, paintBackground);
                canvas.drawText(event.context, (start * dayWidth + (end + 1) * dayWidth) / 2, order * eventHeight + eventHeight / 2 + adjPaintEvent, paintEvent);
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

    public void setToday(int today) {
        if (today >= 0 && today <= 6)
            this.today = today;
        else
            today = -1;
    }

    private void drawWeek(Canvas canvas) {
        canvas.save();
        for (int i = 0; i < 7; i++) {
            if (i == 5)
                paintWeek.setColor(getResources().getColor(R.color.Saturday));
            else if (i == 6)
                paintWeek.setColor(getResources().getColor(R.color.Sunday));
            else
                paintWeek.setColor(getResources().getColor(R.color.Weekday));
            switch (weekOfMonth) {
                case 1:
                    paintWeek.setAlpha(100);
                    if (weekdays[i] < 10)
                        paintWeek.setAlpha(255);
                    drawDayText(canvas, i);
                    canvas.translate(dayWidth, 0);
                    break;
                case 5:
                    paintWeek.setAlpha(255);
                    if (weekdays[i] < 10)
                        paintWeek.setAlpha(100);
                    drawDayText(canvas, i);
                    canvas.translate(dayWidth, 0);
                    break;
                default:
                    paintWeek.setAlpha(255);
                    drawDayText(canvas, i);
                    canvas.translate(dayWidth, 0);
                    break;
            }

        }
        canvas.restore();
    }

    private void drawDayText(Canvas canvas, int i) {
        if (i == today) {
            Paint paint = new Paint();
            paint.setColor(Data.getInstance().themeColor);
            canvas.drawRect(0, 0, dayWidth, weekHeight, paint);
        }
        canvas.drawText(String.valueOf(weekdays[i]), dayWidth / 2, weekHeight / 2 + adjPaintWeek, paintWeek);
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

    private int[] getDayInfo(int day) {
        int year = this.year;
        int month = this.month;
        Calendar calendar = Calendar.getInstance();
        if (weekOfMonth == 1 && day > 10)
            calendar.set(year, month - 1, weekdays[day]);
        else if (weekOfMonth == 5 && day < 10)
            calendar.set(year, month + 1, weekdays[day]);
        else
            calendar.set(year, month, weekdays[day]);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new int[]{year, month, weekOfMonth, dayOfMonth, hour, minute};
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
        public void onShowPress(MotionEvent e) {
            Log.d("Seamas", "onShowPress");
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Intent intent = new Intent();
            int[] dayInfo = getDayInfo(press);
            intent.putExtra("dayInfo", dayInfo);
            intent.setClass(context, AddEventActivity.class);
            context.startActivity(intent);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            press = checkClickRegion(e.getX());
            invalidate();
            return true;
        }
    }
}
