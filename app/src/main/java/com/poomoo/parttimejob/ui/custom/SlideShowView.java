package com.poomoo.parttimejob.ui.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.listener.AdvertisementListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 */

public class SlideShowView extends FrameLayout {

    private String TAG = "SlideShowView";

    private final MyPagerAdapter myPagerAdapter;
    private final MyPageChangeListener myPageChangeListener;

    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    private String[] imageUrls;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    private AdvertisementListener listener;


    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
//            LogUtils.i(TAG, "currentItem:" + currentItem);
            if (currentItem == 0)
                viewPager.setCurrentItem(currentItem, false);
            else
                viewPager.setCurrentItem(currentItem, true);
        }

    };
    private float ratio = 2f;
    private boolean isfirst = true;

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        myPagerAdapter = new MyPagerAdapter();
        myPageChangeListener = new MyPageChangeListener();
        initData();
        if (isAutoPlay)
            startPlay();
    }

    public void setPics(String[] urls, AdvertisementListener listener) {
        this.imageUrls = urls;
        this.listener = listener;
        initData();
        if (isAutoPlay) {
            startPlay();
        }
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
        }
    }


    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<>();
        // 一步任务获取图片
//        new GetListTask().execute("");
        initUI(context);
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        LogUtils.d(TAG, "initUI:");
        if (imageUrls == null || imageUrls.length == 0)
            return;
        this.removeAllViews();
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        dotViewsList = new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view = new ImageView(context);
            view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
//            view.setAdjustViewBounds(true);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(myPageChangeListener);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            LogUtils.i(TAG, "destroyItem:" + position);
//            container.removeView(imageViewsList.get(position));
            container.removeView((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            LogUtils.i(TAG, "instantiateItem:" + position);
            ImageView imageView = imageViewsList.get(position);
            Glide.with(context).load(imageUrls[position]).into(imageView);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onResult(position);
                }
            });
            container.addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
//            LogUtils.i(TAG, "imageViewsList.size():" + imageViewsList.size());
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0, false);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1, false);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
//            LogUtils.i(TAG, "dotViewsList:" + dotViewsList.size());
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
//                    (dotViewsList.get(pos)).setBackgroundResource(R.drawable.ic_dot_selected);
                } else {
//                    (dotViewsList.get(i)).setBackgroundResource(R.drawable.ic_dot_normal);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }


    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }


    /**
     * 异步任务,获取数据
     */
    class GetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片

                imageUrls = new String[]{
                        "http://img1.3lian.com/img013/v4/96/d/53.jpg",
                        "http://p1.gexing.com/G1/M00/2B/71/rBACE1NghIvyY0VfAAC2vmi1Uzo574.jpg",
                        "http://img1.3lian.com/img013/v4/96/d/51.jpg",
                        "http://img.sc115.com/uploads/sc/psd/140312/1403122.jpg"
                };
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                initUI(context);
            }
        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        LogUtils.i("test", "子dispatchTouchEvent" + ev.getAction());
//        return true;
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        LogUtils.i("test", "子onInterceptTouchEvent" + ev.getAction());
//        return super.onInterceptTouchEvent(ev);
//    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        LogUtils.i("test", "子onTouchEvent" + ev.getAction());
//        return super.onInterceptTouchEvent(ev);
//    }
}