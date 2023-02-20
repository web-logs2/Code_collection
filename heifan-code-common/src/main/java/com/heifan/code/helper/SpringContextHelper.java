package com.heifan.code.helper;

import com.heifan.code.domain.enums.EnvEnum;
import org.springframework.context.ApplicationContext;

/**
 * @description Spring上下文工具类
 * @author HiF
 * @date 2023/1/31 10:47
 */
public class SpringContextHelper {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String beanId, Class<T> clazz) {
        return applicationContext.getBean(beanId, clazz);
    }

    /**
     * 是否是单元测试环境
     */
    public static Boolean isJunitEnv() {
        return isCurrEnv(EnvEnum.junit);
    }

    /**
     * 是否是开发环境
     */
    public static Boolean isDevEnv() {
        return isCurrEnv(EnvEnum.dev);
    }

    /**
     * 是否是测试环境
     */
    public static Boolean isTestEnv() {
        return isCurrEnv(EnvEnum.test);
    }

    /**
     * 是否是生产环境
     */
    public static Boolean isProdEnv() {
        return isCurrEnv(EnvEnum.prod);
    }

    /**
     * 是否本地开发环境
     *
     * @return
     */
    public static Boolean isLocalEnv() {
        return isCurrEnv(EnvEnum.local);
    }

    private static boolean isCurrEnv(EnvEnum envEnum) {

        if (null == applicationContext ||
                null == applicationContext.getEnvironment()) {
            return false;
        }
        // 获取profiles
        String[] activeProfiles = applicationContext.getEnvironment().getActiveProfiles();
        if (activeProfiles.length == 0) {
            return false;
        }
        for (String profile : activeProfiles) {
            if (profile.contains(envEnum.name())) {
                return true;
            }
        }
        return false;
    }

}