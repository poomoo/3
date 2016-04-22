package com.poomoo.parttimejob.database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李苜菲
 * @ClassName AreaInfo
 * @Description TODO 区域模型
 * @date 2015年8月16日 下午10:45:03
 */
public class CityInfo extends DataSupport {
    private int cityId;
    private String cityName;
    private List<AreaInfo> areaInfoList = new ArrayList<AreaInfo>();

//    public CityInfo(int cityId, String cityName, List<AreaInfo> areaInfoList) {
//        this.cityId = cityId;
//        this.cityName = cityName;
//        this.areaInfoList = areaInfoList;
//    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<AreaInfo> getAreaInfoList() {
        return areaInfoList;
    }

    public void setAreaInfoList(List<AreaInfo> areaInfoList) {
        this.areaInfoList = areaInfoList;
    }
}
