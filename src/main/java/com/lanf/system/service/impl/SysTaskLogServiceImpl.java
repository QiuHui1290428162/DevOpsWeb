package com.lanf.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanf.system.model.SysTaskLog;
import com.lanf.system.vo.SysTaskLogQueryVo;
import com.lanf.system.mapper.SysTaskLogMapper;
import com.lanf.system.service.SysTaskLogService;
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
public class SysTaskLogServiceImpl extends ServiceImpl
<SysTaskLogMapper, SysTaskLog> implements SysTaskLogService {
    @Autowired
    private SysTaskLogMapper sysTaskLogMapper;

    @Override
    public IPage<SysTaskLog> selectPage(Page<SysTaskLog> pageParam, SysTaskLogQueryVo sysTaskLogQueryVo) {
        //QueryWrapper<SysTaskLog> queryWrapper = new QueryWrapper<>();
        //return sysTaskLogMapper.selectPage(pageParam,queryWrapper);
        return sysTaskLogMapper.selectPage(pageParam,sysTaskLogQueryVo);
    }

    @Override
    public List<SysTaskLog> queryList(SysTaskLogQueryVo sysTaskLogQueryVo){
     List<SysTaskLog> result = sysTaskLogMapper.queryList(sysTaskLogQueryVo);
     return result;
    }
    @Override
    public SysTaskLog getById(String id){
         SysTaskLog sysTaskLog = sysTaskLogMapper.selectById(id);
         return sysTaskLog;
    }
   @Override
   public List<SysTaskLog> getByIds(List<String> ids) {
      List<SysTaskLog> list = this.sysTaskLogMapper.selectBatchIds(ids);
      return list;
   }
}
