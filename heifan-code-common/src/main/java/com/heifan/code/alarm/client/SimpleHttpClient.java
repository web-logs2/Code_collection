package com.heifan.code.alarm.client;

import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimpleHttpClient {

    private static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";

    public static <K> String doPost(String url, K jsonParam, Map<String, String> headers) {
        if (jsonParam != null) {
            Gson gson = new Gson();
            String jsonStr = gson.toJson(jsonParam);
            HttpResponse response = HttpUtil.createPost(url)
                    .header(Header.CONTENT_TYPE, headers.get(Header.CONTENT_TYPE.getValue()))
                    .body(jsonStr).timeout(20000).executeAsync();
            return response.body();
        }
        return "";

    }

    /**
     * 发送简单get请求
     *
     * @param url 请求URL
     * @return 请求结果
     */
    public static String getHttp(String url) {
        Gson gson = new Gson();
        return HttpUtil.get(url);
    }

    /**
     * 发送带有验证头的Get请求
     *
     * @param url 请求URL
     * @return 请求结果
     */
    public static String getHttp(String url, String authorization) {
        HttpUtil.get(url);
        HttpResponse response = HttpUtil.createGet(url)
                .header(Header.AUTHORIZATION, authorization)
                .executeAsync();
        return response.body();
    }

    public static <T, K> T post(String url, K jsonParam, Class<T> clazz) {
        return post(url, jsonParam, clazz, null);
    }

    public static <T, K> T post(String url, K jsonParam, Class<T> clazz, Map<String, String> header) {
        String json = null;
        header = header == null ? new HashMap<>() : header;
        if (!header.containsKey(Header.CONTENT_TYPE.toString())) {
            header.put(Header.CONTENT_TYPE.toString(), DEFAULT_CONTENT_TYPE);
        }
        json = doPost(url, jsonParam, header);
        Gson gson = new Gson();
        T res = json == null ? null : gson.fromJson(json, clazz);
        return res;
    }

}
