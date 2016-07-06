/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.base.BaseJobBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/5/24 10:28.
 */
public interface MyJobCollectionView extends BaseView {
    void succeed(List<BaseJobBO> list);

    void cancelSucceed(String msg);

    void cancelFailed(String msg);
}
