/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.NetConfig;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyUtils;
import com.poomoo.commlib.PingYinUtil;
import com.poomoo.commlib.StringHelper;
import com.poomoo.model.base.BaseRequestBO;
import com.poomoo.model.response.RAreaBO;
import com.poomoo.parttimejob.R;
import com.poomoo.parttimejob.database.AreaInfo;
import com.poomoo.parttimejob.database.CityInfo;
import com.poomoo.parttimejob.database.DataBaseHelper;
import com.poomoo.parttimejob.database.ProvinceInfo;
import com.poomoo.parttimejob.event.Events;
import com.poomoo.parttimejob.event.RxBus;
import com.poomoo.parttimejob.ui.base.BaseActivity;
import com.poomoo.parttimejob.ui.custom.MyLetterListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import rx.schedulers.Schedulers;

/**
 * 城市列表
 * 作者: 李苜菲
 * 日期: 2015/11/25 15:40.
 */
public class CityListActivity extends BaseActivity implements OnScrollListener {
    private BaseAdapter adapter;
    private ResultListAdapter resultListAdapter;
    private ListView personList;
    private ListView resultList;
    private TextView overlay; // 对话框首字母textview
    private MyLetterListView letterListView; // A-Z listview
    private HashMap<String, Integer> alphaIndexer;// 存放存在的汉语拼音首字母和与之对应的列表位置
    private String[] sections;// 存放存在的汉语拼音首字母
    private Handler handler;
    private OverlayThread overlayThread; // 显示首字母对话框
    private List<RAreaBO.city> allCity_lists; // 所有城市列表
    private List<RAreaBO.city> city_lists = new ArrayList<>();// 城市列表
    private List<RAreaBO.city> city_AllHot;
    private List<RAreaBO.city> city_NewHot;
    private List<RAreaBO.city> city_result;
    private List<CityInfo> cityInfos = new ArrayList<>();//保存城市
    private List<AreaInfo> areaInfos = new ArrayList<>();//保存区域
    private EditText sh;
    private TextView tv_noresult;

    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private String currentCity; // 当前城市
    private String locateCity; // 定位城市
    private int locateProcess = 1; // 记录当前定位的状态 正在定位-定位成功-定位失败
    private boolean isNeedFresh;
    private WindowManager windowManager;
    private List<ProvinceInfo> provinceInfos;
    private int currentCityId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack();
        initView();
    }

    @Override
    protected String onSetTitle() {
        return getString(R.string.title_selectCity);
    }

    @Override
    protected int onBindLayout() {
        return R.layout.activity_city_list;
    }

    protected void initView() {
        personList = (ListView) findViewById(R.id.list_view);
        resultList = (ListView) findViewById(R.id.search_result);
        sh = (EditText) findViewById(R.id.sh);
        tv_noresult = (TextView) findViewById(R.id.tv_noresult);
        letterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);

        initData();
        cityInfos = DataBaseHelper.getCityList();
        if (cityInfos.size() > 0) {
            initCityList();
            getCityList(true);
        } else
            getCityList(false);
    }


    private void initData() {
        locateCity = application.getLocateCity();

        allCity_lists = new ArrayList<>();
        city_AllHot = new ArrayList<>();
        city_result = new ArrayList<>();
        sh.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (TextUtils.isEmpty(s + "")) {
                    letterListView.setVisibility(View.VISIBLE);
                    personList.setVisibility(View.VISIBLE);
                    resultList.setVisibility(View.GONE);
                    tv_noresult.setVisibility(View.GONE);
                } else {
                    city_result.clear();
                    letterListView.setVisibility(View.GONE);
                    personList.setVisibility(View.GONE);
                    getResultCityList(s.toString());
                    if (city_result.size() <= 0) {
                        tv_noresult.setVisibility(View.VISIBLE);
                        resultList.setVisibility(View.GONE);
                    } else {
                        tv_noresult.setVisibility(View.GONE);
                        resultList.setVisibility(View.VISIBLE);
                        resultListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        letterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<>();
        handler = new Handler();
        overlayThread = new OverlayThread();
        isNeedFresh = true;
        personList.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 2) {
                currentCity = allCity_lists.get(position).cityName;
                currentCityId = allCity_lists.get(position).cityId;
                if (!application.getCurrCity().equals(currentCity)) {
                    application.setCurrCity(currentCity);
                    application.setCurrCityId(currentCityId);
                    RxBus.getInstance().send(Events.EventEnum.DELIVER_CITY, null);
                    finish();
                    getActivityOutToRight();
                }
            }
        });
        locateProcess = 1;
