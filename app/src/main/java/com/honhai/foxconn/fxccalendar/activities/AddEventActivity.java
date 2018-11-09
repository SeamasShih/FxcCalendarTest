package com.honhai.foxconn.fxccalendar.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.data.Data;

public class AddEventActivity extends AppCompatActivity {

    int year, month,weekOfMonth, dayOfMonth, hour, minute;
    TextView startWMDY, startHM;
    TextView endWMDY, endHM;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int[] dayInfo = intent.getIntArrayExtra("dayInfo");
        if (dayInfo != null) {
            year = dayInfo[0];
            month = dayInfo[1];
            weekOfMonth = dayInfo[2];
            dayOfMonth = dayInfo[3];
            hour = dayInfo[4];
            minute = dayInfo[5];
        }
        setContentView(R.layout.activity_add_event);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        findViews();
        setDayText();
        setColor();
    }

    private void setColor() {
        int color = Data.getInstance().themeColor;
        editText.setHintTextColor(Color.argb(100,Color.red(color),Color.green(color),Color.blue(color)));
        editText.setTextColor(color);
    }

    private void setDayText() {
        startWMDY.setText(year + " " + month + "/" + dayOfMonth);
        startHM.setText(hour + " : " + minute);
        endWMDY.setText(year + " " + month + "/" + dayOfMonth);
        endHM.setText(hour + " : " + minute);
    }

    private void findViews() {
        startWMDY = findViewById(R.id.startWMDY);
        startHM = findViewById(R.id.startHM);
        endWMDY = findViewById(R.id.endWMDY);
        endHM = findViewById(R.id.endHM);
        editText = findViewById(R.id.edit);
    }
}
