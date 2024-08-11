package com.lanf.common.exception;

import com.lanf.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * cache 失效异常处理类
 *
 */
@Data
public class CacheExpiredException extends RuntimeException {

    private Integer code;

    private String message;

    private  String className;

    private Exception exception;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param code
     * @param message
     */
    public CacheExpiredException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public CacheExpiredException(Integer code, String message, Exception e) {
        this.code = code;
        this.message = message;
        this.exception = e;
    }

    public CacheExpiredException(Integer code, String message, String className) {
        this.code = code;
        this.message = message;
        this.className = className;
    }

    public CacheExpiredException(Integer code, String message, String className, Exception e) {
        this.code = code;
        this.message = message;
        this.className = className;
        this.exception = e;
    }

    /**
     * 接收枚举类型对象
     *
     * @param resultCodeEnum
     */
    public CacheExpiredException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.message = resultCodeEnum.getMessage();
    }

    public CacheExpiredException(ResultCodeEnum resultCodeEnum, Exception e) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMessage(),e);
    }

    public CacheExpiredException(ResultCodeEnum resultCodeEnum,String className) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMessage(),className);
    }

    public CacheExpiredException(ResultCodeEnum resultCodeEnum,String className, Exception e) {
        this(resultCodeEnum.getCode(), resultCodeEnum.getMessage(),className,e);
    }

    @Override
    public String toString() {
        return "LanfException{" +
                "code=" + code +
                ", message=" + message +
                ", message=" + className +
                ", message=" + exception +
                '}';
    }
}
