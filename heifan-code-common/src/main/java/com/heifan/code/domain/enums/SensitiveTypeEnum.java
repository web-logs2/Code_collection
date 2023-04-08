package com.heifan.code.domain.enums;

/**
 * @description 敏感类型枚举
 * @author HiF
 * @date 2023/3/1 11:28
 */
public enum SensitiveTypeEnum {

    /**
     * 中文名
     */
    CHINESE_NAME,
    /**
     * 身份证号
     */
    ID_CARD,
    /**
     * 座机号
     */
    FIXED_PHONE,
    /**
     * 手机号
     */
    MOBILE_PHONE,
    /**
     * 地址
     */
    ADDRESS,
    /**
     * 电子邮件
     */
    EMAIL,
    /**
     * 银行卡
     */
    BANK_CARD,
    /**
     * 公司开户银行联号
     */
    CNAPS_CODE,
    /**
     * 敏感数值
     */
    SECRET_NUM;
}
