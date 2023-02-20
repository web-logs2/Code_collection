package com.heifan.code.mdc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author z201.coding@gamil.com
 * 日志拦截器，排除对spring cloud gateway的影响 (WebMvcConfigurer)
 */
@ConditionalOnClass(WebMvcConfigurer.class)
@Slf4j
public class MdcApiLogAutoConfiguration {

    @Bean
    public FilterRegistrationBean requestContextRepositoryFilterRegistrationBean() {
        FilterRegistrationBean<MdcTraceContextFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new MdcTraceContextFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("mdcTraceContextFilter");
        return filterRegistrationBean;
    }
}
