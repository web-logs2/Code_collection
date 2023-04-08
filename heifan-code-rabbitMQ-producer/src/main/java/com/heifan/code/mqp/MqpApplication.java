package com.heifan.code.mqp;

import com.heifan.code.alarm.EnableAlarmNoticeConfiguration;
import com.heifan.code.helper.SpringContextHelper;
import com.heifan.code.mdc.EnableMdcApiLogConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@EnableAlarmNoticeConfiguration
@EnableMdcApiLogConfiguration
@SpringBootApplication(scanBasePackages = "com.heifan.code")
public class MqpApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MqpApplication.class, args);
        SpringContextHelper.setApplicationContext(context);
    }
}
