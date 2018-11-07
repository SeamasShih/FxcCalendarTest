package com.honhai.foxconn.fxccalendar.activities;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.CalendarLayout;
import com.honhai.foxconn.fxccalendar.views.CalendarWeekItem;
import com.honhai.foxconn.fxccalendar.views.pager.Adapter;
import com.honhai.foxconn.fxccalendar.views.pager.Pager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
    private CalendarLayout calendarLayout;
    private ViewPager viewPager;
    private CalendarWeekItem w1, w2, w3, w4, w5;
    private int year = calendar.get(Calendar.YEAR);
    private int month = calendar.get(Calendar.MONTH);
    private int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
    private int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    private int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = findViewById(R.id.viewPager);
        List<Pager> list = new ArrayList<>();
        list.add(new Pager(this,calendar));
        list.add(new Pager(this,calendar));
        list.add(new Pager(this,calendar));
        viewPager.setAdapter(new Adapter(list));
    }

}
