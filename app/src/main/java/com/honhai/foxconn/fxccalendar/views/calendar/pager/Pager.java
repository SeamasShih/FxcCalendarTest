package com.honhai.foxconn.fxccalendar.views.calendar.pager;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.calendar.CalendarLayout;
import com.honhai.foxconn.fxccalendar.views.calendar.CalendarLayoutSixWeek;

import java.util.ArrayList;
import java.util.Calendar;

public class Pager extends ConstraintLayout {
    public Pager(Context context, Calendar calendar) {
        super(context);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, maxDay);
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        if (week == 6) {
            View view = LayoutInflater.from(context).inflate(R.layout.calendar_pager_item_six_week, this);
            CalendarLayoutSixWeek layout = view.findViewById(R.id.calendarLayout);
            layout.findViews();
            layout.setDays(calendar);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.calendar_pager_item, this);
            CalendarLayout layout = view.findViewById(R.id.calendarLayout);
            layout.findViews();
            layout.setDays(calendar);
        }
    }
}
