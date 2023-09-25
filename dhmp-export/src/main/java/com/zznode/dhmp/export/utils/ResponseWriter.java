package com.zznode.dhmp.export.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * ResponseWriter 注意，调用 ResponseWriter 后，不应该再执行其它方法，response 已经关闭.
 *
 * @author 王俊
 */
public class ResponseWriter {

    private ResponseWriter() {
    }

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static void write(HttpServletResponse response, Object o) throws IOException {
        String value = OBJECT_MAPPER.writeValueAsString(o);
        write(response, value);
    }

    private static void write(HttpServletResponse response, String json) throws IOException {
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        response.getWriter().write(json);
    }

}
