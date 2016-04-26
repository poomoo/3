/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RUrl;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/26 09:31.
 */
public interface AuthView extends BaseView {
    void upLoadSucceed(RUrl rUrl);

    void submitSucceed(String msg);
}
