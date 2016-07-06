package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RCommodityInfoBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/7/5 17:32.
 */
public interface CommodityInfoView extends BaseView {
    void succeed(RCommodityInfoBO rCommodityInfoBO);

    void collectSucceed(String msg);

    void collectFailed(String msg);

    void cancelCollectSucceed(String msg);

    void cancelCollectFailed(String msg);

}
