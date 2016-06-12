package com.poomoo.parttimejob.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.poomoo.api.AbsAPICallback;
import com.poomoo.api.ApiException;
import com.poomoo.api.Network;
import com.poomoo.commlib.LogUtils;
import com.poomoo.commlib.MyConfig;
import com.poomoo.model.response.RWxTokenBO;
import com.poomoo.parttimejob.application.MyApplication;
import com.poomoo.parttimejob.ui.activity.LoginActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import rx.schedulers.Schedulers;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXEntryActivity";

    private void handleIntent(Intent paramIntent) {
        MyApplication.api.handleIntent(paramIntent, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        LogUtils.d(TAG, "onResp:" + baseResp.toString());
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) baseResp;

            //获取微信传回的code
            String code = newResp.code;
            LogUtils.d(TAG, newResp.code);
            LoginActivity.loginPresenter.getToken(code);
            finish();
        }
    }
}