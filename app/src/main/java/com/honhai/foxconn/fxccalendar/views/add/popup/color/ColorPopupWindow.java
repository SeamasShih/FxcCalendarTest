package com.honhai.foxconn.fxccalendar.views.add.popup.color;

import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.honhai.foxconn.fxccalendar.R;

public class ColorPopupWindow extends PopupWindow {

    public ColorPopupWindow(View view){
        super(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        );
    }
}
