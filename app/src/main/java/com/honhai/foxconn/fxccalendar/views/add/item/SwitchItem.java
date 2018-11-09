package com.honhai.foxconn.fxccalendar.views.add.item;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.honhai.foxconn.fxccalendar.R;

public class SwitchItem extends SampleView {

    private ImageView imageView;
    private MySwitch mSwitch;

    public SwitchItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int color){
        findViews();
        imageView.setImageTintList(ColorStateList.valueOf(color));
        mSwitch.setEventColor(color);
    }

    public void findViews() {
        if (imageView == null)
            imageView = findViewById(R.id.image);
        if (mSwitch == null)
            mSwitch = findViewById(R.id.swi);
    }
}
