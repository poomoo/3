/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.poomoo.commlib.DensityUtils;
import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.FreeTimeView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 求职意向设置
 * 作者: 李苜菲
 * 日期: 2016/4/18 11:53.
 */
public class JobIntentionActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.freeTimeView)
    FreeTimeView freeTimeView;
    @Bind(R.id.llayout_zone)
    LinearLayout zoneLlayout;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;

    private String[] zone = {"不限", "南明", "修文", "息烽1", "息烽2", "息烽3", "息烽4", "息烽5", "息烽6", "息烽7", "息烽8"};
    final int sWrapContent = LinearLayout.LayoutParams.WRAP_CONTENT;
    final int sMatchParent = LinearLayout.LayoutParams.MATCH_PARENT;
    final static int ID = 8888;
    int id[];
    private SparseArray<RadioGroup> zoneArray = new SparseArray<>();
    private int len;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        addView();
        final LayoutInflater mInflater = LayoutInflater.from(this);
        mFlowLayout.setAdapter(new TagAdapter<String>(zone) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.tv, mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobIntention);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_job_intention;
    }

    private void addView() {
        len = zone.length % 4 == 0 ? zone.length / 4 : zone.length / 4 + 1;
        id = new int[len];
        LogUtils.d(TAG, "addView------>" + len);
        for (int i = 0; i < len; i++) {
            RadioGroup radioGroup = new RadioGroup(this);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(sMatchParent, sWrapContent);
            params.setMargins(DensityUtils.dp2px(this, 20), DensityUtils.dp2px(this, 10), DensityUtils.dp2px(this, 20), 0);
            radioGroup.setLayoutParams(params);
            radioGroup.setGravity(Gravity.CENTER_HORIZONTAL);
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
            radioGroup.setTag(i + 100);
            for (int j = i * 4; j < i * 4 + 4; j++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setLayoutParams(new RadioGroup.LayoutParams(0, sWrapContent, 1));
                radioButton.setTag(j + 200);
                if (j >= zone.length)
                    radioButton.setVisibility(View.INVISIBLE);
                else {
                    radioButton.setBackgroundResource(R.drawable.selector_filter);
                    radioButton.setButtonDrawable(null);
                    radioButton.setText(zone[j]);
                    radioButton.setTextColor(getResources().getColor(R.color.selector_text_filter_background));
                    radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    radioButton.setGravity(Gravity.CENTER);
                    if (j == 0)
                        radioButton.setChecked(true);
                }
                radioGroup.addView(radioButton);
                if (j != i * 4 + 4 - 1) {
                    TextView tv = new TextView(this);
                    tv.setLayoutParams(new RadioGroup.LayoutParams(0, sWrapContent, 0.2f));
                    radioGroup.addView(tv);
                }
                radioGroup.setOnCheckedChangeListener(this);
            }
            zoneArray.put(i, radioGroup);
            zoneLlayout.addView(radioGroup);
            LogUtils.d(TAG, "addView:" + i);
        }
    }

    boolean changeGroup = false;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        LogUtils.d(TAG, "id:" + group.getCheckedRadioButtonId() + "checkedId:" + checkedId + "isChecked:" + group.getChildAt(checkedId));
        if (!changeGroup) {
            for (int i = 0; i < len; i++) {
                LogUtils.d(TAG, "i:" + i + "onCheckedChanged:" + group.getTag() + "zoneArray.get(i).getId():" + zoneArray.get(i).getTag());
                if (group.getTag() != zoneArray.get(i).getTag()) {
                    LogUtils.d(TAG, "i:" + i + "进去清除状态");
                    changeGroup = true;
                    group.check(-1);
                    LogUtils.d(TAG, "i:" + i + "清除状态");
                }
            }
            changeGroup = false;
        }

    }
}
