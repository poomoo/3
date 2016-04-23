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
public class TypeInfo extends DataSupport {
    private int cateId;
    private String name;

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
