/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RBuyBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/5/24 10:28.
 */
public interface MyCommodityCollectionView extends BaseView {
    void succeed(List<RBuyBO> list);

    void cancelSucceed(String msg);

    void cancelFailed(String msg);
}
