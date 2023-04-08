package com.heifan.code.config.desen;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HiF
 * @description 统一拦截器
 * @date 2023/3/1 15:00
 */
@Aspect
@Configuration
public class SensitiveAspect {

    public static final String ACCESS_EXECUTION = "execution(* com.heifan.code.*.controller..*.*(..))";

    /**
     * 注解脱敏处理
     */
    @Around(ACCESS_EXECUTION)
    public Object sensitiveClass(ProceedingJoinPoint joinPoint) throws Throwable {
        return sensitiveFormat(joinPoint);
    }

    /**
     * 注解统一拦截器
     */
    public Object sensitiveFormat(ProceedingJoinPoint joinPoint) throws Throwable {
        Object resObj = null;
        Boolean isDesen = Boolean.TRUE;
        Object obj = joinPoint.proceed();
        if (obj == null || isPrimitive(obj.getClass())) {
            return obj;
        }
        HttpServletRequest request = null;
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (isDesen) {
            resObj = DesensitizedUtils.getObj(obj);
        } else {
            resObj = obj;
        }
        return resObj;
    }

    /**
     * 基本数据类型和String类型判断
     */
    public static boolean isPrimitive(Class<?> clz) {
        try {
            if (String.class.isAssignableFrom(clz) || clz.isPrimitive()) {
                return true;
            } else {
                return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
            }
        } catch (Exception e) {
            return false;
        }
    }
}
