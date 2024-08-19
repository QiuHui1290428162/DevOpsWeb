package com.lanf.log.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lanf.log.model.SysLog;
import com.lanf.log.vo.SysLogQueryVo;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志 Service接口
* @date 2024-08-11 17:02:27
*/
public interface SysLogService extends IService<SysLog> {
    IPage<SysLog> selectPage(Page<SysLog> pageParam, SysLogQueryVo queryVo);
    List<SysLog> queryList(SysLogQueryVo queryVo);

    public SysLog getById(String id);
    public List<SysLog> getByIds(List<String> ids);

    public boolean addLog(String module, String FunctionName, String ClassName
            , String OperationDescription, String Result, String UserName);
}
