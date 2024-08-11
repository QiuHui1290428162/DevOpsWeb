package com.lanf.log.annotation;

import com.lanf.log.type.BusinessType;
import com.lanf.log.type.OperatorType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义操作日志记录注解
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String title() default ""; // 模块名称
    BusinessType businessType() default BusinessType.OTHER; // 操作的类型
    OperatorType operatorType() default OperatorType.MANAGE; // 操作人类别
    boolean isSaveRequestData() default true; // 是否保存请求的参数
    boolean isSaveResponseData() default true; // 是否保存响应的参数
}


