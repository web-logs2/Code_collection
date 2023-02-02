package com.heifan.code.alarm.manager;

import cn.hutool.core.util.StrUtil;
import com.heifan.code.alarm.dto.ServiceNotice;
import com.heifan.code.alarm.message.AlarmNoticeSendI;
import com.heifan.code.alarm.property.AlarmNoticeProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author HiF
 * @date 2022/3/8 10:21
 */
@Slf4j
public class AlarmNoticeManage {

    private AlarmNoticeProperty alarmNoticeProperty;

    private final Map<String, String> blameMap = new HashMap<>();

    @Autowired(required = false)
    AlarmNoticeSendI alarmNoticeSend;

    public AlarmNoticeManage(AlarmNoticeProperty alarmNoticeProperty) {
        this.alarmNoticeProperty = alarmNoticeProperty;
        if (!alarmNoticeProperty.getNotice().isEmpty()) {
            Iterator<Map.Entry<String, String>> entries = alarmNoticeProperty.getNotice().entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                String name = entry.getKey();
                String phone = entry.getValue();
                String regex = "^((1[0-9])([0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(phone);
                boolean isMatch = m.matches();
                if (!isMatch) {
                    log.error(" 预警手机号 格式不正确。name  {} phone  {}", name, phone);
                    continue;
                }
                blameMap.put(name, phone);
            }
        }
    }

    /**
     * 最基础的异常通知的创建方法
     *
     * @param blamedFor ？
     * @return
     */
    public void createNotice(ServiceNotice serviceNotice, String blamedFor) {
        blamedFor = checkBlameFor(blamedFor);
        alarmNoticeSend.send(serviceNotice, blamedFor);
    }

    /**
     * 最基础的异常通知的创建方法
     *
     * @return
     */
    public void createNoticeAll(ServiceNotice serviceNotice) {
        if (blameMap.isEmpty()) {
            String[] blamedForArr = new String[]{};
            alarmNoticeSend.send(serviceNotice, blamedForArr);
        } else {
            String[] blamedForArr = new String[blameMap.size()];
            blamedForArr = blameMap.values().toArray(blamedForArr);
            alarmNoticeSend.send(serviceNotice, blamedForArr);
        }
    }


    /**
     * 最基础的异常通知的创建方法
     *
     * @param blamedFor ？
     * @return
     */
    public void createNoticeMany(ServiceNotice serviceNotice, String... blamedFor) {
        String[] blamedForArr = checkBlameFor(blamedFor);
        alarmNoticeSend.send(serviceNotice, blamedForArr);
    }


    /**
     * 最基础的异常通知的创建方法
     *
     * @param blamedFor ？
     * @return
     */
    public void createNotice(Throwable throwable, String extMessage, String blamedFor) {
        blamedFor = checkBlameFor(blamedFor);
        alarmNoticeSend.sendNotice(throwable, extMessage, blamedFor);
    }

    /**
     * 最基础的异常通知的创建方法
     *
     * @return
     */
    public void createNoticeAll(Throwable throwable, String extMessage) {
        if (blameMap.isEmpty()) {
            String[] blamedForArr = new String[]{};
            alarmNoticeSend.sendNotice(throwable, extMessage, blamedForArr);
        } else {
            String[] blamedForArr = new String[blameMap.size()];
            blamedForArr = blameMap.values().toArray(blamedForArr);
            alarmNoticeSend.sendNotice(throwable, extMessage, blamedForArr);
        }
    }

    /**
     * 最基础的异常通知的创建方法
     *
     * @param blamedFor ？
     * @return
     */
    public void createNoticeAppTraceIdMany(Throwable throwable, String appTraceId, String extMessage, String... blamedFor) {
        String[] blamedForArr = checkBlameFor(blamedFor);
        alarmNoticeSend.sendNotice(throwable, appTraceId, extMessage, blamedForArr);
    }

    /**
     * 最基础的异常通知的创建方法
     *
     * @param blamedFor ？
     * @return
     */
    public void createNoticeMany(Throwable throwable, String extMessage, String... blamedFor) {
        String[] blamedForArr = checkBlameFor(blamedFor);
        alarmNoticeSend.sendNotice(throwable, extMessage, blamedForArr);
    }


    private String checkBlameFor(String blameFor) {
        if (StringUtils.isEmpty(blameFor) || (!blameMap.containsKey(blameFor))) {
            return blameMap.get(alarmNoticeProperty.getDefaultNotice());
        }
        return blameMap.get(blameFor);
    }

    private String[] checkBlameFor(String... blameFor) {
        if (null == blameFor || blameFor.length == 0) {
            return new String[]{alarmNoticeProperty.getDefaultNotice()};
        } else {
            Set<String> blameForSet = new HashSet<>();
            String temp = null;
            for (int i = 0; i < blameFor.length; i++) {
                if (null != blameFor[i]) {
                    temp = blameFor[i];
                    if (StrUtil.isEmpty(temp) || (!blameMap.containsKey(temp))) {
                        temp = blameMap.get(alarmNoticeProperty.getDefaultNotice());
                    } else {
                        temp = blameMap.get(temp);
                    }
                    blameForSet.add(temp);
                }
            }
            return blameForSet.toArray(new String[blameForSet.size()]);
        }
    }


}
