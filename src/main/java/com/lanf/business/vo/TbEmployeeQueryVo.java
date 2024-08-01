package com.lanf.business.vo;

import lombok.Data;
import java.util.Date;
/**
* @author code
* @version 1.0
* @description 员工信息 vo类
* @date 2023-11-29 21:17:16
*/
@Data
public class TbEmployeeQueryVo {
       private String realname;
       private Integer age;
       private java.util.Date bornDate;
       private Date createTimeBegin;
       private Date createTimeEnd;
       private Date updateTimeBegin;
       private Date updateTimeEnd;
}

