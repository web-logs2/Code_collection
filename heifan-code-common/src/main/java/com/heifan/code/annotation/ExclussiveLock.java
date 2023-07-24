package com.heifan.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排他锁
 * @author HiF
 * @date 2023/7/24 9:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExclussiveLock {

    /**
     * 排他秒数，默认10秒
     */
    int exclussiveSeconds() default 10;

    /**
     * 错误提示语
     */
    String errorMsg() default "系统繁忙";

}