package com.heifan.code.mdc;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public class MdcLogImportSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        //获取EnableMdcApiLogAutoConfiguration注解的所有属性的value
        Map<String, Object> attributes =  importingClassMetadata.getAnnotationAttributes(EnableMdcApiLogConfiguration.class.getName());
        if (null != attributes && null != attributes.get("enable")) {
            Boolean enable = (Boolean) attributes.get("enable");
            if (enable) {
                return new String[] {
                        "com.heifan.code.mdc.MdcApiLogAutoConfiguration",
                };
            }
        }
        return new String[] {};
    }
}
