package com.honhai.foxconn.fxccalendar.views.add;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Switch;

import com.honhai.foxconn.fxccalendar.R;

public class ChoiceItem extends ConstraintLayout {

    private ImageView imageView;
    private MySwitch mSwitch;

    public ChoiceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setColor(int color){
        findViews();
        imageView.setImageTintList(ColorStateList.valueOf(color));
        mSwitch.setEventColor(color);
    }

    private void findViews() {
        if (imageView == null)
            imageView = findViewById(R.id.image);
        if (mSwitch == null)
            mSwitch = findViewById(R.id.swi);
    }
}
