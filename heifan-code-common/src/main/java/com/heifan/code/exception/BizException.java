package com.heifan.code.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@EqualsAndHashCode(callSuper = false)
@Data
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 谁背锅
     */
    private String[] blamedFor;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message = "";

    /**
     * 异常扩展信息
     */
    private String extMessage = "";

    /**
     * 默认的code码
     */
    protected MessageCodeEnum defaultMessageCode = MessageCodeEnum.NO_UPDATE;

    public BizException() {
        super();
        this.code = defaultMessageCode.getCode();
        this.message = defaultMessageCode.getMsg();
    }

    public BizException(MessageCodeEnum messageCode) {
        this.code = messageCode.getCode();
        this.message = messageCode.getMsg();
    }

    public BizException(String extMessage) {
        super();
        this.code = defaultMessageCode.getCode();
        this.message = extMessage;
        setMsg(extMessage);
    }

    public BizException(String extMessage, Object... args) {
        super();
        this.code = defaultMessageCode.getCode();
        this.message = defaultMessageCode.getMsg();
        setMsg(extMessage, args);
    }

    public BizException(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public BizException(int code, String message, String extMessage) {
        super();
        this.code = code;
        this.message = message;
        this.extMessage = extMessage;
    }

    public BizException(Throwable cause, String message, String extMessage) {
        super(cause);
        this.code = defaultMessageCode.getCode();
        this.message = message;
        this.extMessage = extMessage;
    }

    public BizException(Throwable cause, MessageCodeEnum messageCodeEnum, String message, String extMessage) {
        super(cause);
        this.code = messageCodeEnum.getCode();
        this.message = message;
        this.extMessage = extMessage;
    }


    public BizException(Throwable cause) {
        super(cause);
    }

    /**
     * 填充数据
     *
     * @param extMessage
     */
    private void setMsg(String extMessage) {
        this.extMessage = extMessage;
    }

    /**
     * 多参数绑定
     *
     * @param message，消息体：例如 'ABCD{}F{}H'
     * @param args           绑定数组，例如: E,G 分别替换msg中的{}
     * @return ABCDEFGH
     */
    private void setMsg(String message, Object... args) {
        String[] spi = message.split("\\{\\}");
        StringBuilder resultMsg = new StringBuilder();
        for (int index = 0; index < spi.length; index++) {
            resultMsg.append(spi[index]);
            if (index >= args.length) {
                resultMsg.append("{}");
            } else {
                resultMsg.append(args[index]);
            }
        }
        setMsg(resultMsg.toString());
    }

}
