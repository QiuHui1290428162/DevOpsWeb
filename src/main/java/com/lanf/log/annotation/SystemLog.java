package com.lanf.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    String module() default "";  // 模块名称
    String functionName() default "";  // 功能名称
    String description() default "";  // 操作描述
    boolean clearDataSource() default false;  // 是否在方法执行后清除数据源
}
