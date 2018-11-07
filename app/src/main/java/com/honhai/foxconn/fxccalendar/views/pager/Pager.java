package com.honhai.foxconn.fxccalendar.views.pager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.views.CalendarLayout;

import java.util.Calendar;

public class Pager extends ConstraintLayout {
    public Pager(Context context, Calendar calendar) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.calendar_pager_item, this);
        CalendarLayout layout = view.findViewById(R.id.calendarLayout);
        layout.findViews();
        layout.setDays(calendar);
        layout.distributeEvent();
    }
}
