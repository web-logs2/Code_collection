package com.heifan.code.alarm.dto;

import lombok.Data;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class ExceptionNotice {

    /**
     * 工程名
     */
    private String project;

    /**
     * 异常的标识码
     */
    private String uid;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 方法参数信息
     */
    private List<Object> parames;

    /**
     * app 链路id
     */
    private String appTraceId;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    /**
     * 异常追踪信息
     */
    private List<String> traceInfo = new ArrayList<>();

    public ExceptionNotice(Throwable ex, Object[] args) {
        this.exceptionMessage = gainExceptionMessage(ex);
        this.parames = args == null ? null : Arrays.stream(args).collect(toList());
        if (null != ex) {
            List<StackTraceElement> list = Arrays.stream(ex.getStackTrace())
                    .filter(x -> x.getFileName() != null)
                    .filter(x -> !x.getFileName().equals("<generated>")).limit(10).collect(toList());
            if (list.size() > 0) {
                this.traceInfo = list.stream().map(x -> x.toString()).collect(toList());
                this.methodName = list.get(0).getMethodName();
                this.classPath = list.get(0).getClassName();
            }
        }
        this.uid = calUid();
    }

    public ExceptionNotice(Throwable ex, String appTraceId, Object[] args) {
        this.exceptionMessage = gainExceptionMessage(ex);
        this.parames = args == null ? null : Arrays.stream(args).collect(toList());
        if (null != ex) {
            List<StackTraceElement> list = Arrays.stream(ex.getStackTrace())
                    .filter(x -> x.getFileName() != null)
                    .filter(x -> !x.getFileName().equals("<generated>")).limit(10).collect(toList());
            if (list.size() > 0) {
                this.traceInfo = list.stream().map(x -> x.toString()).collect(toList());
                this.methodName = list.get(0).getMethodName();
                this.classPath = list.get(0).getClassName();
            }
        }
        this.appTraceId = appTraceId;
        this.uid = calUid();
    }

    private String gainExceptionMessage(Throwable exception) {
        if (null != exception) {
            String em = exception.toString();
            if (exception.getCause() != null) {
                em = String.format("%s\r\n\tcaused by : %s", em, gainExceptionMessage(exception.getCause()));
            }
            return em;
        }
        return "exception 为 null 无法展示堆栈信息";
    }

    private String calUid() {
        String md5 = DigestUtils.md5DigestAsHex(
                String.format("%s-%s", exceptionMessage, traceInfo.size() > 0 ? traceInfo.get(0) : "").getBytes());
        return md5;
    }

    public String createDingDingText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("## 告警信息").append("\n");
        stringBuilder.append("### 工程名：").append("\n")
                .append("> ").append(project).append("\n");
        stringBuilder.append("### 类路径：").append("\n")
                .append("> ").append(classPath).append("\n");
        stringBuilder.append("### 方法名：").append("\n")
                .append("> ").append(methodName).append("\n");
        stringBuilder.append("### 链路标志：").append("\n")
                .append("> ").append(appTraceId).append("\n");
        stringBuilder.append("### 异常标志：").append("\n")
                .append("> ").append(uid).append("\n");
        if (parames != null && parames.size() > 0) {
            stringBuilder.append("### 异常扩展信息：").append("\n")
                    .append("> ")
                    .append(String.join(",", parames.stream().limit(3).map(x -> x.toString()).collect(toList()))).append("\r\n");
        }
        stringBuilder.append("### 异常信息：").append("\n")
                .append("> ").append(exceptionMessage).append("\n");
        stringBuilder.append("### 异常追踪：").append("\n")
                .append(" --- ")
                .append("\n")
                .append(" ```java ")
                .append(String.join("\n", traceInfo)).append("\n")
                .append(" ``` ");
        return stringBuilder.toString();
    }

    public String createWorkWxText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### 告警信息\n");
        stringBuilder.append(">工程名：").append(project).append("\n");
        stringBuilder.append(">类路径：").append(classPath).append("\n");
        stringBuilder.append(">方法名：").append(methodName).append("\n");
        stringBuilder.append(">链路标志：").append(appTraceId).append("\n");
        stringBuilder.append(">异常标志：").append(uid).append("\n");
        stringBuilder.append(">异常扩展信息：");
        if (parames != null && parames.size() > 0) {
            stringBuilder.append(String.join(",", parames.stream().limit(3).map(x -> x.toString()).collect(toList()))).append("\r\n");
        }
        stringBuilder.append("异常信息：").append(exceptionMessage).append("\n");
        StringBuilder parameBuilder = new StringBuilder();
        parameBuilder.append(String.join(",", traceInfo.stream().limit(3).map(x -> x.toString()).collect(toList()))).append("\r\n");
        if (parameBuilder.toString().length() > 2500) {
            String str = parameBuilder.toString().substring(0, 2500);
            stringBuilder.append("异常追踪：").append("\n`").append(str).append("`\n");
        } else {
            stringBuilder.append("异常追踪：").append("\n`").append(parameBuilder.toString()).append("`\n");
        }
        return stringBuilder.toString();
    }

    public String createFeiShuText() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("### 告警信息\n");
        stringBuilder.append(">工程名：").append(project).append("\n");
        stringBuilder.append(">类路径：").append(classPath).append("\n");
        stringBuilder.append(">方法名：").append(methodName).append("\n");
        stringBuilder.append(">链路标志：").append(appTraceId).append("\n");
        stringBuilder.append(">异常标志：").append(uid).append("\n");
        stringBuilder.append(">异常扩展信息：");
        if (parames != null && parames.size() > 0) {
            stringBuilder.append(String.join(",", parames.stream().limit(3).map(x -> x.toString()).collect(toList()))).append("\r\n");
        }
        stringBuilder.append("异常信息：").append(exceptionMessage).append("\n");
        StringBuilder parameBuilder = new StringBuilder();
        parameBuilder.append(String.join(",", traceInfo.stream().limit(3).collect(toList()))).append("\r\n");
        if (parameBuilder.toString().length() > 2500) {
            String str = parameBuilder.substring(0, 2500);
            stringBuilder.append(">>异常追踪：").append("\n`").append(str).append("`\n");
        } else {
            stringBuilder.append(">>异常追踪：").append("\n`").append(parameBuilder).append("`\n");
        }
        return stringBuilder.toString();
    }
}
