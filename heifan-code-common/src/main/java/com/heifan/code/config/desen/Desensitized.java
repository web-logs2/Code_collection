package com.heifan.code.config.desen;

import com.heifan.code.domain.enums.SensitiveTypeEnum;

import java.lang.annotation.*;

/**
 * @description 脱敏注解
 * @author HiF
 * @date 2023/3/1 14:24
 */
@Documented
@Inherited
@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Desensitized {

    /**
     * 脱敏类型(规则)
     */
    SensitiveTypeEnum type();

    /**
     * 判断注解是否生效的方法
     */
    String isEffectiveMethod() default "";
}
