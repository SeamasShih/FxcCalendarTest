package com.honhai.foxconn.fxccalendar.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Data;
import com.honhai.foxconn.fxccalendar.data.Event;
import com.honhai.foxconn.fxccalendar.views.add.Toolbar;
import com.honhai.foxconn.fxccalendar.views.add.item.color.ColorItem;

public class AddEventActivity extends AppCompatActivity {

    private TextView startWMDY, startHM;
    private TextView endWMDY, endHM;
    private EditText editText;
    private Toolbar toolbar;
    private ColorItem colorItem;
    private int prevColor;
    private int currColor;
    private int[] startTime = new int[6];
    private int[] endTime = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int[] dayInfo = intent.getIntArrayExtra("dayInfo");
        if (dayInfo != null) {
            for (int i = 0; i < dayInfo.length; i++) {
                startTime[i] = dayInfo[i];
                endTime[i] = dayInfo[i];
            }
        }
        setContentView(R.layout.activity_add_event);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViews();
        setDayText();
        setToolbar();
        setColor();
        setTimeSettingListener();
    }

    private void setTimeSettingListener() {
        startWMDY.setOnClickListener(v -> {
                    DatePickerDialog dp = new DatePickerDialog(
                            this,
                            (view, year, monthOfYear, dayOfMonth) -> {
                                startTime[0] = year;
                                startTime[1] = monthOfYear;
                                startTime[3] = dayOfMonth;
                                setDayText();
                            },
                            startTime[0], startTime[1], startTime[3]);
                    dp.show();
                }
        );
        startHM.setOnClickListener(v -> {
                    TimePickerDialog dp = new TimePickerDialog(
                            this,
                            (view, hourOfDay, minute) -> {
                                startTime[4] = hourOfDay;
                                startTime[5] = minute;
                                setDayText();
                            },
                            startTime[4], startTime[5], true);
                    dp.show();
                }
        );
        endWMDY.setOnClickListener(v -> {
                    DatePickerDialog dp = new DatePickerDialog(
                            this,
                            (view, year, monthOfYear, dayOfMonth) -> {
                                endTime[0] = year;
                                endTime[1] = monthOfYear;
                                endTime[3] = dayOfMonth;
                                setDayText();
                            },
                            endTime[0], endTime[1], endTime[3]);
                    dp.show();
                }
        );
        endHM.setOnClickListener(v -> {
                    TimePickerDialog dp = new TimePickerDialog(
                            this,
                            (view, hourOfDay, minute) -> {
                                endTime[4] = hourOfDay;
                                endTime[5] = minute;
                                setDayText();
                            },
                            endTime[4], endTime[5], true);
                    dp.show();
                }
        );
    }

    @SuppressLint("NewApi")
    private void setToolbar() {
        toolbar.inflateMenu(R.menu.add_event_toolbar_widget);
        toolbar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.done) {
                if (editText.getText().length() == 0)
                    Toast.makeText(this, "Event name must not be empty!", Toast.LENGTH_SHORT).show();
                else if (!timeIsAvailable())
                    Toast.makeText(this, "Time setting is error!", Toast.LENGTH_SHORT).show();
                else {
                    Data.getInstance().events.add(new Event(
                            editText.getText().toString(), startTime[0], startTime[1], startTime[3], startTime[4], startTime[5],
                            endTime[0], endTime[1], endTime[3], endTime[4], endTime[5], currColor
                    ));
                    finish();
                }
            }
            return true;
        });
    }

    private boolean timeIsAvailable() {
        for (int i = 0; i < startTime.length; i++) {
            if (i == 2) continue;
            if (startTime[i] < endTime[i])
                return true;
            else if (startTime[i] > endTime[i])
                return false;
            else if (i == startTime.length - 1)
                return false;
        }
        return true;
    }

    private void setColor() {
        setColor(Data.getInstance().themeColor);
        prevColor = Data.getInstance().themeColor;
    }

    public void setColor(int color) {
        editText.setHintTextColor(Color.argb(100, Color.red(color), Color.green(color), Color.blue(color)));
        editText.setTextColor(color);
        Drawable icon = toolbar.getMenu().getItem(0).getIcon();
        icon.setTintList(ColorStateList.valueOf(color));
        toolbar.getMenu().getItem(0).setIcon(icon);
        startWMDY.setTextColor(color);
        startHM.setTextColor(color);
        endWMDY.setTextColor(color);
        endHM.setTextColor(color);
        colorItem.setColor(color);
        currColor = color;
    }

    public void refreshPrevColor() {
        prevColor = currColor;
    }

    public void recoverColor() {
        currColor = prevColor;
        setColor(currColor);
    }

    @SuppressLint("SetTextI18n")
    private void setDayText() {
        startWMDY.setText(startTime[0] + " " + (startTime[1]+1 > 9 ? startTime[1]+1 : "0" + startTime[1]+1) + "/" + (startTime[3] > 9 ? startTime[3] : "0" + startTime[3]));
        startHM.setText((startTime[4] > 9 ? startTime[4] : "0" + startTime[4]) + " : " + (startTime[5] > 9 ? startTime[5] : "0" + startTime[5]));
        endWMDY.setText(endTime[0] + " " + (endTime[1]+1 > 9 ? endTime[1]+1 : "0" + endTime[1]+1) + "/" + (endTime[3] > 9 ? endTime[3] : "0" + endTime[3]));
        endHM.setText((endTime[4] > 9 ? endTime[4] : "0" + endTime[4]) + " : " + (endTime[5] > 9 ? endTime[5] : "0" + endTime[5]));
    }

    private void findViews() {
        startWMDY = findViewById(R.id.startWMDY);
        startHM = findViewById(R.id.startHM);
        endWMDY = findViewById(R.id.endWMDY);
        endHM = findViewById(R.id.endHM);
        editText = findViewById(R.id.edit);
        toolbar = findViewById(R.id.tool);
        colorItem = findViewById(R.id.colorItem);
    }
}
