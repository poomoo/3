/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;
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
    @Bind(R.id.rbtn_workPeriod_long)
    RadioButton periodLongRbtn;//工作周期 长
    @Bind(R.id.rbtn_workPeriod_short)
    RadioButton periodShortRbtn;//工作周期 短
    @Bind(R.id.rbtn_workPeriod_weekEnd)
    RadioButton periodWeekendRbtn;//工作周期 周末

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
//        authTBtn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
//            @Override
//            public void onToggle(boolean on) {
//                if (on)
//                    MyUtils.showToast(getApplicationContext(), "认证开启");
//                else
//                    MyUtils.showToast(getApplicationContext(), "认证关闭");
//            }
//        });
        initSex();
        initWorkDay();
        initStartWorkDay();
        initWorkCycle();
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
     * 性別要求
     */
    private void initSex() {
        if (application.getSexReq() == 1)
            manRBtn.setChecked(true);
        else if (application.getSexReq() == 2)
            womanRBtn.setChecked(true);
        else
            sexAllRBtn.setChecked(true);
    }

    /**
     * 上班時間
     */
    private void initWorkDay() {
        if (TextUtils.isEmpty(application.getWorkday()))
            timeAllRbtn.setChecked(true);
        else {
            timeAllRbtn.setChecked(false);
            String temp[] = application.getWorkday().split(",");
            for (int i = 0; i < temp.length; i++) {
                if (temp[i].equals("1"))
                    morningChk.setChecked(true);
                if (temp[i].equals("2"))
                    afternoonChk.setChecked(true);
                if (temp[i].equals("3"))
                    eveningChk.setChecked(true);
            }
        }

    }

    /**
     * 開始工作時間
     */
    private void initStartWorkDay() {
        if (TextUtils.isEmpty(application.getStartWorkDt()))
            startTimeAllRbtn.setChecked(true);
        else {
            startTimeAllRbtn.setChecked(false);
            startWorkTimeTxt.setText(application.getStartWorkDt());
        }
    }

    /**
     * 工作週期
     */
    private void initWorkCycle() {
        if (application.getWorkSycle() == 0)
            periodAllRbtn.setChecked(true);
        else if (application.getWorkSycle() == 1)
            periodLongRbtn.setChecked(true);
        else if (application.getWorkSycle() == 2)
            periodShortRbtn.setChecked(true);
        else if (application.getWorkSycle() == 3)
            periodWeekendRbtn.setChecked(true);
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

        application.setSexReq(0);
        application.setWorkday("");
        application.setStartWorkDt("");
        application.setWorkSycle(0);
    }

    @OnClick({R.id.rbtn_sex_all, R.id.rbtn_sex_man, R.id.rbtn_sex_woman, R.id.rbtn_time_all, R.id.chk_time_morning, R.id.chk_time_afternoon, R.id.chk_time_evening, R.id.rbtn_startTime_assign, R.id.rbtn_startTime_all, R.id.rbtn_workPeriod_all, R.id.rbtn_workPeriod_long, R.id.rbtn_workPeriod_short, R.id.rbtn_workPeriod_weekEnd})
    void checkChanged(View view) {
        switch (view.getId()) {
            case R.id.rbtn_sex_all:
                application.setSexReq(0);
                break;
            case R.id.rbtn_sex_man:
                application.setSexReq(1);
                break;
            case R.id.rbtn_sex_woman:
                application.setSexReq(2);
                break;
            case R.id.rbtn_time_all:
                setOtherChecked();
                application.setWorkday("");
                break;
            case R.id.chk_time_morning:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                application.setWorkday(application.getWorkday() + "1,");
                break;
            case R.id.chk_time_afternoon:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                application.setWorkday(application.getWorkday() + "2,");
                break;
            case R.id.chk_time_evening:
                timeAllRbtn.setChecked(false);
                isAllChecked();
                application.setWorkday(application.getWorkday() + "3,");
                break;
            case R.id.rbtn_startTime_assign:
                calendar();
                break;
            case R.id.rbtn_startTime_all:
                startWorkTimeTxt.setText("不限");
                isAssignTime = false;
                application.setStartWorkDt("");
                break;
            case R.id.rbtn_workPeriod_all:
                application.setWorkSycle(0);
                break;
            case R.id.rbtn_workPeriod_long:
                application.setWorkSycle(1);
                break;
            case R.id.rbtn_workPeriod_short:
                application.setWorkSycle(2);
                break;
            case R.id.rbtn_workPeriod_weekEnd:
                application.setWorkSycle(3);
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
                application.setStartWorkDt(date);
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

    /**
     * 確定
     *
     * @param View
     */
    public void toConfirm(View View) {
        setResult(1);
        finish();
    }
}
