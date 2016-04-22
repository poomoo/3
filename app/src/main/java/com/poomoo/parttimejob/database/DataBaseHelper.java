/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.database;

import android.database.Cursor;

import com.poomoo.commlib.LogUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/20 15:51.
 */
public class DataBaseHelper {
    private static final String TAG = "DateBaseHelper";

    /**
     * 保存城市到本地
     *
     * @param cityInfos
     */
    public static void saveCity(List<CityInfo> cityInfos) {
        for (CityInfo cityInfo : cityInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from cityinfo where cityId =?", cityInfo.getCityId() + "");
            if (cursor.getCount() == 0)
                cityInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveArea完成");
    }

    /**
     * 保存区域到本地
     *
     * @param areaInfos
     */
    public static void saveArea(List<AreaInfo> areaInfos) {
        for (AreaInfo areaInfo : areaInfos) {
            Cursor cursor = DataSupport.findBySQL("select * from areainfo where cityInfo_id = ? and areaId=?", areaInfo.getCityInfo().getCityId() + "", areaInfo.getAreaId() + "");
            if (cursor.getCount() == 0)
                areaInfo.save();
            cursor.close();
        }
        LogUtils.d(TAG, "saveArea完成");
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
     * 查找城市的所有区域
     *
     * @param cityId
     * @return
     */
    public static List<AreaInfo> getAreaList(String cityId) {
        List<AreaInfo> areaList = DataSupport.where("cityId = ?", cityId).find(AreaInfo.class);
        return areaList;
    }
}
