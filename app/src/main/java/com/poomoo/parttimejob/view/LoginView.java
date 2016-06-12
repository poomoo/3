/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RUserBO;
import com.poomoo.model.response.RWxInfoBO;
import com.poomoo.model.response.RWxTokenBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 10:11.
 */
public interface LoginView extends BaseView {
    void loginSucceed(RUserBO rUserBO);

    void loginFailed(String msg);

    void remember();

    void unRemember();

    void getToken(RWxTokenBO rWxTokenBO);

    void getInfo(RWxInfoBO rWxInfoBO);

    void isBond(String msg);

    void notBond(String msg);
}
