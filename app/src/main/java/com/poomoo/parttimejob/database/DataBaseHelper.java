/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.database;

import android.database.Cursor;

import com.poomoo.commlib.LogUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/20 15:51.
 */
public class DataBaseHelper {
    private static final String TAG = "DateBaseHelper";

    /**
     * 保存省份到本地
     *
     * @param provinceInfos
     */
    public static void saveProvince(List<ProvinceInfo> provinceInfos) {
        for (ProvinceInfo provinceInfo : provinceInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from ProvinceInfo where provinceId =?", provinceInfo.getProvinceId() + "");
            if (cursor.getCount() == 0)
                provinceInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveProvince完成");
    }

    /**
     * 保存城市到本地
     *
     * @param cityInfos
     */
    public static void saveCity(List<CityInfo> cityInfos) {
        for (CityInfo cityInfo : cityInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from CityInfo where cityId = ? and provinceInfo_id = ?", cityInfo.getCityId() + "", cityInfo.getProvinceInfo().getProvinceId() + "");
            if (cursor.getCount() == 0)
                cityInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveCity完成");
    }

    /**
     * 保存区域到本地
     *
     * @param areaInfos
     */
    public static void saveArea(List<AreaInfo> areaInfos) {
        for (AreaInfo areaInfo : areaInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from AreaInfo where cityInfo_id = ? and areaId=?", areaInfo.getCityInfo().getCityId() + "", areaInfo.getAreaId() + "");
            if (cursor.getCount() == 0)
                areaInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveArea完成");
    }

    /**
     * 保存类型到本地
     *
     * @param typeInfos
     */
    public static void saveType(List<TypeInfo> typeInfos) {
        for (TypeInfo typeInfo : typeInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from TypeInfo where cateId = ?", typeInfo.getCateId() + "");
            if (cursor.getCount() == 0)
                typeInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveType完成");
    }

    /**
     * 查找所有城市
     *
     * @return
     */
    public static List<ProvinceInfo> getProvinceList() {
        List<ProvinceInfo> provinceInfoList = DataSupport.findAll(ProvinceInfo.class);
        return provinceInfoList;
    }

    /**
     * 查找所有城市
     *
     * @return
     */
    public static List<CityInfo> getCityList() {
        List<CityInfo> cityList = DataSupport.findAll(CityInfo.class);
        return cityList;
    }

    /**
     * 查找所有城市
     *
     * @return
     */
    public static List<CityInfo> getCityList(int provinceId) {
        List<CityInfo> cityList = DataSupport.where("provinceinfo_id = ?", provinceId + "").find(CityInfo.class);
        return cityList;
    }

    /**
     * 查找城市的所有区域
     *
     * @param cityId
     * @return
     */
    public static List<AreaInfo> getAreaList(int cityId) {
        LogUtils.d(TAG, "getAreaList:" + cityId);
        List<AreaInfo> areaList = DataSupport.where("cityinfo_id = ?", cityId + "").find(AreaInfo.class);
        return areaList;
    }

    /**
     * 全部兼职用
     *
     * @param cityName
     * @param cityId
     * @return
     */
    public static List<String> getCityAndArea(String cityName, int cityId) {
        List<String> list = new ArrayList<>();
        cityName = "全" + cityName + "#";
        list.add(cityName);
        List<AreaInfo> areaInfos = getAreaList(cityId);
        for (AreaInfo areaInfo : areaInfos)
            list.add(areaInfo.getAreaName() + "#" + areaInfo.getAreaId());
        return list;
    }

    /**
     * 全部兼职用
     *
     * @return
     */
    public static List<String> getType() {
        List<String> list = new ArrayList<>();
        list.add("不限#");
        List<TypeInfo> typeInfos = DataSupport.findAll(TypeInfo.class);
        for (TypeInfo typeInfo : typeInfos)
            list.add(typeInfo.getName() + "#" + typeInfo.getCateId());
        return list;
    }

    /**
     * @return
     */
    public static List<String> getProvince() {
        List<String> list = new ArrayList<>();
        List<ProvinceInfo> provinceInfos = getProvinceList();
        for (ProvinceInfo provinceInfo : provinceInfos)
            list.add(provinceInfo.getProvinceName() + "#" + provinceInfo.getProvinceId());
        return list;
    }

    public static List<String> getCity(int provinceId) {
        List<String> list = new ArrayList<>();
        List<CityInfo> cityInfos = getCityList(provinceId);
        for (CityInfo cityInfo : cityInfos)
            list.add(cityInfo.getCityName() + "#" + cityInfo.getCityId());
        return list;
    }

    public static List<String> getArea(int cityId) {
        List<String> list = new ArrayList<>();
        List<AreaInfo> areaInfos = getAreaList(cityId);
        for (AreaInfo areaInfo : areaInfos)
            list.add(areaInfo.getAreaName() + "#" + areaInfo.getAreaId());
        return list;
    }

    public static List<String> getArea1(int cityId) {
        List<String> list = new ArrayList<>();
        list.add("不限#");
        List<AreaInfo> areaInfos = getAreaList(cityId);
        for (AreaInfo areaInfo : areaInfos)
            list.add(areaInfo.getAreaName() + "#" + areaInfo.getAreaId());
        return list;
    }

    public static String[] getProvinceCityArea(int provinceId, int cityId, int areaId) {
        LogUtils.d(TAG, "provinceId:" + provinceId + "cityId:" + cityId + "areaId:" + areaId);
        String[] temp = new String[3];
        Cursor cursor = DataSupport.findBySQL("select provinceName from ProvinceInfo where provinceId = ?", provinceId + "");
        cursor.moveToFirst();
        temp[0] = cursor.getString(0);
        LogUtils.d(TAG, "province:" + temp[0]);
        cursor = DataSupport.findBySQL("select cityName from CityInfo where provinceinfo_id = ? and cityId = ?", provinceId + "", cityId + "");
        cursor.moveToFirst();
        temp[1] = cursor.getString(0);
        LogUtils.d(TAG, "city:" + temp[1]);
        cursor = DataSupport.findBySQL("select areaName from AreaInfo where cityinfo_id = ? and areaId = ?", cityId + "", areaId + "");
        cursor.moveToFirst();
        temp[2] = cursor.getString(0);
        LogUtils.d(TAG, "area:" + temp[2]);
        cursor.close();
        return temp;
    }
}
