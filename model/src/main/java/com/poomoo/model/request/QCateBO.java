package com.poomoo.model.request;

import com.poomoo.model.base.BaseRequestBO;

/**
 * Created by 李苜菲 on 2016/4/27.
 */
public class QCateBO extends BaseRequestBO{
    public int cateId;
    public int pageNum;
    public int pageSize;

    public QCateBO(String bizName, String method, int cateId, int pageNum, int pageSize) {
        super(bizName, method);
        this.cateId = cateId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
}
