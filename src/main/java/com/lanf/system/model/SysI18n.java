package com.lanf.system.model;

import com.lanf.model.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
/**
* @author tanlingfei
* @version 1.0
* @description 国际化语言 po类
* @date 2023-10-31 13:47:32
*/
@Data
@ApiModel(description = "国际化语言")
@TableName("sys_i18n")
public class SysI18n extends BaseEntity {


private static final long serialVersionUID = 1L;

        @ApiModelProperty(value = "键名称")
        @TableField("name")
        private String name;
        @ApiModelProperty(value = "键值")
        @TableField("val")
        private String val;
        @ApiModelProperty(value = "类型")
        @TableField("type")
        private String type;
        @TableField(exist = false)
        private String typeName;
}
