package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RBuyBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 16:12.
 */
public interface BuyView extends BaseView {
    void succeed(List<RBuyBO> rBuyBOs);
}
