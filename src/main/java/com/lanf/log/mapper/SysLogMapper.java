package com.lanf.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.log.model.SysLog;
import com.lanf.log.vo.SysLogQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 Mapper层
* @date 2024-08-11 17:02:27
*/
@Repository
@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
    IPage<SysLog> selectPage(Page<SysLog> page, @Param("vo") SysLogQueryVo sysLogQueryVo);
    List<SysLog> queryList(@Param("vo") SysLogQueryVo sysLogQueryVo);

    int addLog(String module, String FunctionName, String ClassName
            , String OperationDescription, String Result, String UserName);
}