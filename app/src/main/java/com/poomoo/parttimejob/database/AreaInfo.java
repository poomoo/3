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
    private int cityId;

    public AreaInfo(int areaId, String areaName, int cityId) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.cityId = cityId;
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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
