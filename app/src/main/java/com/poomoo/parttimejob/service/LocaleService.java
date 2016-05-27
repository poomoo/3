/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.poomoo.commlib.LogUtils;
import com.poomoo.parttimejob.application.MyApplication;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/26 14:42.
 */
public class LocaleService extends Service {
    private String TAG = "LocaleService";
    private MyApplication application;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        application = (MyApplication) getApplication();
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        mLocationClient.start();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 实现实位回调监听
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 5 * 60 * 1000;

        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setPriority(LocationClientOption.GpsFirst);    // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        mLocationClient.setLocOption(option);
        LogUtils.d(TAG, "initLocation");
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            LogUtils.d(TAG, "location.getLongitude():" + location.getLongitude() + "location.getLatitude():"
//                    + location.getLatitude() + "location.getCity():" + location.getCity());
            application.setLat(location.getLatitude());
            application.setLng(location.getLongitude());
            application.setCurrCity(location.getCity());
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
    }
}
