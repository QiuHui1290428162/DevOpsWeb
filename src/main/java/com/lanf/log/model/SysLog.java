package com.lanf.log.model;

import com.lanf.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 po类
* @date 2024-08-11 17:02:27
*/
@Data
@ApiModel(description = "系统日志")
@TableName("sys_log")
public class SysLog extends BaseEntity {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(value = "模块名称")
        @TableField("module")
        private String module;
        @ApiModelProperty(value = "功能名称")
        @TableField("function_name")
        private String functionName;
        @ApiModelProperty(value = "类名称")
        @TableField("class_name")
        private String className;
        @ApiModelProperty(value = "操作描述")
        @TableField("operation_description")
        private String operationDescription;
        @ApiModelProperty(value = "操作结果")
        @TableField("result")
        private String result;
        @ApiModelProperty(value = "用户名")
        @TableField("user_name")
        private String userName;
}
