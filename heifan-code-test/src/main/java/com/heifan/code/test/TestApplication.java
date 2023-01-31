package com.heifan.code.test;

import com.heifan.code.helper.SpringContextHelper;
import com.heifan.code.mdc.EnableMdcApiLogConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@EnableMdcApiLogConfiguration
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(TestApplication.class, args);
        SpringContextHelper.setApplicationContext(context);
    }
}
