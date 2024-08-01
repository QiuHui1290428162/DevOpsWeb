package com.lanf.business.model;

import com.lanf.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
* @author code
* @version 1.0
* @description 员工信息 po类
* @date 2023-11-29 21:17:16
*/
@Data
@ApiModel(description = "员工信息")
@TableName("tb_employee")
public class TbEmployee extends BaseEntity {
        private static final long serialVersionUID = 1L;
        @ApiModelProperty(value = "姓名")
        @TableField("realname")
        private String realname;
        @ApiModelProperty(value = "年龄")
        @TableField("age")
        private Integer age;
        @ApiModelProperty(value = "出生日期")
        @TableField("born_date")
        private java.util.Date bornDate;
}
