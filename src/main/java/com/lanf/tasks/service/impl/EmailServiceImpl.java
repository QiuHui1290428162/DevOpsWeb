package com.lanf.tasks.service.impl;

import com.lanf.common.config.DynamicDataSource;
import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.common.info.ApplicationProperty;
import com.lanf.common.utils.EmailUtil;
import com.lanf.common.utils.StringUtil;
import com.lanf.common.utils.ValidUtil;
import com.lanf.log.annotation.SystemLog;
import com.lanf.log.service.SysLogService;
import com.lanf.tasks.model.TaskScheduledEmail;
import com.lanf.tasks.service.DatabaseExecutorService;
import com.lanf.tasks.service.EmailService;
import com.lanf.tasks.service.StoredProcedureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.lanf.common.result.ResultCodeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.FileSystemResource;


import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LogManager.getLogger(EmailServiceImpl.class);


    @Autowired
    private StoredProcedureService storedProcedureServiceImpl;

    @Autowired
    private DatabaseExecutorService databaseExecutorServiceImpl;

    @Autowired
    private SysLogService sysLogServiceImpl;

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
    public void sendMail(String RecipientEmail, String ccEmail, String subject, String Body, String filePath) throws GlobalExpiredException {
//        logger.info("发送邮件参数:\nfrom:{},\nRecipientEmail:{},\nccEmail:{},\nsubject:{},\nBody:{},\nfilePath:{}"
//                , applicationProperty.getFormEmail(),RecipientEmail,ccEmail,subject,(Body.length() > 1000 ? Body.substring(0, 1000) : Body),filePath);

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);

            // 设置收件人
            String[] recipientEmails = EmailUtil.trimRecipientEmails(RecipientEmail);
            if (recipientEmails != null) {
                message.setTo(recipientEmails);
            }else {
                //没有收件人直接结束进程
                throw new GlobalExpiredException(ResultCodeEnum.NO_RECIPIENT_EMAIL);
            }

            // 设置抄送人
            if (StringUtil.isNotNullOrEmptyString(ccEmail)) {
                String[] ccEmails = EmailUtil.trimRecipientEmails(ccEmail);
                if (ccEmails != null) {
                    message.setCc(ccEmails);
                }else {
                    //没有收件人直接结束进程
                    throw new GlobalExpiredException(ResultCodeEnum.NO_CC_EMAIL);
                }
            }

            //设置邮件主题
            if (StringUtil.isNotNullOrEmptyString(subject)) {
                message.setSubject(subject);
            }else{
                //没有设置邮件主题, 直接结束进程
                throw new GlobalExpiredException(ResultCodeEnum.EMAIL_SUBJECT_NOT_SET);
            }

            if (StringUtil.isNotNullOrEmptyString(Body)) {
                message.setText(Body, true); // 第二个参数为 true 表示内容为 HTML 格式
            }else {
                //没有设置邮件内容, 直接结束进程
                throw new GlobalExpiredException(ResultCodeEnum.EMAIL_CONTENT_EMPTY);
            }

            message.setFrom(applicationProperty.getFormEmail(), "系统管理员");

            if (StringUtil.isNotNullOrEmptyString(filePath)){
                // 获取到文件
                FileSystemResource file = new FileSystemResource(new File(filePath));
                if (file.exists() && file.isFile()) {
                    String fileName = file.getFilename();
                    message.addAttachment(fileName, file);
                } else {
                    throw new GlobalExpiredException(1002002,"邮件("+subject+"): 该路径下未找到文件, "+filePath);
                }
            }

            // 发送邮件
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new GlobalExpiredException(110000,"邮件发送失败,错误原因: "+e.getMessage());
        }
    }


    //邮件发送,
    @Override
    @SystemLog(module = "定时任务", functionName = "发送邮件", description = "发送邮件成功", clearDataSource = true)
    public void sendMail(TaskScheduledEmail task) {
        // 检查 Cron 表达式是否合法
        if (!task.getScheduledTime().equals("0 0 0 0 0 0")&&!ValidUtil.isValidCronExpression(task.getScheduledTime())) {
            // 如果不合法直接抛异常
            throw new GlobalExpiredException(ResultCodeEnum.INVALID_CRON_EXPRESSION);
        }

        String bodyContent = "";
        if (task == null) {
            throw new GlobalExpiredException(ResultCodeEnum.PARA_NOT_NULL);
        }
        //查询邮件内容是否为空
        if (StringUtil.isNotNullOrEmptyString(task.getBody())){
            bodyContent = task.getBody();
        }else{
            throw new GlobalExpiredException(ResultCodeEnum.EMAIL_CONTENT_EMPTY);
        }

        {
            // 动态选择数据源
            DynamicDataSource.setDataSource(task.getDatabaseName().trim());

            int bodyType = task.getBodyType()%10;
            //BodyType为1时, 调用储存过程
            if (bodyType == 1) {
                //执行储存过程, 并返回结果集
                bodyContent = storedProcedureServiceImpl.executeStoredProcedure(bodyContent);
            }
            //BodyType为2时, 调用SQL语句查询
            else if (bodyType == 2 && task.getBody().toLowerCase().startsWith("select")) {
                //执行储存过程, 并返回结果集
                bodyContent = databaseExecutorServiceImpl.executeQuery(bodyContent);

            }
            //查询结果是否为空
            if (StringUtil.isNullOrEmptyString(bodyContent)){

                throw new GlobalExpiredException(ResultCodeEnum.STORED_PROCEDURE_NOT_FOUND_OR_EMPTY_RESULT);
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
            }

            // 清除数据源
            DynamicDataSource.clearDataSource();
            sendMail(task.getRecipientEmail(), task.getCcEmail(), task.getSubject(), bodyContent, filePath);

        }
    }

}
