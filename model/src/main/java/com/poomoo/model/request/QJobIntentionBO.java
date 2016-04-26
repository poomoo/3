package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 求职意向
 * Created by 李苜菲 on 2016/4/26.
 */
public class QJobIntentionBO extends BaseRequestBO{
    public int userId;
    public String cateId;//--职位类别编号，多值逗号隔开
    public String workday;//--第一个数字表示时间段（上午，下午，晚上），第二个数字表示周
    public String workAreaId;//--工作区域选择，多值逗号隔开
    public String otherInfo;//--其他信息

    public QJobIntentionBO(String bizName, String method, int userId, String cateId, String workday, String workAreaId, String otherInfo) {
        super(bizName, method);
        this.userId = userId;
        this.cateId = cateId;
        this.workday = workday;
        this.workAreaId = workAreaId;
        this.otherInfo = otherInfo;
    }
}
