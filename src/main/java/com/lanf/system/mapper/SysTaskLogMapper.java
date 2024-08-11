package com.lanf.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.system.model.SysTaskLog;
import com.lanf.system.vo.SysTaskLogQueryVo;
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
public interface SysTaskLogMapper extends BaseMapper<SysTaskLog> {
    IPage<SysTaskLog> selectPage(Page<SysTaskLog> page, @Param("vo") SysTaskLogQueryVo sysTaskLogQueryVo);
    List<SysTaskLog> queryList(@Param("vo") SysTaskLogQueryVo sysTaskLogQueryVo);
}