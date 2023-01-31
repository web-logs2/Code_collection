package com.heifan.code.domain.result;

import com.heifan.code.domain.constant.SysConstant;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 统一返回结果
 * @author HiF
 * @date 2023/1/31 10:42
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3320624190109556619L;

    private Boolean success;
    private Integer code;
    private String msg;
    private T data;

    private Result() {
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMsg(null);
        return result;
    }

    public static <T> Result<T> success(Integer status, String msg) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(status);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMsg(null);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.TRUE);
        result.setCode(SysConstant.ResultCode.SUCCESS);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> failure(String msg) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(SysConstant.ResultCode.FAILURE);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> failure(Integer status, String msg) {
        Result<T> result = new Result<>();
        result.setSuccess(Boolean.FALSE);
        result.setCode(status);
        result.setMsg(msg);
        return result;
    }

}
