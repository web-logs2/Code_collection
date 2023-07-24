package com.heifan.code.aspect;

import com.heifan.code.annotation.NoRepeatSubmit;
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
 * 防重复提交AOP配置
 * @author HiF
 * @date 2023/7/24 9:48
 */
@Slf4j
@Aspect
public class NoRepeatSubmitAspect {
    
    private static final String NO_REPEAT_SUBMIT_LOCK = "LOCK:NO_REPEAT_SUBMIT";

    @Resource
    private RedisCacheService redisCacheService;

    @Pointcut(value = "@annotation(com.heifan.code.annotation.NoRepeatSubmit)")
    public void noRepeatSubmitPointcut() {

    }

    @Around(value = "noRepeatSubmitPointcut()")
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
            NoRepeatSubmit noRepeatSubmit = method.getAnnotation(NoRepeatSubmit.class);
            if (noRepeatSubmit != null) {
                // 防重复提交的redisKey
                String redisKey = CommonUtil.redisKey(NO_REPEAT_SUBMIT_LOCK,
                    request.getRequestURI() + "#" + HttpTool.getUserMark(request));
                // 防重复提交
                if (!redisCacheService.setNx(redisKey, noRepeatSubmit.avoidRepeatSeconds())) {
                    log.warn("防重复提交锁：{} -> {}", noRepeatSubmit.errorMsg(), redisKey);
                    Result<?> result = Result.failure(noRepeatSubmit.errorMsg());
                    HttpTool.writeJson(result, response);
                    // 操作频繁，直接返回给前端
                    return null;
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

    @AfterReturning(value = "noRepeatSubmitPointcut()")
    public void doAfterReturning() {
        // 处理完请求
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 获取request
            HttpServletRequest request = attributes.getRequest();
            // 防重复提交的redisKey
            String redisKey = CommonUtil.redisKey(NO_REPEAT_SUBMIT_LOCK,
                request.getRequestURI() + "#" + HttpTool.getUserMark(request));
            // 删除redisKey
            redisCacheService.del(redisKey);
        }
    }

    @AfterThrowing(value = "noRepeatSubmitPointcut()")
    public void doAfterThrowing() {
        // 发生异常
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            // 获取request
            HttpServletRequest request = attributes.getRequest();
            // 防重复提交的redisKey
            String redisKey = CommonUtil.redisKey(NO_REPEAT_SUBMIT_LOCK,
                request.getRequestURI() + "#" + HttpTool.getUserMark(request));
            // 删除redisKey
            redisCacheService.del(redisKey);
        }
    }

}