package com.heifan.code.config.ip;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

@Configuration
public class IpConfig extends WebMvcConfigurationSupport {

    @Resource
    IpInterceptor ipInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.ipInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
