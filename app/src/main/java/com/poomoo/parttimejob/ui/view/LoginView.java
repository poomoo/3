/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.ui.view;

import com.poomoo.model.response.RUserBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 10:11.
 */
public interface LoginView {
    void loginSucceed(RUserBO rUserBO);

    void loginFailed(String msg);

    void remember();

    void unRemember();
}
