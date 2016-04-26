/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/26 09:52.
 */
public class QAuthBO extends BaseRequestBO {
    public int userId;
    public String realName;
    public String schoolName;
    public String intoSchoolDt;
    public String idCardNum;
    public String idPicture;

    public QAuthBO(String bizName, String method, int userId, String realName, String schoolName, String intoSchoolDt, String idCardNum, String idPicture) {
        super(bizName, method);
        this.userId = userId;
        this.realName = realName;
        this.schoolName = schoolName;
        this.intoSchoolDt = intoSchoolDt;
        this.idCardNum = idCardNum;
        this.idPicture = idPicture;
    }
}
