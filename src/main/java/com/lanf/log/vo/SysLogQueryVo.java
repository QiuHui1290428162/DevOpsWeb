package com.lanf.log.vo;

import lombok.Data;
import java.util.Date;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 vo类
* @date 2024-08-11 17:02:27
*/
@Data
public class SysLogQueryVo {
       private String module;
       private String functionName;
       private String className;
       private String operationDescription;
       private String result;
       private String userName;
       private Date createTimeBegin;
       private Date createTimeEnd;
}

