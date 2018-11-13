package com.honhai.foxconn.fxccalendar.views.add.item.color;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.honhai.foxconn.fxccalendar.R;
import com.honhai.foxconn.fxccalendar.activities.AddEventActivity;
import com.honhai.foxconn.fxccalendar.views.add.item.SampleView;
import com.honhai.foxconn.fxccalendar.views.add.popup.color.ColorPopupWindow;
import com.honhai.foxconn.fxccalendar.views.add.popup.widget.ColorChoiceDot;
import com.honhai.foxconn.fxccalendar.views.add.popup.widget.ColorPreviewBar;

import java.util.List;

public class ColorItem extends SampleView {

    private Context context;
    private ImageView imageView;
    private TextView textView;
    private ImageView indicator;
    private boolean isConfirm = false;

    public ColorItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnClickListener(v -> showPopupWindow());
    }

    private void showPopupWindow() {
        isConfirm = false;
        View view = LayoutInflater.from(context).inflate(R.layout.add_event_color_popup_window, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, OrientationHelper.VERTICAL, false));
        recyclerView.setAdapter(new Adapter(recyclerView));
        TextView confirm = view.findViewById(R.id.confirm);
        ColorPopupWindow popupWindow = new ColorPopupWindow(view);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        confirm.setOnClickListener(v -> {
            isConfirm = true;
            popupWindow.dismiss();
        });
        popupWindow.setOnDismissListener(() -> {
            if (isConfirm)
                ((AddEventActivity) context).refreshPrevColor();
            else
                ((AddEventActivity) context).recoverColor();
        });
    }

    @Override
    public void setColor(int color) {
        if (imageView == null)
            findViews();
        imageView.setBackgroundTintList(ColorStateList.valueOf(color));
        textView.setTextColor(color);
        indicator.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public void findViews() {
        imageView = findViewById(R.id.colorImage);
        textView = findViewById(R.id.colorText);
        indicator = findViewById(R.id.colorIndicator);
    }

    private class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int[] colors;
        private int checked = -1;
        private RecyclerView recyclerView;

        private Adapter(RecyclerView recyclerView) {
            colors = getResources().getIntArray(R.array.eventColorArray);
            this.recyclerView = recyclerView;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.add_event_color_popup_window_recucler_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            ColorPreviewBar bar = ((ViewHolder) viewHolder).bar;
            TextView textView = ((ViewHolder) viewHolder).textView;
            ColorChoiceDot dot = ((ViewHolder) viewHolder).dot;
            View parent = ((ViewHolder) viewHolder).parent;

            bar.setColor(colors[position]);
            textView.setTextColor(colors[position]);
            dot.setColor(colors[position]);
            dot.setChosen(checked == position);
            parent.setOnClickListener(v -> {
                if (checked != position) {
                    View view = recyclerView.getLayoutManager().findViewByPosition(checked);
                    if (view != null) {
                        ((ColorChoiceDot) view.findViewById(R.id.choice)).setChosen(false);
                    }
                    checked = position;
                    dot.setChosen(true);
                    ((AddEventActivity) context).setColor(colors[position]);
                }
            });
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public int getItemCount() {
            return colors.length;
        }

        private class ViewHolder extends RecyclerView.ViewHolder {

            private ColorPreviewBar bar;
            private TextView textView;
            private ColorChoiceDot dot;
            private View parent;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                bar = itemView.findViewById(R.id.color);
                textView = itemView.findViewById(R.id.label);
                dot = itemView.findViewById(R.id.choice);
                parent = itemView;
            }
        }
    }
}
