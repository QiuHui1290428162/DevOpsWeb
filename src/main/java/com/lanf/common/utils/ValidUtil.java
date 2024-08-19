package com.lanf.common.utils;

import org.springframework.scheduling.support.CronSequenceGenerator;

//校验工具类
public class ValidUtil {

    //校验cron表达式
    public static boolean isValidCronExpression(String cronExpression) {
        try {
            // 尝试创建一个 CronSequenceGenerator 实例，如果失败则抛出异常
            new CronSequenceGenerator(cronExpression);
            return true; // 如果没有异常抛出，则表示 Cron 表达式合法
        } catch (IllegalArgumentException e) {
            return false; // 如果抛出异常，表示 Cron 表达式不合法
        }
    }
}
