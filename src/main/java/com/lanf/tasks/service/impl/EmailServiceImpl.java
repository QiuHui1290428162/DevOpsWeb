package com.lanf.tasks.service.impl;

import com.lanf.common.config.DynamicDataSource;
import com.lanf.common.info.ApplicationProperty;
import com.lanf.common.info.Constant;
import com.lanf.common.utils.StringUtil;
import com.lanf.system.service.SysLogService;
import com.lanf.tasks.model.TaskScheduledEmail;
import com.lanf.tasks.service.DatabaseExecutorService;
import com.lanf.tasks.service.EmailService;
import com.lanf.tasks.service.StoredProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import com.lanf.common.exception.CacheExpiredException;
import com.lanf.common.result.ResultCodeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;


import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);


    @Autowired
    private StoredProcedureService storedProcedureServiceImpl;

    @Autowired
    private DatabaseExecutorService databaseExecutorServiceImpl;

    @Autowired
    private SysLogService sysTaskLogServiceImpl;

    @Autowired
    private ApplicationProperty applicationProperty;

    @Autowired
    private JavaMailSender mailSender;



    /**
     * 发送包含附件的邮箱
     * @param RecipientEmail  收件人
     * @param ccEmail  抄送人
     * @param subject  邮箱主题
     * @param Body  邮箱内容
     * @param filePath  附件路径
     */
    @Override
    public void sendMail(String RecipientEmail, String ccEmail, String subject, String Body, String filePath) throws CacheExpiredException{
        logger.info("发送邮件参数:\nfrom:{},\nRecipientEmail:{},\nccEmail:{},\nsubject:{},\nBody:{},\nfilePath:{}"
                , applicationProperty.getFormEmail(),RecipientEmail,ccEmail,subject,(Body.length() > 1000 ? Body.substring(0, 1000) : Body),filePath);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);

            // 设置收件人
            if (StringUtil.isNotNullOrEmptyString(RecipientEmail)) {
                String[] recipientEmails = RecipientEmail.split(";");

                // 使用流去除每个字符串的前后空格
                recipientEmails = Arrays.stream(recipientEmails)
                        .map(String::trim)
                        .toArray(String[]::new);

                message.setTo(recipientEmails);
            }else {
                //没有收件人直接结束进程
                throw new CacheExpiredException(ResultCodeEnum.NO_RECIPIENT_EMAIL,"EmailServiceImpl");
            }

            // 设置抄送人
            if (StringUtil.isNotNullOrEmptyString(ccEmail)) {
                String[] ccEmails = ccEmail.split(";");
                message.setCc(ccEmails);
            }

            //设置邮件主题
            if (StringUtil.isNotNullOrEmptyString(subject)) {
                message.setSubject(subject);
            }else{
                //没有设置邮件主题, 直接结束进程
                throw new CacheExpiredException(ResultCodeEnum.EMAIL_SUBJECT_NOT_SET,"EmailServiceImpl");
            }

            if (StringUtil.isNotNullOrEmptyString(Body)) {
                message.setText(Body, true); // 第二个参数为 true 表示内容为 HTML 格式
            }else {
                //没有设置邮件内容, 直接结束进程
                throw new CacheExpiredException(ResultCodeEnum.EMAIL_CONTENT_EMPTY,"EmailServiceImpl");
            }

            message.setFrom(applicationProperty.getFormEmail(), "系统管理员");

            if (StringUtil.isNotNullOrEmptyString(filePath)){
                // 获取到文件
                FileSystemResource file = new FileSystemResource(new File(filePath));
                if (file.exists() && file.isFile()) {
                    String fileName = file.getFilename();
                    message.addAttachment(fileName, file);
                } else {
                    throw new CacheExpiredException(100013,"任务"+subject+": 该路径下未找到文件或不是文件, "+filePath,"EmailServiceImpl");
                }
            }

            // 发送邮件
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            logger.error("任务{},邮件发送失败,错误原因: {}", subject, e.getMessage(), e);
            throw new CacheExpiredException(110000,"邮件发送失败,错误原因: "+e.getMessage(),"EmailServiceImpl");
        }
    }


    //邮件发送, @Retryable注解用于指定异常触发重试，设置最大重试次数为3次，每次重试间隔2分钟（120000毫秒）。
    @Override
    @Retryable(include = {CacheExpiredException.class, Exception.class}, maxAttempts = 5, backoff = @Backoff(delay = 120000))
    public void sendMail(TaskScheduledEmail task) {
        if (task == null) {
            throw new CacheExpiredException(ResultCodeEnum.PARA_NOT_NULL,"DynamicTaskScheduler");
        }

        String bodyContent = task.getBody();
        try {
            // 动态选择数据源
            DynamicDataSource.setDataSource(task.getDatabaseName().trim());

            logger.info("任务{}({}): 调用数据源 {} 成功", task.getTaskName(),task.getId(), task.getDatabaseName());

            int bodyType = task.getBodyType()%10;
            //BodyType为1时, 调用储存过程
            if (bodyType == 1) {
                if (StringUtil.isNotNullOrEmptyString(bodyContent)){
                    //执行储存过程, 并返回结果集
                    bodyContent = storedProcedureServiceImpl.executeStoredProcedure(bodyContent);
                }

            }
            //BodyType为2时, 调用SQL语句查询
            else if (bodyType == 2 && task.getBody().toLowerCase().startsWith("select")) {
                if (StringUtil.isNotNullOrEmptyString(bodyContent)){
                    //执行储存过程, 并返回结果集
                    bodyContent = databaseExecutorServiceImpl.executeQuery(bodyContent);
                }

            }
            //查询结果是否为空
            if (StringUtil.isNullOrEmptyString(bodyContent)){

                throw new CacheExpiredException(ResultCodeEnum.STORED_PROCEDURE_NOT_FOUND_OR_EMPTY_RESULT,"DynamicTaskScheduler");
            }

            String filePath = "";
            //判断是否需要附件
            if ( StringUtil.isNotNullOrEmptyString(task.getAttachment()) ){
                int attachmentType = task.getAttachmentType()%10;
                if ( attachmentType == 0 ){
                    //调用储存过程
                    filePath = storedProcedureServiceImpl
                            .exportStoredProcedureToExcel(task.getAttachment(),task.getSubject()
                                    ,applicationProperty.getEmailExportAddressToExcel());

                }else if (attachmentType == 1 && task.getAttachment().toLowerCase().startsWith("select")) {
                    //调用SQL语句
                    filePath = databaseExecutorServiceImpl
                            .executeQueryToExcel(task.getAttachment(),task.getSubject()
                                    ,applicationProperty.getEmailExportAddressToExcel());
                }
                logger.info("任务{}({}): filePath文件路径为{}", task.getTaskName(),task.getId(), filePath);
            }

            // 清除数据源
            DynamicDataSource.clearDataSource();
            sendMail(task.getRecipientEmail(), task.getCcEmail(), task.getSubject(), bodyContent, filePath);

            logger.info("任务{}({}): 已完成邮件发送任务。", task.getTaskName(),task.getId());
            sysTaskLogServiceImpl.addLog(Constant.MODULE_TASK, Constant.MODULE_TASK_SENDEMAIL
                    ,"DynamicTaskScheduler"
                    ,"任务"+task.getTaskName()+"("+task.getId()+") : 已完成邮件发送任务"
                    , "success","admin");

        } catch (CacheExpiredException e){
            // 清除数据源
            DynamicDataSource.clearDataSource();

            sysTaskLogServiceImpl.addLog(Constant.MODULE_TASK, Constant.MODULE_TASK_SENDEMAIL
                    ,e.getClassName()
                    ,"任务"+task.getTaskName()+"("+task.getId()+") : 发送邮件任务执行失败,"+e.getMessage()
                    , "error","admin");
            logger.error("任务{}({}): 发送邮件任务执行失败, {}", task.getTaskName(),task.getId(), e.getMessage(), e);

            throw e;  // 继续抛出异常以触发重试
        } catch ( Exception e ){
            // 清除数据源
            DynamicDataSource.clearDataSource();
            sysTaskLogServiceImpl.addLog(Constant.MODULE_TASK, Constant.MODULE_TASK_SENDEMAIL
                    ,"DynamicTaskScheduler"
                    ,"任务"+task.getTaskName()+"("+task.getId()+") : 发送邮件任务执行失败,"+e.getMessage()
                    , "error","admin");
            logger.error("任务{}({}): 发送邮件任务执行失败, {}", task.getTaskName(),task.getId(), e.getMessage(), e);

            throw e;  // 继续抛出异常以触发重试
        }
    }

    @Recover
    public void recover(CacheExpiredException e, TaskScheduledEmail task) {
        logger.error("任务{}({}): 邮件发送失败重试3次后仍未成功，错误信息: {}", task.getTaskName(), task.getId(), e.getMessage(), e);
        // 记录最终失败日志
        sysTaskLogServiceImpl.addLog(Constant.MODULE_TASK, Constant.MODULE_TASK_SENDEMAIL, e.getClassName(),
                "任务" + task.getTaskName() + "(" + task.getId() + ") : 发送邮件任务最终失败," + e.getMessage(), "error", "admin");
    }

    @Recover
    public void recover(Exception e, TaskScheduledEmail task) {
        logger.error("任务{}({}): 邮件发送失败重试3次后仍未成功，错误信息: {}", task.getTaskName(), task.getId(), e.getMessage(), e);
        // 记录最终失败日志
        sysTaskLogServiceImpl.addLog(Constant.MODULE_TASK, Constant.MODULE_TASK_SENDEMAIL, "DynamicTaskScheduler",
                "任务" + task.getTaskName() + "(" + task.getId() + ") : 发送邮件任务最终失败," + e.getMessage(), "error", "admin");
    }
}
