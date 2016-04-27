package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RMessageBO;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/27 11:51.
 */
public interface MessageView extends BaseView {
    void downSucceed(List<RMessageBO> rMessageBOList);

    void upSucceed(String msg);

    void upFailed(String msg);
}
