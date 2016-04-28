/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.JobTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 身高
 * 作者: 李苜菲
 * 日期: 2016/4/11 14:58.
 */
public class ProvincePopUpWindow extends PopupWindow {
    private View mMenuView;
    private ListView list_type;
    private Button confirmBtn;
    public JobTypeAdapter adapter;
    private String selected;

    public ProvincePopUpWindow(final Activity context, final List<String> stringList, final SelectCategory selectCategory) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_resume, null);
        list_type = (ListView) mMenuView.findViewById(R.id.list_type);
        confirmBtn = (Button) mMenuView.findViewById(R.id.btn_resumeConfirm);

        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        list_type.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels * 6 / 10));

        adapter = new JobTypeAdapter(context, stringList.size());
        list_type.setAdapter(adapter);
        adapter.addItems(stringList);
        if (stringList.size() > 0)
            selected = stringList.get(0);


        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);

        confirmBtn.setOnClickListener(v -> {
            selectCategory.selectCategory(selected);
            dismiss();
        });

        list_type.setOnItemClickListener((parent, view, position, id) -> {
            selected = stringList.get(position);
            adapter.clearIsCheckedMap();
            adapter.getIsCheckedMap().put(position, true);

            adapter.notifyDataSetChanged();
        });

        mMenuView.setOnTouchListener((v, event) -> {
            int height_top = mMenuView.findViewById(R.id.llayout_resume).getTop();
            int height_bottom = mMenuView.findViewById(R.id.llayout_resume).getBottom();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y < height_top || y > height_bottom) {
                    dismiss();
                }
            }
            return true;
        });

    }

    /**
     *
     */
    public interface SelectCategory {
        /**
         * 把选中的下标通过方法回调回来
         *
         * @param province
         */
        public void selectCategory(String province);
    }
}
