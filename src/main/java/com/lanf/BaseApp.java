package com.lanf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * 启动类
 * @version 1.0
 * @description TODO
 * @date 2023/5/1 18:30
 */
@SpringBootApplication   //已包含@ComponentScan
@EnableScheduling   //调度任务
@EnableRetry   //Spring Retry重试
public class BaseApp {
    public static void main(String[] args) {
        SpringApplication.run(BaseApp.class, args);
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10); // 设置线程池大小，根据需要调整
        //设置线程名前缀，便于调试和日志记录
        scheduler.setThreadNamePrefix("scheduled-tasks-");
        scheduler.initialize();
        return scheduler;
    }
}
