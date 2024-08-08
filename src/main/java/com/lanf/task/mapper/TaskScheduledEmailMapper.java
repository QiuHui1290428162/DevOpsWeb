package com.lanf.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.task.model.TaskScheduledEmail;
import com.lanf.task.vo.TaskScheduledEmailQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 Mapper层
* @date 2024-08-07 12:50:30
*/
@Repository
@Mapper
public interface TaskScheduledEmailMapper extends BaseMapper<TaskScheduledEmail> {
    IPage<TaskScheduledEmail> selectPage(Page<TaskScheduledEmail> page, @Param("vo") TaskScheduledEmailQueryVo taskScheduledEmailQueryVo);
    List<TaskScheduledEmail> queryList(@Param("vo") TaskScheduledEmailQueryVo taskScheduledEmailQueryVo);
}