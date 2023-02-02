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
     * 请求参数不完整
     */
    PARAM_ERROR(201, "请求参数不完整。"),
    /**
     * 更新操作失败
     */
    NO_UPDATE(202, "系统繁忙，请稍后再试"),
    /**
     * 服务器异常
     */
    SERVER_ERROR(203, "系统繁忙，请稍后再试"),
    /**
     *  未授权
     */
    NOT_LOGIN(204, "未登录系统"),
    /**
     * 用户其它设备登录中
     */
    USER_OFFLINE(205, "用户其它设备登录中");

    /**
     * 状态码值
     */
    private int code;
    private String msg;


}
