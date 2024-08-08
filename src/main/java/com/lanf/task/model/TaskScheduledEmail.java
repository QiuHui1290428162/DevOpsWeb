package com.lanf.task.model;

import com.lanf.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 po类
* @date 2024-08-07 12:50:30
*/
@Data
@ApiModel(description = "定时发送邮件任务参数表")
@TableName("task_scheduled_email")
public class TaskScheduledEmail extends BaseEntity {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(value = "任务名称")
        @TableField("task_name")
        private String taskName;
        @ApiModelProperty(value = "收件人邮箱号")
        @TableField("recipient_email")
        private String recipientEmail;
        @ApiModelProperty(value = "抄送人邮箱号")
        @TableField("cc_email")
        private String ccEmail;
        @ApiModelProperty(value = "邮件主题")
        @TableField("subject")
        private String subject;
        @ApiModelProperty(value = "邮件内容")
        @TableField("body")
        private String body;
        @ApiModelProperty(value = "邮件内容类型（默认为0,0:文本/HTML, 1:存储过程, 2:数据表/视图）")
        @TableField("body_type")
        private Integer bodyType;
        @ApiModelProperty(value = "附件")
        @TableField("attachment")
        private String attachment;
        @ApiModelProperty(value = "附件类型（默认为0,0:存储过程,1:数据表/视图）")
        @TableField("attachment_type")
        private Integer attachmentType;
        @ApiModelProperty(value = "数据库名称")
        @TableField("database_name")
        private String databaseName;
        @ApiModelProperty(value = "定时发送时间")
        @TableField("scheduled_time")
        private String scheduledTime;
        @ApiModelProperty(value = "备注")
        @TableField("remarks")
        private String remarks;
        @ApiModelProperty(value = "任务状态(1:启动, 0:停止)")
        @TableField("status")
        private Integer status;
}
