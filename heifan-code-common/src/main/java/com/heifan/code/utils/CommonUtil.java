package com.heifan.code.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author fengzx
 * @date 2018年06月27日
 */
@Slf4j
public class CommonUtil {

    /**
     * 手机号码
     */
    public static final String MOBILE = "^1\\d{10}$";

    public static final String COLON = ":";

    /**
     * 随机32位字符串
     *
     * @return String
     */
    public static String uuid() {
        return RandomStringUtils.randomAlphanumeric(32);
    }

    /**
     * 随机指定位数字符串（英文+数字）
     *
     * @param length
     *            长度
     * @return String
     */
    public static String randomAlphanumeric(int length) {
        if (length <= 0) {
            return "";
        }
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * 随机指定位数字符串（纯英文）
     *
     * @param length
     *            长度
     * @return String
     */
    public static String randomAlphabetic(int length) {
        if (length <= 0) {
            return "";
        }
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * 随机指定位数字符串（纯数字）
     *
     * @param length
     *            长度
     * @return String
     */
    public static String randomNumeric(int length) {
        if (length <= 0) {
            return "";
        }
        return RandomStringUtils.randomNumeric(length);
    }

    /**
     * 隐藏手机中间4位
     *
     * @param phone
     *            手机号码
     * @return String
     */
    public static String hidePhone(String phone) {
        if (!isPhone(phone)) {
            return phone;
        }
        return RegExUtils.replaceAll(phone, HIDDEN_PHONE_REGEX, "$1****$2");
    }

    private static final String HIDDEN_PHONE_REGEX = "^(\\d{3})\\d{4}(\\d{4})$";

    /**
     * 是否是手机号码
     *
     * @param phone
     *            手机号码
     * @return boolean
     */
    public static boolean isPhone(String phone) {
        log.info("CommonUtil.isPhone：{}", phone);
        return matchRegex(phone, MOBILE);
    }

    /**
     * 匹配正则表达式
     * 
     * @param str
     *            待匹配内容
     * @param regex
     *            正则表达式
     * @return boolean
     */
    public static boolean matchRegex(String str, String regex) {
        if (StringUtils.isAnyBlank(str, regex)) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    /**
     * 在Redis中的key
     *
     * @param values
     *            字符串数组
     * @return 字符串
     */
    public static String redisKey(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i < values.length - 1) {
                // 分隔符
                builder.append(COLON);
            }
        }
        return builder.toString();
    }

    /**
     * 拼接字符串（非空判断）
     * 
     * @param strs
     *            字符串
     * @return 字符串
     */
    public static String append(String... strs) {
        if (ArrayUtils.isEmpty(strs)) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            if (StringUtils.isNotBlank(str)) {
                builder.append(str);
            }
        }
        return builder.toString();
    }

    /**
     * 获取第一个不为空的字符串
     *
     * @param strs
     *            字符串
     * @return 字符串
     */
    public static String firstNotBlank(String... strs) {
        if (ArrayUtils.isEmpty(strs)) {
            return "";
        }

        for (String str : strs) {
            if (StringUtils.isNotBlank(str)) {
                return str;
            }
        }
        return "";
    }

    /**
     * 替换变量
     *
     * @param content
     *            待替换内容
     * @param values
     *            变量值
     * @return 替换好的内容
     */
    public static String replaceVar(String content, JSONObject values) {
        if (StringUtils.isBlank(content)) {
            return "";
        }

        if (values != null && values.size() > 0) {
            Set<String> keySet = values.keySet();
            for (String key : keySet) {
                if (values.get(key) != null) {
                    content = content.replace("${" + key + "}", values.get(key).toString());
                }
            }
        }
        return content;
    }

    /**
     * 比较版本号
     */
    public static int compareVersion(String v1, String v2) {
        if (v1.equals(v2)) {
            return 0;
        }
        String[] version1Array = v1.split("[._]");
        String[] version2Array = v2.split("[._]");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        long diff = 0;

        while (index < minLen
            && (diff = Long.parseLong(version1Array[index]) - Long.parseLong(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Long.parseLong(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Long.parseLong(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

}