package com.poomoo.api;

import android.util.Log;

/**
 * Created by liukun on 16/3/10.
 */
public class ApiException extends RuntimeException {
    //异常
    public static final int NETWORKEX = 1000;//网络异常
    public static final int SERVEREEX = 1001;//服务器异常
    public static final int PARSEEX = 1002;//解析异常
    public static final int UNKNOWNEX = 1003;//未知异常
    public static final int TIMEOUTEX = 1004;//连接超时
    //服务器返回的错误
    public static final int NORMALERR = -1;//常规错误
    public static final int NULLERR = -2;//参数为空
    public static final int CHECKERR = -3;//参数校验不合法
    public static final int EMPTYERR = -4;//无数据
    public static final int SERVERERR = -5;//服务器错误

    public ApiException(int resultCode, String msg) {
        this(getApiExceptionMessage(resultCode, msg));
    }

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode, ""));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    public static String getApiExceptionMessage(int code, String msg) {
        Log.d("getApiExceptionMessage", code + "");
        String message;
        switch (code) {
            case NETWORKEX:
                message = "网络异常,请检查网络";
                break;
            case SERVEREEX:
                message = "服务器异常";
                break;
            case PARSEEX:
                message = "解析异常";
                break;
            case UNKNOWNEX:
                message = "未知异常";
                break;
            case TIMEOUTEX:
                message = "连接超时";
                break;
            case NORMALERR:
                message = msg;
                break;
            case NULLERR:
                message = "参数为空";
                break;
            case CHECKERR:
                message = "校验错误";
                break;
            case EMPTYERR:
                message = "无数据";
                break;
            case SERVERERR:
                message = "服务器错误";
                break;
            default:
                message = "未知错误";

        }
//        return message + "(code:" + code + ")";
        return message;
    }
}

