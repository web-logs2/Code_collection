package com.heifan.code.alarm.manager;

import com.heifan.code.alarm.property.AlarmNoticeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.synchronizedMap;

/**
 *
 * @author HiF
 * @date 2022/3/8 10:21
 */
@Slf4j
@Component
public class AlarmWebHookTokenManager {

    @Autowired(required = false)
    AlarmNoticeProperty alarmNoticeProperty;

    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger(0);

    private final Map<Integer, String> tokenMaps = synchronizedMap(new LinkedHashMap<>());

    public void loadToken() throws IllegalAccessException {
        log.info("init alarmDingTalkWebHookTokenManager loadToken");
        tokenMaps.clear();
        if (null != alarmNoticeProperty.getWebHook() && !alarmNoticeProperty.getWebHook().isEmpty()) {
            for (int i = 0; i < alarmNoticeProperty.getWebHook().size(); i++) {
                tokenMaps.put(i, alarmNoticeProperty.getWebHook().get(i));
            }
        } else {
            throw new IllegalAccessException("预警模块配置文件初始化异常,项目未初始化 webHook , 钉钉预警无法初始化回调信息");
        }
    }

    public String availableToken() {
        int length = tokenMaps.size();
        int index = counter.incrementAndGet();
        return tokenMaps.get(index % length);
    }

}
