package com.lanf.system.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tanlingfei
 * @version 1.0
 * @description 国际化语言 导出类
 * @date 2023-10-31 13:47:32
 */
@Data
@ApiModel(description = "国际化语言")
public class SysI18nExportVo {
    @ApiModelProperty(value = "键名称")
    @ExcelProperty("键名称")
    private String name;
    @ApiModelProperty(value = "键值")
    @ExcelProperty("键值")
    private String val;
    @ApiModelProperty(value = "类型")
    @ExcelProperty("类型")
    private String type;
    @ExcelProperty("类型")
    private String typeName;
}
