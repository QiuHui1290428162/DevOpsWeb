package com.lanf.log.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.common.exception.GlobalExpiredException;
import com.lanf.common.result.ResultCodeEnum;
import com.lanf.log.mapper.SysLogMapper;
import com.lanf.log.model.SysLog;
import com.lanf.log.vo.SysLogQueryVo;
import com.lanf.log.service.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 Service实现类
* @date 2024-08-11 17:02:27
*/
@Transactional
@Service
public class SysLogServiceImpl extends ServiceImpl
<SysLogMapper, SysLog> implements SysLogService {
    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public IPage<SysLog> selectPage(Page<SysLog> pageParam, SysLogQueryVo sysLogQueryVo) {
        //QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        //return sysLogMapper.selectPage(pageParam,queryWrapper);
        return sysLogMapper.selectPage(pageParam,sysLogQueryVo);
    }

    @Override
    public List<SysLog> queryList(SysLogQueryVo sysLogQueryVo){
     List<SysLog> result = sysLogMapper.queryList(sysLogQueryVo);
     return result;
    }
    @Override
    public SysLog getById(String id){
         SysLog sysLog = sysLogMapper.selectById(id);
         return sysLog;
    }
   @Override
   public List<SysLog> getByIds(List<String> ids) {
      List<SysLog> list = this.sysLogMapper.selectBatchIds(ids);
      return list;
   }

    @Override
    public boolean addLog(String module, String FunctionName, String ClassName, String OperationDescription, String Result, String UserName) {
        int row = sysLogMapper.addLog(module,FunctionName,ClassName,OperationDescription,Result,UserName);
        if(row <= 0){
            throw new GlobalExpiredException(ResultCodeEnum.UPDATE_ERROR);
        }
        return row>0;
    }


}
