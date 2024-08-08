package com.lanf.task.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.task.model.TaskScheduledEmail;
import com.lanf.task.vo.TaskScheduledEmailQueryVo;
import com.lanf.task.mapper.TaskScheduledEmailMapper;
import com.lanf.task.service.TaskScheduledEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.lanf.common.result.ResultCodeEnum;
import com.lanf.system.exception.LanfException;
/**
* @author hy.qiu
* @version 1.0
* @description 定时发送邮件任务参数表 Service实现类
* @date 2024-08-07 12:50:30
*/
@Transactional
@Service
public class TaskScheduledEmailServiceImpl extends ServiceImpl
<TaskScheduledEmailMapper, TaskScheduledEmail> implements TaskScheduledEmailService {
    @Autowired
    private TaskScheduledEmailMapper taskScheduledEmailMapper;

    @Override
    public IPage<TaskScheduledEmail> selectPage(Page<TaskScheduledEmail> pageParam, TaskScheduledEmailQueryVo taskScheduledEmailQueryVo) {
        //QueryWrapper<TaskScheduledEmail> queryWrapper = new QueryWrapper<>();
        //return taskScheduledEmailMapper.selectPage(pageParam,queryWrapper);
        return taskScheduledEmailMapper.selectPage(pageParam,taskScheduledEmailQueryVo);
    }

    @Override
    public List<TaskScheduledEmail> queryList(TaskScheduledEmailQueryVo taskScheduledEmailQueryVo){
     List<TaskScheduledEmail> result = taskScheduledEmailMapper.queryList(taskScheduledEmailQueryVo);
     return result;
    }


    @Override
    public boolean save(TaskScheduledEmail taskScheduledEmail){
        int result = this.taskScheduledEmailMapper.insert(taskScheduledEmail);
        return result>0;
    }
    @Override
    public boolean updateById(TaskScheduledEmail taskScheduledEmail){
        int row = this.taskScheduledEmailMapper.updateById(taskScheduledEmail);
        if(row <= 0){
            throw new LanfException(ResultCodeEnum.UPDATE_ERROR);
         }
        return row>0;
    }
    @Override
    public TaskScheduledEmail getById(String id){
         TaskScheduledEmail taskScheduledEmail = taskScheduledEmailMapper.selectById(id);
         return taskScheduledEmail;
    }
   @Override
   public List<TaskScheduledEmail> getByIds(List<String> ids) {
      List<TaskScheduledEmail> list = this.taskScheduledEmailMapper.selectBatchIds(ids);
      return list;
   }
}
