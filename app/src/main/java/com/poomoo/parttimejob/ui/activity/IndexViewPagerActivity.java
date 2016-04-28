package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.StatusBarUtil;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.adapter.ViewPagerAdapter;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 引导页
 */
public class IndexViewPagerActivity extends BaseActivity implements OnClickListener, OnPageChangeListener, View.OnTouchListener {

    @Bind(R.id.viewpager_viewpager)
    ViewPager vp;

    private ViewPagerAdapter vpAdapter;
    private List<View> views;

    // 引导图片资源
    private static final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private static final int lenth = pics.length;

    // 记录当前选中位置
    private int currentIndex;
    private float x1 = 0;
    private float x2 = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        StatusBarUtil.setTransparent(this);
        initView();
    }

    private void initView() {
        views = new ArrayList<>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            iv.setOnClickListener(this);
            views.add(iv);
        }
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        vp.addOnPageChangeListener(this);
        views.get(lenth - 1).setOnTouchListener(this);
    }

    @Override
    protected String onSetTitle() {
        return null;
    }

    @Override
    protected int onBindLayout() {
        return R.layout.viewpager;
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= lenth) {
            return;
        }
        vp.setCurrentItem(position);
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        currentIndex = arg0;
        LogUtils.d(TAG, "onPageSelected:" + currentIndex);
    }

    @Override
    public void onClick(View v) {
        if (currentIndex < lenth - 1)
            setCurView(currentIndex + 1);
        else {
            openActivity(LoginActivity.class);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指离开的时候
            x2 = event.getX();
            if ((x1 - x2 > 5)) {//向左滑
                openActivity(LoginActivity.class);
                finish();
                return true;
            }
        }
        return false;
    }
}
