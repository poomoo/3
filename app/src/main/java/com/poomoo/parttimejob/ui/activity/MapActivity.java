/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.poomoo.commlib.MyUtils;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.ui.base.BaseActivity;

import butterknife.Bind;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/28 09:26.
 */
public class MapActivity extends BaseActivity {
    @Bind(R.id.mapview)
    MapView mapView;
    private BaiduMap bdMap;

    private double lat;
    private double lng;

    private RoutePlanSearch routePlanSearch;// 路径规划搜索接口

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_jobLine);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_map;
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapview);

        mapView.showZoomControls(false);
        bdMap = mapView.getMap();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(application.getLat(), application.getLng())).zoom(18.0f);
        bdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        routePlanSearch = RoutePlanSearch.newInstance();
        routePlanSearch.setOnGetRoutePlanResultListener(routePlanResultListener);

        lat = getIntent().getDoubleExtra(getString(R.string.intent_lat), 0.0);
        lng = getIntent().getDoubleExtra(getString(R.string.intent_lng), 0.0);
        walkSearch();
    }

    private void walkSearch() {
        WalkingRoutePlanOption walkOption = new WalkingRoutePlanOption();
//        walkOption.from(PlanNode.withCityNameAndPlaceName("北京", startPlace));
//        walkOption.to(PlanNode.withCityNameAndPlaceName("北京", endPlace));
        walkOption.from(PlanNode.withLocation(new LatLng(application.getLat(), application.getLng())));
        walkOption.to(PlanNode.withLocation(new LatLng(lat, lng)));
        routePlanSearch.walkingSearch(walkOption);
    }

    private int totalLine;
    /**
     * 路线规划结果回调
     */
    OnGetRoutePlanResultListener routePlanResultListener = new OnGetRoutePlanResultListener() {

        /**
         * 步行路线结果回调
         */
        @Override
        public void onGetWalkingRouteResult(
                WalkingRouteResult walkingRouteResult) {
            bdMap.clear();
            if (walkingRouteResult == null
                    || walkingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // TODO
                return;
            }
            if (walkingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                WalkingRouteOverlay walkingRouteOverlay = new WalkingRouteOverlay(
                        bdMap);
                walkingRouteOverlay.setData(walkingRouteResult.getRouteLines().get(0));
                bdMap.setOnMarkerClickListener(walkingRouteOverlay);
                walkingRouteOverlay.addToMap();
                walkingRouteOverlay.zoomToSpan();
                totalLine = walkingRouteResult.getRouteLines().size();
                MyUtils.showToast(getApplicationContext(), "共查询出" + totalLine + "条符合条件的线路");
            }
        }

        /**
         * 换成路线结果回调
         */
        @Override
        public void onGetTransitRouteResult(
                TransitRouteResult transitRouteResult) {
            bdMap.clear();
            if (transitRouteResult == null
                    || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // drivingRouteResult.getSuggestAddrInfo()
                return;
            }
            if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                TransitRouteOverlay transitRouteOverlay = new TransitRouteOverlay(
                        bdMap);
                transitRouteOverlay.setData(transitRouteResult.getRouteLines()
                        .get(0));// 设置一条驾车路线方案
                bdMap.setOnMarkerClickListener(transitRouteOverlay);
                transitRouteOverlay.addToMap();
                transitRouteOverlay.zoomToSpan();
                totalLine = transitRouteResult.getRouteLines().size();
                MyUtils.showToast(getApplicationContext(), "共查询出" + totalLine + "条符合条件的线路");
                // 通过getTaxiInfo()可以得到很多关于打车的信息
                MyUtils.showToast(getApplicationContext(), "该路线打车总路程" + transitRouteResult.getTaxiInfo().getDistance());
            }
        }

        /**
         * 驾车路线结果回调 查询的结果可能包括多条驾车路线方案
         */
        @Override
        public void onGetDrivingRouteResult(
                DrivingRouteResult drivingRouteResult) {
            bdMap.clear();
            if (drivingRouteResult == null
                    || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MapActivity.this, "抱歉，未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                // drivingRouteResult.getSuggestAddrInfo()
                return;
            }
            if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
                DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                        bdMap);
                drivingRouteOverlay.setData(drivingRouteResult.getRouteLines()
                        .get(0));// 设置一条驾车路线方案
                bdMap.setOnMarkerClickListener(drivingRouteOverlay);
                drivingRouteOverlay.addToMap();
                drivingRouteOverlay.zoomToSpan();
                totalLine = drivingRouteResult.getRouteLines().size();
                MyUtils.showToast(getApplicationContext(), "共查询出" + totalLine + "条符合条件的线路");
                // 通过getTaxiInfo()可以得到很多关于打车的信息
                MyUtils.showToast(getApplicationContext(), "该路线打车总路程" + drivingRouteResult.getTaxiInfo().getDistance());
            }
        }
    };
}
