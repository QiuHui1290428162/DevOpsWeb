package com.lanf.tasks.vo;

import lombok.Data;
import java.util.Date;
/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 vo类
* @date 2024-08-07 12:50:30
*/
@Data
public class TaskScheduledEmailQueryVo {
       private String taskName;
       private String recipientEmail;
       private String ccEmail;
       private String subject;
       private String body;
       private Integer bodyType;
       private String attachment;
       private Integer attachmentType;
       private String databaseName;
       private String scheduledTime;
       private String remarks;
       private Integer status;
       private Date createTimeBegin;
       private Date createTimeEnd;
       private Date updateTimeBegin;
       private Date updateTimeEnd;
}

