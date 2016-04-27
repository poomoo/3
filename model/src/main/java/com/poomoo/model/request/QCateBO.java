package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * Created by 李苜菲 on 2016/4/27.
 */
public class QCateBO extends BaseRequestBO{
    public int cateId;

    public QCateBO(String bizName, String method, int cateId) {
        super(bizName, method);
        this.cateId = cateId;
    }
}
