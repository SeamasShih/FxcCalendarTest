package com.honhai.foxconn.fxccalendar.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Data;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.calendar.toolbar.BottomBar;
import com.honhai.foxconn.fxccalendar.views.calendar.toolbar.TopBar;
import com.honhai.foxconn.fxccalendar.views.calendar.pager.Adapter;
import com.honhai.foxconn.fxccalendar.views.calendar.pager.Pager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StartActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
    private Data data = Data.getInstance();
    private ViewPager viewPager;
    private TopBar topBar;
    private BottomBar bottomBar;
    private TextView title, subtitle;
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

        setViewPager();
        setTopBar();
    }

    @Override
    protected void onResume() {
        title.setTextColor(Data.getInstance().themeColor);
        super.onResume();
    }

    private void setTopBar() {
        topBar = findViewById(R.id.topBar);
        topBar.setOnClickListener(v -> rollback());
        title = findViewById(R.id.title);
        setTitleText();
    }

    private void setTitleText() {
        String m = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.UK);
        title.setText(m + " " + calendar.get(Calendar.YEAR));
    }

    private void setViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new MySimpleOnPageListener());
        viewPager.addOnAdapterChangeListener((viewPager, pagerAdapter, pagerAdapter1) -> viewPager.setCurrentItem(1, false));
        viewPager.setAdapter(getAdapter());
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        viewPager.setAdapter(getAdapter());
        setTitleText();
    }

    private void prevMonth() {
        calendar.add(Calendar.MONTH, -1);
        viewPager.setAdapter(getAdapter());
        setTitleText();
    }

    private void rollback() {
        calendar.set(year, month, dayOfMonth);
        viewPager.setAdapter(getAdapter());
        setTitleText();
    }


    private Adapter getAdapter() {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        List<Pager> list = new ArrayList<>();
        calendar.add(Calendar.MONTH, -1);
        list.add(new Pager(this, calendar));
        calendar.set(year, month, dayOfMonth);
        list.add(new Pager(this, calendar));
        calendar.add(Calendar.MONTH, 1);
        list.add(new Pager(this, calendar));
        calendar.set(year, month, dayOfMonth);
        return new Adapter(list);
    }

    private class MySimpleOnPageListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 0) {
                int cur = viewPager.getCurrentItem();
                if (cur == 0)
                    prevMonth();
                else if (cur == 2)
                    nextMonth();
            }
        }
    }
}
