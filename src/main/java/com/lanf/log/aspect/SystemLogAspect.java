package com.lanf.log.aspect;


import com.lanf.common.config.DynamicDataSource;
import com.lanf.common.utils.UserUtil;
import com.lanf.log.annotation.SystemLog;
import com.lanf.log.service.SysLogService;
import com.lanf.system.model.SysUser;
import com.lanf.tasks.model.TaskScheduledEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SystemLogAspect {

    private static final Logger logger = LogManager.getLogger(SystemLogAspect.class);

    @Autowired
    private SysLogService sysLogServiceImpl;

    @Pointcut("@annotation(loggable)")
    public void loggablePointcut(SystemLog loggable) {}

    // 记录方法开始执行的日志
    @Before("loggablePointcut(loggable)")
    public void logBefore(JoinPoint joinPoint, SystemLog loggable) {

        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        //instanceof 运算符来检查参数是否是某类的实例
        if ( className.equals("EmailServiceImpl") && methodName.equals("sendMail") && args[0]  instanceof TaskScheduledEmail) {
            TaskScheduledEmail task = (TaskScheduledEmail) args[0];
            logger.info("任务{}({}): 调用数据源 {} 成功", task.getTaskName(),task.getId(), task.getDatabaseName());
        }else {
            logger.info(className + "." + methodName + "方法开始执行: ");
        }


    }

    // 记录方法成功执行完成后的日志
    @AfterReturning(pointcut = "loggablePointcut(loggable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, SystemLog loggable, Object result) {

        String module = loggable.module();
        String functionName = loggable.functionName();
        String description = loggable.description();

        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        // 获取当前操作的用户名
        String username = "admin";
        SysUser sysUser = UserUtil.getUserInfo();
        if (sysUser != null) {
            username = sysUser.getUsername();
        }

        //instanceof 运算符来检查参数是否是某类的实例
        if ( className.equals("EmailServiceImpl") && methodName.equals("sendMail") && args[0]  instanceof TaskScheduledEmail) {
            TaskScheduledEmail task = (TaskScheduledEmail) args[0];
            logger.info("任务{}({}): 已完成邮件发送任务", task.getTaskName(),task.getId());
            sysLogServiceImpl.addLog(module, functionName,className
                    ,"任务"+task.getTaskName()+"("+task.getId()+") : 已完成邮件发送任务"
                    , "success", username);
        }else {
            sysLogServiceImpl.addLog(module, functionName, className, methodName + "-方法成功执行完成: " + description, "success", username);
        }


    }

    // 记录方法抛出异常后的日志
    @AfterThrowing(pointcut = "loggablePointcut(loggable)", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, SystemLog loggable, Throwable error) {
        // 如果设置了clearDataSource为true，则清除数据源
        if (loggable.clearDataSource()) {
            DynamicDataSource.clearDataSource();
        }

        String module = loggable.module();
        String functionName = loggable.functionName();
        String description = loggable.description();

        //获取类名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        //获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 获取方法的参数
        Object[] args = joinPoint.getArgs();

        // 获取当前操作的用户名
        String username = "admin";
        SysUser sysUser = UserUtil.getUserInfo();
        if (sysUser != null) {
            username = sysUser.getUsername();
        }

        if ( className.equals("EmailServiceImpl") && methodName.equals("sendMail") && args[0]  instanceof TaskScheduledEmail) {
            TaskScheduledEmail task = (TaskScheduledEmail) args[0];
            logger.info("任务{}({}): 发送邮件任务执行失败", task.getTaskName(),task.getId());
            sysLogServiceImpl.addLog(module, functionName, className
                    ,"任务"+task.getTaskName()+"("+task.getId()+") : 发送邮件任务执行失败,"+error.getMessage()
                    , "error",username);
        }else {
            sysLogServiceImpl.addLog(module, functionName, className, methodName + "-方法执行时发生异常: " + error.getMessage(), "error", username);
        }
    }
}

