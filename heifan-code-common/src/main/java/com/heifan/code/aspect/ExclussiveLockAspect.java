package com.heifan.code.aspect;

import com.heifan.code.annotation.ExclussiveLock;
import com.heifan.code.domain.result.Result;
import com.heifan.code.redis.RedisCacheService;
import com.heifan.code.utils.CommonUtil;
import com.heifan.code.utils.HttpTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 排他锁AOP配置
 *
 * @author fengzx
 * @date 2018年12月26日
 */
@Slf4j
@Aspect
public class ExclussiveLockAspect {

    /**
     * 排他锁
     */
    public static final String EXCLUSSIVE_LOCK = "LOCK:EXCLUSSIVE";

    @Resource
    private RedisCacheService redisCacheService;

    @Pointcut(value = "@annotation(com.heifan.code.annotation.ExclussiveLock)")
    public void exclussiveLockPointcut() {

    }

    @Around(value = "exclussiveLockPointcut()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 获取request、response
            HttpServletRequest request = attributes.getRequest();
            HttpServletResponse response = attributes.getResponse();
            // 获取method
            MethodSignature methodSignature = (MethodSignature)proceedingJoinPoint.getSignature();
            Method method = methodSignature.getMethod();
            // 从method上获取注解
            ExclussiveLock exclussiveLock = method.getAnnotation(ExclussiveLock.class);
            if (exclussiveLock != null) {
                // 排他锁的redisKey
                String redisKey = CommonUtil.redisKey(EXCLUSSIVE_LOCK, request.getRequestURI());
                // 排他锁
                if (!redisCacheService.setNx(redisKey, exclussiveLock.exclussiveSeconds())) {
                    log.warn("排他锁：{} -> {}", exclussiveLock.errorMsg(), redisKey);
                    Result<?> result = Result.failure(exclussiveLock.errorMsg());
                    HttpTool.writeJson(result, response);
                    // 系统繁忙，直接返回给前端
                    return null;
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

    @AfterReturning(value = "exclussiveLockPointcut()")
    public void doAfterReturning() {
        // 处理完请求
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 获取request
            HttpServletRequest request = attributes.getRequest();
            // 排他锁的redisKey
            String redisKey = CommonUtil.redisKey(EXCLUSSIVE_LOCK, request.getRequestURI());
            // 删除redisKey
            redisCacheService.del(redisKey);
        }
    }

    @AfterThrowing(value = "exclussiveLockPointcut()")
    public void doAfterThrowing() {
        // 发生异常
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 获取request
            HttpServletRequest request = attributes.getRequest();
            // 排他锁的redisKey
            String redisKey = CommonUtil.redisKey(EXCLUSSIVE_LOCK, request.getRequestURI());
            // 删除redisKey
            redisCacheService.del(redisKey);
        }
    }

}