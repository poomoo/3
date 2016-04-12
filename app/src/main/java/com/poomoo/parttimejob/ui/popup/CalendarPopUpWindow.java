/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.custom.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日历选择器
 * 作者: 李苜菲
 * 日期: 2016/4/11 14:58.
 */
public class CalendarPopUpWindow extends PopupWindow {
    private View mMenuView;
    private ImageView closeImg;
    private ImageView lastImg;
    private ImageView nextImg;
    private TextView dateTxt;
    private CalendarView calendar;

    public CalendarPopUpWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_calendar, null);
        calendar = (CalendarView) mMenuView.findViewById(R.id.calendar);
        closeImg = (ImageView) mMenuView.findViewById(R.id.img_close);
        lastImg = (ImageView) mMenuView.findViewById(R.id.img_month_last);
        nextImg = (ImageView) mMenuView.findViewById(R.id.img_month_next);
        dateTxt = (TextView) mMenuView.findViewById(R.id.txt_currDate);

        closeImg.setOnClickListener(itemsOnClick);
        lastImg.setOnClickListener(itemsOnClick);
        nextImg.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

//        mMenuView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height_top = mMenuView.findViewById(R.id.llayout_calendar).getTop();
//                int height_bottom = mMenuView.findViewById(R.id.llayout_calendar).getBottom();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height_top || y > height_bottom) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });

    }

    public CalendarView getCalendar() {
        return calendar;
    }

    public TextView getDateTxt() {
        return dateTxt;
    }
}
