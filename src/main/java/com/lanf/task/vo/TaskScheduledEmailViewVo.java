package com.lanf.task.vo;

import lombok.Data;

import java.util.Date;

/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 视图vo类
* @date 2024-08-07 12:50:30
*/
@Data
public class TaskScheduledEmailViewVo {

       private String id;
       private String taskName;
       private String recipientEmail;
       private String ccEmail;
       private String subject;
       private String body;
       private String bodyType;
       private String attachment;
       private String attachmentType;
       private String databaseName;
       private String scheduledTime;
       private String remarks;
       private String status;
       private Date createTime;
       private Date updateTime;
}

