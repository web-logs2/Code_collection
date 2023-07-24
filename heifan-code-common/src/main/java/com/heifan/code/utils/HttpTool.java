package com.heifan.code.utils;

import com.alibaba.fastjson.JSON;
import com.heifan.code.domain.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * HTTP工具类
 *
 * @author fengzx
 * @date 2018年06月27日
 */
@Slf4j
public class HttpTool {

    /**
     * 获取客户端IP地址
     *
     * @param request
     *            Http请求
     * @return String
     */
    public static String getIp(HttpServletRequest request) {
        String unknow = "unknown";

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || unknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || unknow.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || unknow.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 是否是Ajax请求
     *
     * @param request
     *            Http请求
     * @return Boolean
     */
    public static Boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("x-requested-with");
        return "XMLHttpRequest".equalsIgnoreCase(requestedWith);
    }

    /**
     * 响应JSON数据
     *
     * @param result
     *            结果
     * @param response
     *            Http响应
     */
    public static void writeJson(Result<?> result, HttpServletResponse response) {
        if (result == null) {
            log.error("result为null");
            throw new RuntimeException("响应异常");
        }
        if (response == null) {
            log.error("response为null");
            throw new RuntimeException("响应异常");
        }

        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            out = response.getWriter();
            out.write(JSON.toJSONString(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 获取token
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }

    /**
     * 获取用户标记
     */
    public static String getUserMark(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotBlank(token)) {
            return token;
        }

        Cookie[] cookies = request.getCookies();
        if (ArrayUtils.isEmpty(cookies)) {
            return "";
        }
        for (Cookie cookie : cookies) {
            if (StringUtils.containsIgnoreCase(cookie.getName(), "token")) {
                return cookie.getValue();
            }
        }
        return "";
    }

}