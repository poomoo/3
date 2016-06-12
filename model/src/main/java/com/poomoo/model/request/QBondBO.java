/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 微信号绑定
 * 作者: 李苜菲
 * 日期: 2016/4/15 16:33.
 */
public class QBondBO extends BaseRequestBO {
    public String wxNum;//	--用户编号
    public String nickName;
    public String HeadPic;
    public String tel;
    public String password;
    public String code;
    public int deviceType;//1-安卓

    public QBondBO(String bizName, String method, String wxNum, String nickName, String headPic, String tel, String password, String code, int deviceType) {
        super(bizName, method);
        this.wxNum = wxNum;
        this.nickName = nickName;
        HeadPic = headPic;
        this.tel = tel;
        this.password = password;
        this.code = code;
        this.deviceType = deviceType;
    }
}
