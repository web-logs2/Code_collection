package com.heifan.code.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @description 消息码枚举
 * @author HiF
 * @date 2023/2/2 9:34
 */
@Getter
@AllArgsConstructor
@ToString
public enum MessageCodeEnum {

    /**
     * 成功
     */
    SUCCESS(200, "成功"),

    /**
     *  未授权
     */
    NOT_LOGIN(401, "未登录系统"),
    /**
     * 请求参数不完整
     */
    PARAM_ERROR(601, "请求参数不完整。"),
    /**
     * 更新操作失败
     */
    NO_UPDATE(602, "系统繁忙，请稍后再试"),
    /**
     * 服务器异常
     */
    SERVER_ERROR(603, "系统繁忙，请稍后再试"),
    /**
     * 用户其它设备登录中
     */
    USER_OFFLINE(604, "用户其它设备登录中"),
    /**
     * 请求不在白名单内
     */
    NOT_IN_THE_WHITELIST(605, "请求不在白名单内");

    /**
     * 状态码值
     */
    private int code;
    private String msg;


}
