package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RUrl;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/25 14:08.
 */
public interface ResumeView extends BaseView {
    void upLoadSucceed(RUrl rUrl);

    void submitSucceed(String msg);
}
