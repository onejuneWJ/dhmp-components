package com.zznode.dhmp.data.exception;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.Arrays;

/**
 * 异常返回对象
 *
 * @author 王俊
 * @date create in 2023/6/28 10:27
 */
public class ExceptionResponse {

    public static final String FAILED_FAILED = "failed";
    public static final String FAILED_MESSAGE = "message";
    public static final String FAILED_EXCEPTION = "trace";
    public static final String FAILED_THROWABLE = "throwable";

    private Boolean failed;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String exception;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] trace;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String[] throwable;

    public ExceptionResponse() {
        this.failed = true;
    }

    public ExceptionResponse(String message) {
        this(true, message);
    }

    public ExceptionResponse(Boolean failed, String message) {
        this.failed = failed;
        this.message = message;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String[] getTrace() {
        return trace;
    }

    @JsonSetter
    public void setTrace(String[] trace) {
        this.trace = trace;
    }

    public void setTrace(StackTraceElement[] trace) {
        this.trace = Arrays.stream(trace).map(StackTraceElement::toString).toArray(String[]::new);
    }

    public String[] getThrowable() {
        return throwable;
    }

    @JsonSetter
    public void setThrowable(String[] throwable) {
        this.throwable = throwable;
    }

    public void setThrowable(String message, StackTraceElement[] trace) {
        String[] strings = Arrays.stream(trace).map(StackTraceElement::toString).toArray(String[]::new);
        this.throwable = ArrayUtil.insert(strings, 0, message);
    }


}
