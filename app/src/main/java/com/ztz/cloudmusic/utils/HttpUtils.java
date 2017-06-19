package com.ztz.cloudmusic.utils;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * 网络访问工具类
 * Created by wqewqe on 2017/6/9.
 */

public class HttpUtils {
    /**
     * 获取Builder 无需在设置请求头
     */

    public static Request.Builder getBuilder(){

        Request.Builder builder=new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json");
        return builder;
    }

    /**
     * 获取一个GET请求
     * @param url 请求地址
     * @return request
     */
    public static Request requestGet(String url){
        Request request=getBuilder().get().url(url).build();
        return request;
    }
    public static Request requestGet(HttpUrl url){
        Request request=getBuilder().get().url(url).build();
        return request;
    }
}
