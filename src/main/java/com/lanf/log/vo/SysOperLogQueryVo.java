package com.lanf.log.vo;

import lombok.Data;

@Data
public class SysOperLogQueryVo {

    private String title;
    private String operName;

    private String createTimeBegin;
    private String createTimeEnd;

}