//        personList.setAdapter(adapter);
        personList.setOnScrollListener(this);
        resultListAdapter = new ResultListAdapter(this, city_result);
        resultList.setAdapter(resultListAdapter);
        resultList.setOnItemClickListener((parent, view, position, id) -> {
            currentCity = city_result.get(position).cityName;
            currentCityId = city_result.get(position).cityId;
            if (!application.getCurrCity().equals(currentCity)) {
                application.setCurrCity(currentCity);
                application.setCurrCityId(currentCityId);
                RxBus.getInstance().send(Events.EventEnum.DELIVER_CITY, null);
                finish();
                getActivityOutToRight();
            }
        });
        initOverlay();
        cityInit();
        setAdapter(allCity_lists);

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        initLocation();
        mLocationClient.start();
    }

    private void cityInit() {
        RAreaBO rAreaBO = new RAreaBO();
        RAreaBO.city city = rAreaBO.new city("定位城市", "0");
        allCity_lists.add(city);
//        city = rAreaBO.new city("历史", "1"); // 最近访问的城市
//        allCity_lists.add(city);
        city = rAreaBO.new city("热门城市", "1"); // 热门城市
        allCity_lists.add(city);
//        city = rAreaBO.new city("全部", "2"); // 全部城市
//        allCity_lists.add(city);
    }

    /**
     * 热门城市
     */
    public void hotCityInit() {
        int len = city_lists.size();
        RAreaBO rAreaBO = new RAreaBO();
        RAreaBO.city city;
//        if (city_AllHot.size() > 0)
//            city_AllHot.clear();
        city_NewHot = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            LogUtils.d(TAG, "热门城市 isHot:" + city_lists.get(i).cityName + city_lists.get(i).isHot);
            if (city_lists.get(i).isHot != null && city_lists.get(i).isHot.equals("1")) {
                city = rAreaBO.new city(city_lists.get(i).cityName, "2");
                city.cityId = city_lists.get(i).cityId;
                city_NewHot.add(city);
                LogUtils.d(TAG, "add");
            }
        }
        if (city_NewHot.size() > city_AllHot.size()) {
            city_AllHot.removeAll(city_NewHot);
            city_AllHot.addAll(city_NewHot);
        }
    }

    private void initCityList() {
        int len = cityInfos.size();
        for (int i = 0; i < len; i++) {
            RAreaBO.city city;
            city = new RAreaBO().new city(cityInfos.get(i).getCityName(), "");
            city.cityId = cityInfos.get(i).getCityId();
            city.isHot = cityInfos.get(i).getIsHot();
            city_lists.add(city);
        }
        initCity();
    }

    private void getCityList(boolean flag) {
        if (!flag)
            showProgressDialog(getString(R.string.dialog_msg));
        BaseRequestBO baseRequestBO = new BaseRequestBO(NetConfig.COMMACTION, NetConfig.CITY);
        Network.getCommApi().getCitys(baseRequestBO)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new AbsAPICallback<List<RAreaBO>>() {
                    @Override
                    protected void onError(ApiException e) {
                        runOnUiThread(() -> {
                            closeProgressDialog();
                            MyUtils.showToast(getApplicationContext(), e.getMessage());
                        });
                    }

                    @Override
                    public void onNext(List<RAreaBO> rAreaBOs) {
                        closeProgressDialog();
                        city_lists = new ArrayList<>();
                        provinceInfos = new ArrayList<>();
                        int index = 0;
                        for (RAreaBO rAreaBO : rAreaBOs) {
                            ProvinceInfo provinceInfo = new ProvinceInfo(rAreaBOs.get(index).provinceId, rAreaBOs.get(index++).provinceName);
                            int len = rAreaBO.cityList.size();
                            LogUtils.d(TAG, rAreaBO + ":" + len + "index:" + index + "rAreaBOs:" + rAreaBOs.size());
                            cityInfos = new ArrayList<>();
                            for (int i = 0; i < len; i++) {
                                CityInfo cityInfo = new CityInfo(rAreaBO.cityList.get(i).cityId, rAreaBO.cityList.get(i).cityName, rAreaBO.cityList.get(i).isHot, provinceInfo.getProvinceId());
                                city_lists.add(rAreaBO.cityList.get(i));
                                int len2 = rAreaBO.cityList.get(i).areaList.size();
                                areaInfos = new ArrayList<>();
                                for (int j = 0; j < len2; j++)
                                    areaInfos.add(new AreaInfo(city_lists.get(i).areaList.get(j).areaId, city_lists.get(i).areaList.get(j).areaName, cityInfo.getCityId()));
                                DataBaseHelper.saveArea(areaInfos);
                                cityInfos.add(cityInfo);
                            }
                            DataBaseHelper.saveCity(cityInfos);
                            provinceInfos.add(provinceInfo);
                        }
                        DataBaseHelper.saveProvince(provinceInfos);
                        runOnUiThread(() -> initCity());
                    }
                });
    }

    private void initCity() {
        setPinYin(city_lists);
        LogUtils.i(TAG, "city_lists:" + city_lists.toString());
        hotCityInit();
        Collections.sort(city_lists, comparator);
        if (allCity_lists.size() > 2)
            allCity_lists = allCity_lists.subList(0, 2);
        allCity_lists.addAll(city_lists);

        LogUtils.i(TAG, "allCity_lists:" + allCity_lists.toString() + allCity_lists.size());
        adapter.notifyDataSetChanged();
        sections = new String[allCity_lists.size()];
        for (int i = 0; i < allCity_lists.size(); i++) {
            // 当前汉语拼音首字母
            String currentStr = getAlpha(allCity_lists.get(i).pinyin);
            // 上一个汉语拼音首字母，如果不存在为" "
            String previewStr = (i - 1) >= 0 ? getAlpha(allCity_lists.get(i - 1).pinyin) : " ";
            if (!previewStr.equals(currentStr)) {
                String name = getAlpha(allCity_lists.get(i).pinyin);
                alphaIndexer.put(name, i);
                sections[i] = name;
            }
        }
    }

    private void getResultCityList(String keyword) {
        int len = city_lists.size();
        RAreaBO.city city;
        for (int i = 0; i < len; i++) {
            city = city_lists.get(i);

            if (city.pinyin.contains(keyword) || city.pinyin.contains(keyword.toUpperCase()) || city.cityName.contains(keyword))
                city_result.add(city);
        }
        Collections.sort(city_result, comparator);
    }

    public void setPinYin(List<RAreaBO.city> list) {
        int i = 0;
        for (RAreaBO.city rAreaBO : list)
            list.get(i++).pinyin = StringHelper.getPinYinHeadChar(rAreaBO.cityName);
    }

    /**
     * a-z排序
     */
    Comparator comparator = new Comparator<RAreaBO.city>() {
        @Override
        public int compare(RAreaBO.city lhs, RAreaBO.city rhs) {
            String a = lhs.pinyin.substring(0, 1);
            String b = rhs.pinyin.substring(0, 1);
            int flag = a.compareTo(b);
            if (flag == 0) {
                return a.compareTo(b);
            } else {
                return flag;
            }
        }
    };

    private void setAdapter(List<RAreaBO.city> list) {
        adapter = new ListAdapter(this, list);
        personList.setAdapter(adapter);
    }

    /**
     * 实现实位回调监听
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
        int span = 1 * 10 * 1000;

        option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setPriority(LocationClientOption.GpsFirst);    // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.i("location", "location.getLongitude():" + location.getLongitude() + "location.getLatitude():"
                    + location.getLatitude() + "location.getCity():" + location.getCity());
            MyUtils.showToast(getApplicationContext(), "city:" + location.getCity());
            if (!isNeedFresh)
                return;

            if (location.getCity() == null) {
                locateProcess = 3; // 定位失败
                mLocationClient.stop();
//                personList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                return;
            }
            locateCity = location.getCity();
            application.setLocateCity(locateCity);
            locateProcess = 2; // 定位成功
//            personList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }

    private class ResultListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<RAreaBO.city> results = new ArrayList<>();

        public ResultListAdapter(Context context, List<RAreaBO.city> results) {
            inflater = LayoutInflater.from(context);
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(results.get(position).cityName);
            return convertView;
        }

        class ViewHolder {
            TextView name;
        }
    }

    public class ListAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private List<RAreaBO.city> list;
        final int VIEW_TYPE = 3;

        public ListAdapter(Context context, List<RAreaBO.city> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
            this.context = context;
            alphaIndexer = new HashMap<>();
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE;
        }

        @Override
        public int getItemViewType(int position) {
            return position < 2 ? position : 2;
        }

        @Override
        public int getCount() {
            LogUtils.d(TAG, "getCount:" + allCity_lists.size());
            return allCity_lists.size();
        }

        @Override
        public Object getItem(int position) {
            return allCity_lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView city;
            int viewType = getItemViewType(position);
            LogUtils.d(TAG, "getView:" + viewType + position);

            if (viewType == 0) { // 定位
                convertView = inflater.inflate(R.layout.frist_list_item, null);
                TextView locateHint = (TextView) convertView.findViewById(R.id.locateHint);
                city = (TextView) convertView.findViewById(R.id.lng_city);
                city.setOnClickListener(v -> {
                    if (locateProcess == 2) {
                        if (!application.getCurrCity().equals(city.getText().toString())) {
                            application.setCurrCity(city.getText().toString());
                            application.setCurrCityId(DataBaseHelper.getCityId(application.getCurrCity()));
                            RxBus.getInstance().send(Events.EventEnum.DELIVER_CITY, null);
                            finish();
                            getActivityOutToRight();
                        }
                    } else if (locateProcess == 3) {
                        locateProcess = 1;
                        personList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        mLocationClient.stop();
                        isNeedFresh = true;
                        initLocation();
                        locateCity = "";
                        mLocationClient.start();
                    }
                });
                ProgressBar pbLocate = (ProgressBar) convertView.findViewById(R.id.pbLocate);
                if (locateProcess == 1) { // 正在定位
                    locateHint.setText("正在定位");
                    city.setVisibility(View.GONE);
                    pbLocate.setVisibility(View.VISIBLE);
                } else if (locateProcess == 2) { // 定位成功
                    locateHint.setText(list.get(position).cityName);
                    LogUtils.i(TAG, "定位成功:" + "currentCity:" + currentCity + "city:" + city);
                    city.setVisibility(View.VISIBLE);
                    city.setText(locateCity);
                    mLocationClient.stop();
                    pbLocate.setVisibility(View.GONE);
                } else if (locateProcess == 3) {
                    locateHint.setText("未定位到城市,请选择");
                    city.setVisibility(View.VISIBLE);
                    city.setText("重新定位");
                    pbLocate.setVisibility(View.GONE);
                }
            } else if (viewType == 1) {
                convertView = inflater.inflate(R.layout.recent_city, null);
                GridView hotCity = (GridView) convertView.findViewById(R.id.recent_city);
                hotCity.setOnItemClickListener((parent1, view, position1, id) -> {
                    if (!application.getCurrCity().equals(city_AllHot.get(position1).cityName)) {
                        application.setCurrCity(city_AllHot.get(position1).cityName);
                        application.setCurrCityId(city_AllHot.get(position1).cityId);
                        RxBus.getInstance().send(Events.EventEnum.DELIVER_CITY, null);
                        finish();
                        getActivityOutToRight();
                    }
                    LogUtils.i(TAG, "热门城市:" + "currentCity" + currentCity + "locateCity:" + locateCity);
                });
                hotCity.setAdapter(new HotCityAdapter(context));
                TextView hotHint = (TextView) convertView
                        .findViewById(R.id.recentHint);
                hotHint.setText(list.get(position).cityName);
            } else {
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.list_item, null);
                    holder = new ViewHolder();
                    holder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                    holder.name = (TextView) convertView.findViewById(R.id.name);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (position >= 2) {
                    LogUtils.i(TAG, "position:" + position + "cityName:" + list.get(position).cityName);
                    holder.name.setText(allCity_lists.get(position).cityName);
                    String currentStr = getAlpha(allCity_lists.get(position).pinyin);
                    String previewStr = (position - 1) >= 0 ? getAlpha(allCity_lists.get(position - 1).pinyin) : " ";
                    LogUtils.i(TAG, "currentStr:" + currentStr + " previewStr:" + previewStr);
                    if (!previewStr.equals(currentStr)) {
                        holder.alpha.setVisibility(View.VISIBLE);
                        holder.alpha.setText(currentStr);
                    } else {
                        holder.alpha.setVisibility(View.GONE);
                    }
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView alpha; // 首字母标题
            TextView name; // 城市名字
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }

    class HotCityAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;

        public HotCityAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return city_AllHot.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogUtils.d(TAG, "热门城市 getView" + position);
            convertView = inflater.inflate(R.layout.item_city, null);
            TextView city = (TextView) convertView.findViewById(R.id.city);
            city.setText(city_AllHot.get(position).cityName);
            return convertView;
        }
    }

//    class HitCityAdapter extends BaseAdapter {
//        private Context context;
//        private LayoutInflater inflater;
//        private List<String> hotCitys;
//
//        public HitCityAdapter(Context context, List<String> hotCitys) {
//            this.context = context;
//            inflater = LayoutInflater.from(this.context);
//            this.hotCitys = hotCitys;
//        }
//
//        @Override
//        public int getCount() {
//            return hotCitys.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return position;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            convertView = inflater.inflate(R.layout.item_city, null);
//            TextView city = (TextView) convertView.findViewById(R.id.city);
//            city.setText(hotCitys.get(position));
//            return convertView;
//        }
//    }

    private boolean mReady;

    // 初始化汉语拼音首字母弹出提示框
    private void initOverlay() {
        mReady = true;
        LayoutInflater inflater = LayoutInflater.from(this);
        overlay = (TextView) inflater.inflate(R.layout.overlay, null);
        overlay.setVisibility(View.INVISIBLE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(overlay, lp);
        LogUtils.d(TAG, "initOverlay" + windowManager + ":" + overlay);
    }

    private boolean isScroll = false;

    private class LetterListViewListener implements
            MyLetterListView.OnTouchingLetterChangedListener {

        @Override
        public void onTouchingLetterChanged(final String s) {
            isScroll = false;
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                personList.setSelection(position);
                overlay.setText(s);
                overlay.setVisibility(View.VISIBLE);
                handler.removeCallbacks(overlayThread);
                // 延迟一秒后执行，让overlay为不可见
                handler.postDelayed(overlayThread, 1000);
            }
        }
    }

    // 设置overlay不可见
    private class OverlayThread implements Runnable {
        @Override
        public void run() {
            overlay.setVisibility(View.GONE);
        }
    }

    // 获得汉语拼音首字母
    private String getAlpha(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定位城市";
        } else if (str.equals("1")) {
            return "热门城市";
        }
//        else if (str.equals("2")) {
//            return "全部";
//        }
        else {
            return "#";
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_TOUCH_SCROLL
                || scrollState == SCROLL_STATE_FLING) {
            isScroll = true;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (!isScroll) {
            return;
        }

        if (mReady) {
            String text;
            String name = allCity_lists.get(firstVisibleItem).cityName;
            String pinyin = allCity_lists.get(firstVisibleItem).pinyin;

            if (firstVisibleItem < 2) {
                text = name;
            } else {
                text = PingYinUtil.converterToFirstSpell(pinyin)
                        .substring(0, 1).toUpperCase();
            }
            LogUtils.i(TAG, "firstVisibleItem:" + firstVisibleItem + " text:" + text);
            overlay.setText(text);
            overlay.setVisibility(View.VISIBLE);
            handler.removeCallbacks(overlayThread);
            // 延迟一秒后执行，让overlay为不可见
            handler.postDelayed(overlayThread, 1000);
        }
    }

    @Override
    protected void onDestroy() {
        windowManager.removeViewImmediate(overlay);
        super.onDestroy();
        LogUtils.d(TAG, "onDestroy:" + windowManager + ":" + overlay);
    }
}
