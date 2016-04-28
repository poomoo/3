/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.popup;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.BaseListAdapter;
import com.poomoo.parttimejob.adapter.JobTypeAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 日历选择器
 * 作者: 李苜菲
 * 日期: 2016/4/11 14:58.
 */
public class TypePopUpWindow extends PopupWindow {
    private View mMenuView;
    private ListView list_type;
    private Button confirmBtn;
    private JobTypeAdapter adapter;
    private List<String> selected;

    public TypePopUpWindow(Activity context, final List<String> stringList, final SelectCategory selectCategory) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_type, null);
        list_type = (ListView) mMenuView.findViewById(R.id.list_type);
        confirmBtn = (Button) mMenuView.findViewById(R.id.btn_typeConfirm);
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm); // 获取手机屏幕的大小
        list_type.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dm.heightPixels * 6 / 10));

        adapter = new JobTypeAdapter(context, stringList.size());
        list_type.setAdapter(adapter);
        adapter.addItems(stringList);


        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
//        this.setAnimationStyle(R.style.mypopwindow_anim_style);

        confirmBtn.setOnClickListener(v -> {
            selected = new ArrayList<>();
            for (int i = 0; i < adapter.getIsCheckedMap().size(); i++) {
                if (adapter.getIsCheckedMap().get(i)) {
                    String temp[] = stringList.get(i).split("#");
                    selected.add(temp.length == 2 ? temp[1] : "");
                }

            }
            selectCategory.selectCategory(selected);
            dismiss();
        });

        list_type.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0)
                adapter.initIsCheckedMap();
            else {
                JobTypeAdapter.ViewHolder holder = (JobTypeAdapter.ViewHolder) view.getTag();
                // 改变CheckBox的状态
                holder.checkBox.toggle();
                // 将CheckBox的选中状况记录下来
                adapter.getIsCheckedMap().put(position, holder.checkBox.isChecked());

                if (!adapter.hasChecked())
                    adapter.getIsCheckedMap().put(0, true);
                else
                    adapter.getIsCheckedMap().put(0, false);

                if (adapter.isAllChecked())
                    adapter.initIsCheckedMap();

            }
            adapter.notifyDataSetChanged();
        });

        mMenuView.setOnTouchListener((v, event) -> {
            int height_top = mMenuView.findViewById(R.id.llayout_type).getTop();
            int height_bottom = mMenuView.findViewById(R.id.llayout_type).getBottom();
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
         * @param type 选中的下标
         */
        public void selectCategory(List<String> type);
    }
}
