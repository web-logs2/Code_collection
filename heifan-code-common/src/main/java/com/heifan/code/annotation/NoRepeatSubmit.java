package com.heifan.code.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 防重复提交
 *
 * @author fengzx
 * @date 2019年06月06日
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRepeatSubmit {

    /**
     * 防重复秒数，默认3秒
     */
    int avoidRepeatSeconds() default 3;

    /**
     * 错误提示语
     */
    String errorMsg() default "操作过于频繁";

}