package com.honhai.foxconn.fxccalendar.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.CalendarWeekItem;

import java.util.ArrayList;
import java.util.Calendar;

public class StartActivity extends AppCompatActivity {

    private Calendar calendar = Calendar.getInstance();
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

        findViews();
        setDays();
        distributeEvent();
    }

    private void distributeEvent() {
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

    private void setDays() {
        int[][] monthDays = getMonthDays(year, month, dayOfMonth, dayOfWeek, weekOfMonth);
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
    }

    private int[][] getMonthDays(int year, int month, int dayOfMonth, int dayOfWeek, int weekOfMonth) {
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


    private void findViews() {
        w1 = findViewById(R.id.w1);
        w2 = findViewById(R.id.w2);
        w3 = findViewById(R.id.w3);
        w4 = findViewById(R.id.w4);
        w5 = findViewById(R.id.w5);
    }
}
