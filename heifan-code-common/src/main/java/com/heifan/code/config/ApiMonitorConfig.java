package com.heifan.code.config;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @description Api监测配置
 * @author HiF
 * @date 2023/2/20 16:00
 */
@Slf4j
@Aspect
@Component
public class ApiMonitorConfig {

    Long startTime;

    Long endTime;

    /**
     * 打印接口请求参数
     *
     * @param joinPoint joinPoint
     */
    @Before("within(com.heifan.code.*.controller.*)")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        HttpServletRequest request = null;
        log.info("--------------------------------");
        startTime = System.currentTimeMillis();
        if (null != httpServletRequest) {
            request = httpServletRequest.getRequest();
            log.info(" HTTP URL Method :  {}#{}", request.getRequestURI(), request.getMethod());
        }
        log.info(" Class Method    :  {}#{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        log.info(" Args   : {}", JSONUtil.toJsonStr(arguments));
        log.info("--------------------------------");
    }

    @After("within(com.heifan.code.*.controller.*)")
    public void after(JoinPoint joinPoint) {
        endTime = System.currentTimeMillis();
        Long runTime = endTime - startTime;
        log.info(" Runtime:   {} ms", runTime);
    }

}
