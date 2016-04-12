package com.poomoo.api;

import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;
import rx.Subscriber;

public abstract class AbsAPICallback<T> extends Subscriber<T> {
    protected AbsAPICallback() {
    }

    @Override
    public void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }

        ApiException ex;
        Log.d("network", "异常:" + e);
        if (e instanceof SocketTimeoutException) {
            ex = new ApiException(ApiException.TIMEOUTEX);
            onError(ex);
        } else if (e instanceof IOException) {
            ex = new ApiException(ApiException.NETWORKEX);
            onError(ex);
        } else if (e instanceof HttpException) {             //HTTP错误
            ex = new ApiException(ApiException.SERVEREEX);
            onError(ex);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ApiException(ApiException.PARSEEX);
            onError(ex);
        } else if (e instanceof ApiException) {
            onError((ApiException)e);
        } else {
            ex = new ApiException(ApiException.UNKNOWNEX);        //未知错误
            onError(ex);
        }
    }


    /**
     * 错误回调
     */
    protected abstract void onError(ApiException e);

    /**
     * 权限错误，需要实现重新登录操作
     */
//    protected abstract void onPermissionError(ResultException ex);

    /**
     * 服务器返回的错误
     */
//    protected abstract void onResultError(ApiException e);
    @Override
    public void onCompleted() {

    }

}