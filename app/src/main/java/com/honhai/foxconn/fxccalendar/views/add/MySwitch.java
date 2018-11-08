package com.honhai.foxconn.fxccalendar.views.add;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.honhai.foxconn.fxccalendar.R;

public class MySwitch extends Switch {

    int eventColor = getResources().getColor(R.color.themeColor);

    public MySwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setThumbTintList(ColorStateList.valueOf(eventColor));
                    setTrackTintList(ColorStateList.valueOf(eventColor));
                }
                else{
                    setThumbTintList(ColorStateList.valueOf(Color.WHITE));
                    setTrackTintList(ColorStateList.valueOf(Color.BLACK));
                }
            }
        });
    }

    public void setEventColor(int eventColor) {
        this.eventColor = eventColor;
    }
}
