package com.poomoo.commlib;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * 倒计时器
 * 作者: 李苜菲
 * 日期: 2015/11/16 15:49.
 */
public class TimeCountDownUtil extends CountDownTimer {
    private String TAG = this.getClass().getSimpleName();
    private TextView textView;

    public TimeCountDownUtil(long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.textView = textView;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);// 设置不能点击
        textView.setText(millisUntilFinished / 1000 + "s");// 设置倒计时时间
        textView.setTextColor(Color.GRAY);
    }

    @Override
    public void onFinish() {
        textView.setClickable(true);// 设置点击
        textView.setText("重新获取");
        textView.setTextColor(Color.parseColor("#0db09b"));
    }

}