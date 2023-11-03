package com.zznode.dhmp.core.exception;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 封装运行异常为CommonException.
 *
 * @author 王俊
 * @date create in 2023/6/28 11:01
 */
public class CommonException extends RuntimeException {

    private final transient Object[] parameters;

    private String code;

    public CommonException(String code, Object... parameters) {
        super(code);
        this.parameters = parameters;
        this.code = code;
    }

    public CommonException(String code, Throwable cause, Object... parameters) {
        super(code, cause);
        this.parameters = parameters;
        this.code = code;
    }

    public CommonException(String code, Throwable cause) {
        super(code, cause);
        this.code = code;
        this.parameters = new Object[]{};
    }


    public CommonException(Throwable cause, Object... parameters) {
        super(cause);
        this.parameters = parameters;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public String getCode() {
        return code;
    }

    public String getTrace() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = null;
        try {
            ps = new PrintStream(baos, false, StandardCharsets.UTF_8);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        this.printStackTrace(ps);
        ps.flush();
        return baos.toString(StandardCharsets.UTF_8);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new LinkedHashMap<>();
        map.put("code", code);
        map.put("message", super.getMessage());
        return map;
    }
}
