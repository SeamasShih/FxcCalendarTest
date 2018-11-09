package com.honhai.foxconn.fxccalendar.views.calendar.pager;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.calendar.CalendarLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class Pager extends ConstraintLayout {
    public Pager(Context context, Calendar calendar, ArrayList<Event> events) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_pager_item, this);
        CalendarLayout layout = view.findViewById(R.id.calendarLayout);
        layout.findViews();
        layout.setDays(calendar);
    }
}
