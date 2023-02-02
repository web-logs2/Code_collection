package com.heifan.code.alarm.message.impl;

import com.google.gson.Gson;
import com.heifan.code.alarm.AlarmNoticeEnum;
import com.heifan.code.alarm.client.SimpleHttpClient;
import com.heifan.code.alarm.dto.AlarmResult;
import com.heifan.code.alarm.dto.ExceptionNotice;
import com.heifan.code.alarm.dto.NoticeText;
import com.heifan.code.alarm.dto.ServiceNotice;
import com.heifan.code.alarm.manager.AlarmWebHookTokenManager;
import com.heifan.code.alarm.message.AlarmNoticeSendI;
import com.heifan.code.alarm.property.AlarmNoticeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 * @author HiF
 * @date 2022/3/8 10:22
 */ 
@Slf4j
public class AlarmNoticeSendImpl implements AlarmNoticeSendI {

    private static final String DD_ULR = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    private static final String WX_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=%s";

    private static final String FS_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/%s";

    @Autowired(required = false)
    private AlarmWebHookTokenManager alarmWebHookTokenManager;

    @Autowired(required = false)
    private AlarmNoticeProperty alarmNoticeProperty;

    @Override
    public void send(String content) {
        send(content, alarmNoticeProperty.getDefaultNotice());
    }

    @Override
    public void send(String content, String blamedFor) {
        Gson gson = new Gson();
        Object exceptionTxtNotice = new Object();
        String url = null;
        if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
            exceptionTxtNotice = new NoticeText.WeChatText(content,
                    new String[]{blamedFor});
            url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
        } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.DD)) {
            exceptionTxtNotice = new NoticeText.DingTalkText(content,
                    new String[]{blamedFor});
            url = String.format(DD_ULR, alarmWebHookTokenManager.availableToken());
        } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.FS)) {
            exceptionTxtNotice = new NoticeText.FeiShuText(content,
                    new String[]{blamedFor});
            url = String.format(FS_URL, alarmWebHookTokenManager.availableToken());
        }
        log.info("{} ", gson.toJson(exceptionTxtNotice));
        try {
            AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                    AlarmResult.class);
            log.info("result {} ", result);
        } catch (Exception e) {
            log.info("通知失败 {}", content);
        }
    }

    @Override
    public void send(ServiceNotice serviceNotice, String... blamedFor) {
        if (blamedFor != null) {
            Gson gson = new Gson();
            Object exceptionTxtNotice = new Object();
            String url = null;
            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                exceptionTxtNotice = new NoticeText.WeChatText(serviceNotice.createText(),
                        blamedFor);
                url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.DD)) {
                exceptionTxtNotice = new NoticeText.DingTalkText(serviceNotice.createText(),
                        blamedFor);
                url = String.format(DD_ULR, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.FS)) {
                exceptionTxtNotice = new NoticeText.FeiShuText(serviceNotice.createText(),
                        blamedFor);
                url = String.format(FS_URL, alarmWebHookTokenManager.availableToken());
            }
            log.info("{} ", gson.toJson(exceptionTxtNotice));
            try {
                AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        AlarmResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {}", serviceNotice);
            }
        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

    @Override
    public void send(ServiceNotice serviceNotice, String blamedFor) {
        send(serviceNotice, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String blamedFor) {
        sendNotice(throwable, extMessage, new String[]{blamedFor});
    }

    @Override
    public void sendNotice(Throwable throwable, String extMessage, String... blamedFor) {
        if (blamedFor != null) {

            Gson gson = new Gson();
            Object exceptionTxtNotice = new Object();
            String url = null;

            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable,
                    new String[]{extMessage});
            exceptionNotice.setProject(alarmNoticeProperty.getProjectName());

            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                exceptionTxtNotice = new NoticeText.WeChatText(exceptionNotice.createWorkWxText(),
                        blamedFor);
                url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.DD)) {
                exceptionTxtNotice = new NoticeText.DingTalkText(exceptionNotice.createDingDingText(),
                        blamedFor);
                url = String.format(DD_ULR, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.FS)) {
                exceptionTxtNotice = new NoticeText.FeiShuText(exceptionNotice.createFeiShuText(),
                        blamedFor);
                url = String.format(FS_URL, alarmWebHookTokenManager.availableToken());
            }
            log.info("{} ", gson.toJson(exceptionTxtNotice));
            try {
                AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        AlarmResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {}", exceptionNotice);
            }
        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

    @Override
    public void sendNotice(Throwable throwable, String appTraceId, String extMessage, String... blamedFor) {
        if (blamedFor != null) {

            Gson gson = new Gson();
            Object exceptionTxtNotice = new Object();
            String url = null;

            ExceptionNotice exceptionNotice = new ExceptionNotice(throwable, appTraceId,
                    new String[]{extMessage});
            exceptionNotice.setProject(alarmNoticeProperty.getProjectName());

            if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.WX)) {
                exceptionTxtNotice = new NoticeText.WeChatText(exceptionNotice.createWorkWxText(),
                        blamedFor);
                url = String.format(WX_URL, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.DD)) {
                exceptionTxtNotice = new NoticeText.DingTalkText(exceptionNotice.createDingDingText(),
                        blamedFor);
                url = String.format(DD_ULR, alarmWebHookTokenManager.availableToken());
            } else if (alarmNoticeProperty.getType().equals(AlarmNoticeEnum.FS)) {
                exceptionTxtNotice = new NoticeText.FeiShuText(exceptionNotice.createFeiShuText(),
                        blamedFor);
                url = String.format(FS_URL, alarmWebHookTokenManager.availableToken());
            }
            if (log.isDebugEnabled()) {
                log.debug(gson.toJson(exceptionTxtNotice));
            }
            try {
                AlarmResult result = SimpleHttpClient.post(url, exceptionTxtNotice,
                        AlarmResult.class);
                log.info("result {} ", result);
            } catch (Exception e) {
                log.info("异常预警失败 {} {}", exceptionNotice, e);
            }

        } else {
            log.info("无法进行通知，不存在背锅侠");
        }
    }

}
