package com.poomoo.api;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.poomoo.model.response.ResponseBO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        final String response = value.string();
        ResponseBO responseBO = gson.fromJson(response, ResponseBO.class);
        Log.d("convert", "返回的数据:" + responseBO);
        if (responseBO.rsCode == 1) {
            if (type.equals(ResponseBO.class))
                return (T) responseBO;
            //result==1表示成功返回，继续用本来的Model类解析
            String jsonData = responseBO.jsonData.toString();
            if (!TextUtils.isEmpty(jsonData)) {
                return gson.fromJson(jsonData, type);
            }
        } else
            throw new ApiException(responseBO.rsCode, responseBO.msg);
        return null;
    }
}