package com.poomoo.parttimejob.database;

import org.litepal.crud.DataSupport;

/**
 * @author 李苜菲
 * @ClassName AreaInfo
 * @Description TODO 区域模型
 * @date 2015年8月16日 下午10:45:03
 */
public class AreaInfo extends DataSupport {
    private int areaId;
    private String areaName;
    private CityInfo cityInfo;

    public AreaInfo(int areaId, String areaName, CityInfo cityInfo) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.cityInfo = cityInfo;
    }

    public CityInfo getCityInfo() {
        return cityInfo;
    }

    public void setCityInfo(CityInfo cityInfo) {
        this.cityInfo = cityInfo;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
