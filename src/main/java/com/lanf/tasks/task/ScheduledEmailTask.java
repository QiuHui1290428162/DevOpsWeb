package com.lanf.tasks.task;

import com.lanf.common.exception.CacheExpiredException;
import com.lanf.common.info.Constant;
import com.lanf.common.utils.ValidUtil;
import com.lanf.log.service.SysLogService;
import com.lanf.tasks.model.TaskScheduledEmail;
import com.lanf.tasks.service.EmailService;
import com.lanf.tasks.service.TaskScheduledEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Profile("prod")  // 只有在prod环境时才启用
public class ScheduledEmailTask {

    private static final Logger logger = LogManager.getLogger(ScheduledEmailTask.class);

    @Autowired
    private TaskScheduledEmailService taskScheduledEmailServiceImpl;

    @Autowired
    private EmailService emailServiceImpl;

    @Autowired
    private SysLogService sysLogServiceImpl;

    @Qualifier("taskScheduler")//指定装配的类
    @Autowired
    private TaskScheduler taskScheduler;

    // 用于缓存已经调度的任务
    private final Map<String, ScheduledFuture<?>> scheduledTasksMap = new ConcurrentHashMap<>();

    /**
     * 使用@Scheduled, 可以定时执行任务
     * *
     * 秒（Seconds）：0 - 在第0秒开始执行。
     * 分钟（Minutes）：0/30 - 从第0分钟开始，每隔30分钟执行一次。
     * 小时（Hours）：* - 每小时执行。
     * 日（Day of month）：* - 每天执行。
     * 月（Month）：* - 每月执行。
     * 星期（Day of week）：? - 不指定具体的星期几。
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void scheduleTasks() {
        logger.info("开始执行邮件发送调度任务...");

        //获取数据表所有任务, 再每一条执行
        List<TaskScheduledEmail> tasks = taskScheduledEmailServiceImpl.queryList(null);

        // 取消所有已经调度的任务
        scheduledTasksMap.forEach((taskId, scheduledFuture) -> {
            scheduledFuture.cancel(true);
        });
        scheduledTasksMap.clear();

        for (TaskScheduledEmail task : tasks) {
            // 跳过已经调度过该任务
            if (scheduledTasksMap.containsKey(task.getId())) {
                continue;
            }

            if (task.getStatus()==1 && task.getIsDeleted()==0 && !task.getScheduledTime().equals("0 0 0 0 0 0")) {
                scheduleTask(task);
            } else {
                logger.warn("任务{}({}): 未执行邮件发送任务。", task.getTaskName(), task.getId());
            }
        }
    }

    //调度单个任务的方法
    public void scheduleTask(TaskScheduledEmail task) {
        // 检查 Cron 表达式是否合法
        if (ValidUtil.isValidCronExpression(task.getScheduledTime())) {
            //用于定义任务的执行时间
            CronTrigger cronTrigger = new CronTrigger(task.getScheduledTime());
            ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(() -> {
                logger.info("执行邮件发送任务 {}({}): {}", task.getTaskName(),task.getId(), task.getScheduledTime());

                ExecTask(task);
            }, cronTrigger);

            //记录已执行任务
            scheduledTasksMap.put(task.getId(), scheduledFuture);

        }else {
            sysLogServiceImpl.addLog("定时任务", "发送邮件", "ScheduledEmailTask",
                    "任务" + task.getTaskName() + "(" + task.getId() + ") : 发送邮件失败，无效的Cron表达式: " + task.getScheduledTime(),
                    "error", "admin");
        }

    }

    /**
     * 多设置一层方法, 用于发送邮件错误时,触发重试机制
     * &#064;Retryable注解用于指定异常触发重试，设置最大重试次数为3次，每次重试间隔2分钟（120000毫秒）。
     */
    @Retryable(include = {CacheExpiredException.class, Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 120000))
    public void  ExecTask(TaskScheduledEmail task) {
        emailServiceImpl.sendMail(task);
    }

    @Recover
    public void recover(CacheExpiredException e, TaskScheduledEmail task) {
        // 记录最终失败日志
        logger.error("任务{}({}): 邮件发送失败重试3次后仍未成功，错误信息: {}", task.getTaskName(), task.getId(), e.getMessage(), e);
    }

    @Recover
    public void recover(Exception e, TaskScheduledEmail task){
        // 记录最终失败日志
        logger.error("任务{}({}): 邮件发送失败重试3次后仍未成功，错误信息: {}", task.getTaskName(), task.getId(), e.getMessage(), e);
    }
}
