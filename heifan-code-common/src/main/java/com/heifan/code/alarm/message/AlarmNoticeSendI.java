package com.heifan.code.alarm.message;

import com.heifan.code.alarm.dto.ServiceNotice;

/**
 *
 * @author HiF
 * @date 2022/3/8 10:22
 */
public interface AlarmNoticeSendI {

    void send(String content);

    void send(String content, String blamedFor);

    void send(ServiceNotice serviceNotice, String... blamedFor);

    void send(ServiceNotice serviceNotice, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String blamedFor);

    void sendNotice(Throwable throwable, String extMessage, String... blamedFor);

    void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor);


}
