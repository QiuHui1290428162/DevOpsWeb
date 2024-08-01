package com.lanf.system.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author tanlingfei
 * @version 1.0
 * @description 国际化语言 vo类
 * @date 2023-10-31 13:47:32
 */
@Data
public class SysI18nQueryVo {
    private String name;
    private String val;
    private String type;
    private String typeName;
    private Date createTimeBegin;
    private Date createTimeEnd;
    private Date updateTimeBegin;
    private Date updateTimeEnd;
}

