package com.lanf.task.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.task.model.TaskScheduledEmail;
import com.lanf.task.vo.TaskScheduledEmailQueryVo;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 Service接口
* @date 2024-08-07 12:50:30
*/
public interface TaskScheduledEmailService extends IService<TaskScheduledEmail> {
    IPage<TaskScheduledEmail> selectPage(Page<TaskScheduledEmail> pageParam, TaskScheduledEmailQueryVo queryVo);
    List<TaskScheduledEmail> queryList(TaskScheduledEmailQueryVo queryVo);
    public boolean save(TaskScheduledEmail taskScheduledEmail);
    public boolean updateById(TaskScheduledEmail taskScheduledEmail);
    public TaskScheduledEmail getById(String id);
    public List<TaskScheduledEmail> getByIds(List<String> ids);
}
