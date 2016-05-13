package com.poomoo.parttimejob.database;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 李苜菲
 * @ClassName ProvinceInfo
 * @Description TODO 区域模型
 * @date 2015年8月16日 下午10:45:03
 */
public class ProvinceInfo extends DataSupport {
    private int provinceId;
    private String provinceName;

    public ProvinceInfo(int provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
