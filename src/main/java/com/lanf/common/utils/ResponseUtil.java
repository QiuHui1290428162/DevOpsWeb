package com.lanf.common.utils;

import com.lanf.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtil {

    // 定义一个静态方法，用于将Result对象写入HTTP响应
    public static void out(HttpServletResponse response, Result r) {
        // 创建一个ObjectMapper对象，Jackson库的核心类，用于将Java对象转换为JSON格式
        ObjectMapper mapper = new ObjectMapper();

        // 设置响应状态码为200 OK
        response.setStatus(HttpStatus.OK.value());

        // 设置响应的Content-Type为application/json，并指定UTF-8编码
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try {
            // 使用ObjectMapper将Result对象写入响应的输出流中，以JSON格式返回
            mapper.writeValue(response.getWriter(), r);
        } catch (IOException e) {
            // 如果在写入时发生IOException，则打印堆栈跟踪以便调试
            e.printStackTrace();
        }
    }
}

