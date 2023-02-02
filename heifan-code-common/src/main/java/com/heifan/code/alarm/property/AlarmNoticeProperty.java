package com.heifan.code.alarm.property;

import com.heifan.code.alarm.AlarmNoticeEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 *
 * @author HiF
 * @date 2022/3/8 10:22
 */
@ConfigurationProperties(prefix = "alarm")
@Getter
@Setter
public class AlarmNoticeProperty {

    /**
     * 异常工程名
     */
    @Value("${spring.application.name?:['未指定项目名称']}")
    private String projectName;

    /**
     * 默认通知人，当异常通知找不到背锅侠时，就用默认背锅侠
     */
    private String defaultNotice;

    /**
     * webHook 开发环境
     */
    private List<String> webHookDevToken;

    /**
     * webHook 正式环境
     */
    private List<String> webHookProToken;

    /**
     * webHook
     */
    private List<String> webHook;

    /**
     * 令牌每分钟限流
     */
    private Integer tokenLimitPerMinute;

    /**
     * 发送钉钉异常通知给谁
     */
    private Map<String, String> notice;

    /**
     * 环境
     */
    private AlarmNoticeEnum type;

}
