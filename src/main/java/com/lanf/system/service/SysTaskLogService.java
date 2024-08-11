package com.lanf.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.system.model.SysTaskLog;
import com.lanf.system.vo.SysTaskLogQueryVo;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 Service接口
* @date 2024-08-11 17:02:27
*/
public interface SysTaskLogService extends IService<SysTaskLog> {
    IPage<SysTaskLog> selectPage(Page<SysTaskLog> pageParam, SysTaskLogQueryVo queryVo);
    List<SysTaskLog> queryList(SysTaskLogQueryVo queryVo);

    public SysTaskLog getById(String id);
    public List<SysTaskLog> getByIds(List<String> ids);
}
