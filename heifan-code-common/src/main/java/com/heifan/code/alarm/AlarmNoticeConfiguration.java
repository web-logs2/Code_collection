package com.heifan.code.alarm;

import com.heifan.code.alarm.manager.AlarmNoticeManage;
import com.heifan.code.alarm.manager.AlarmWebHookTokenManager;
import com.heifan.code.alarm.message.AlarmNoticeSendI;
import com.heifan.code.alarm.message.impl.AlarmNoticeSendImpl;
import com.heifan.code.alarm.property.AlarmNoticeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 * 预警通知配置
 **/
@EnableConfigurationProperties({AlarmNoticeProperty.class})
public class AlarmNoticeConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AlarmNoticeConfiguration.class);

    private static final String DEV = "dev";
    private static final String TEST = "test";
    private static final String PROD = "prod";

    @Autowired
    AlarmNoticeProperty alarmNoticeProperty;

    @Value("${spring.profiles.active}")
    private String active;

    @Autowired
    private AlarmWebHookTokenManager alarmWebHookTokenManager;

    @Bean
    @ConditionalOnMissingBean({AlarmNoticeManage.class})
    public AlarmNoticeManage alarmNoticeManage() throws IllegalAccessException {
        if (null != active) {
            log.info("active {}", active);
            List<String> dingTalkWebHook = null;
            if (active.contains(TEST) || active.contains(DEV)) {
                if (null == alarmNoticeProperty.getWebHookDevToken() ||
                        alarmNoticeProperty.getWebHookDevToken().isEmpty()) {
                    throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 webHookDevToken , 预警无法初始化回调信息");
                }
                dingTalkWebHook = alarmNoticeProperty.getWebHookDevToken();
            } else if (active.contains(PROD)) {
                if (null == alarmNoticeProperty.getWebHookProToken() ||
                        alarmNoticeProperty.getWebHookProToken().isEmpty()) {
                    throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 webHookProToken , 预警无法初始化回调信息");
                }
                dingTalkWebHook = alarmNoticeProperty.getWebHookProToken();
            } else {
                throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定合法的 spring.profiles.active , 钉钉预警无法初始化回调信息");
            }
            alarmNoticeProperty.setWebHook(dingTalkWebHook);
        } else {
            throw new IllegalAccessException("预警模块配置文件初始化异常,项目未指定 spring.profiles.active , 钉钉预警无法初始化回调信息");
        }
        alarmWebHookTokenManager.loadToken();
        return new AlarmNoticeManage(alarmNoticeProperty);
    }


    @Bean
    @ConditionalOnMissingBean({AlarmWebHookTokenManager.class})
    public AlarmWebHookTokenManager alarmWebHookTokenManager() {
        return new AlarmWebHookTokenManager();
    }

    @Bean
    @ConditionalOnMissingBean({AlarmNoticeSendImpl.class})
    public AlarmNoticeSendI alarmNoticeSend() {
        return new AlarmNoticeSendImpl();
    }
}
