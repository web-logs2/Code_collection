package com.heifan.code.mdc;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MdcLogImportSelector.class)
public @interface EnableMdcApiLogConfiguration {

    boolean enable() default true;
}
