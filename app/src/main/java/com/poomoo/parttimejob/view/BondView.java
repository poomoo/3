/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.parttimejob.view;

import com.poomoo.model.response.RUserBO;

/**
 * 作者: 李苜菲
 * 日期: 2016/4/5 10:11.
 */
public interface BondView extends BaseView {

    void checkSucceed(String msg);

    void checkFailed(String msg);

    void code(String msg);

    void loginSucceed(RUserBO rUserBO);

    void loginFailed(String msg);
}
