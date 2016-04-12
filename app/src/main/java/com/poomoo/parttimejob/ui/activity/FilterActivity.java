/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.custom.CalendarView;
import com.poomoo.parttimejob.ui.popup.CalendarPopUpWindow;
import com.zcw.togglebutton.ToggleButton;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 筛选
 * 作者: 李苜菲
 * 日期: 2016/4/9 17:03.
 */
public class FilterActivity extends BaseActivity {
    @Bind(R.id.tBtn_authJob)
    ToggleButton authTBtn;
    @Bind(R.id.tBtn_soonJob)
    ToggleButton soonTBtn;
    @Bind(R.id.rbtn_sex_all)
    RadioButton sexAllRBtn;//性别 不限
    @Bind(R.id.rbtn_sex_man)
    RadioButton manRBtn;
    @Bind(R.id.rbtn_sex_woman)
    RadioButton womanRBtn;
    @Bind(R.id.chk_time_morning)
    CheckBox morningChk;
    @Bind(R.id.chk_time_afternoon)
    CheckBox afternoonChk;
    @Bind(R.id.chk_time_evening)
    CheckBox eveningChk;
    @Bind(R.id.rbtn_time_all)
    RadioButton timeAllRbtn;//上班时间 不限
    @Bind(R.id.rbtn_startTime_all)
    RadioButton startTimeAllRbtn;//开始工作时间 不限
    @Bind(R.id.txt_startWorkTime)
    TextView startWorkTimeTxt;//指定开始工作时间
    @Bind(R.id.rbtn_workPeriod_all)
    RadioButton periodAllRbtn;//工作周期 不限

    private TextView dateTxt;
    private CalendarView calendar;

    private CalendarPopUpWindow calendarPopUpWindow;
    private String[] ya;
    private boolean isAssignTime = false;//指定开始工作日期


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleBar();
        authTBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on)
                    MyUtils.showToast(getApplicationContext(), "认证开启");
                else
                    MyUtils.showToast(getApplicationContext(), "认证关闭");
            }
        });
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_filter);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_filter;
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.rightTxt.setText("重置");
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
    }

    /**
     * 重置
     *
     * @param view
     */
    public void toDo(View view) {
        sexAllRBtn.setChecked(true);
        timeAllRbtn.setChecked(true);
        startTimeAllRbtn.setChecked(true);
        periodAllRbtn.setChecked(true);
        morningChk.setChecked(false);
        afternoonChk.setChecked(false);
        eveningChk.setChecked(false);
        authTBtn.setToggleOff();
        soonTBtn.setToggleOff();
        startWorkTimeTxt.setText("不限");
    }

    @OnClick({R.id.rbtn_time_all, R.id.chk_time_morning, R.id.chk_time_afternoon, R.id.chk_time_evening, R.id.rbtn_startTime_assign, R.id.rbtn_startTime_all})
    void checkChanged(View view) {
        switch (view.getId()) {
            case R.id.rbtn_time_all:
                setOtherChecked();
                break;
            case R.id.chk_time_morning:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                break;
            case R.id.chk_time_afternoon:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                break;
            case R.id.chk_time_evening:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                break;
            case R.id.rbtn_startTime_assign:
                calendar();
                break;
            case R.id.rbtn_startTime_all:
                startWorkTimeTxt.setText("不限");
                isAssignTime = false;
                break;
        }
    }

    void setOtherChecked() {
        morningChk.setChecked(false);
        afternoonChk.setChecked(false);
        eveningChk.setChecked(false);
        LogUtils.i(TAG, "setOtherChecked:" + timeAllRbtn.isChecked());
    }

    void isAllChecked() {
        LogUtils.i(TAG, "isAllChecked:" + morningChk.isChecked() + afternoonChk.isChecked() + eveningChk.isChecked());

        if ((morningChk.isChecked() && afternoonChk.isChecked() && eveningChk.isChecked()) || (!morningChk.isChecked() && !afternoonChk.isChecked() && !eveningChk.isChecked())) {
            timeAllRbtn.setChecked(true);
            morningChk.setChecked(false);
            afternoonChk.setChecked(false);
            eveningChk.setChecked(false);
        }
    }

    void calendar() {
        calendarPopUpWindow = new CalendarPopUpWindow(this, itemsOnClick);
        // 显示窗口
        calendarPopUpWindow.showAtLocation(this.findViewById(R.id.llayout_filter),
                Gravity.CENTER, 0, 0); // 设置layout在genderWindow中显示的位置
        dateTxt = calendarPopUpWindow.getDateTxt();
        calendar = calendarPopUpWindow.getCalendar();

        String startWorkTime = startWorkTimeTxt.getText().toString();
        if (startWorkTime.length() > 2)
            try {
                calendar.setDownDate(MyUtils.ConvertToDate(startWorkTime));
                calendar.setCalendarData(MyUtils.ConvertToDate(startWorkTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        ya = calendar.getYearAndMonth().split("-");
        dateTxt.setText(ya[0] + "年" + ya[1] + "月");
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date downDate) {
                String date = calendar.getYearAndMonth();
                startWorkTimeTxt.setText(date);
                ya = date.split("-");
                dateTxt.setText(ya[0] + "年" + ya[1] + "月");
                isAssignTime = true;
            }
        });
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.img_close:
                    calendarPopUpWindow.dismiss();
                    if (!isAssignTime)
                        startTimeAllRbtn.setChecked(true);
                    break;
                case R.id.img_month_last:
                    String leftYearAndMonth = calendar.clickLeftMonth();
                    ya = leftYearAndMonth.split("-");
                    dateTxt.setText(ya[0] + "年" + ya[1] + "月");
                    break;
                case R.id.img_month_next:
                    //点击下一月
                    String rightYearAndMonth = calendar.clickRightMonth();
                    ya = rightYearAndMonth.split("-");
                    dateTxt.setText(ya[0] + "年" + ya[1] + "月");
                    break;
            }
        }
    };
}
