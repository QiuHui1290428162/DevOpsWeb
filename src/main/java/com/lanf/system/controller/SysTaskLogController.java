package com.lanf.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.common.result.Result;
import com.lanf.system.model.SysTaskLog;
import com.lanf.system.vo.SysTaskLogQueryVo;
import com.lanf.system.service.SysTaskLogService;
import com.lanf.log.type.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.lanf.log.annotation.Log;
import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志
* @date 2024-08-11 17:02:27
*/
@Api(tags = "定时任务日志")
@RestController
@RequestMapping("/system/sysTaskLog")
public class SysTaskLogController {
    @Autowired
    private SysTaskLogService sysTaskLogService;

    @PreAuthorize("hasAuthority('bnt.sysTaskLog.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true)
    @PathVariable Long page,
    @ApiParam(name = "limit", value = "每页记录数", required = true)
    @PathVariable Long limit,
    @ApiParam(name = "sysTaskLogQueryVo", value = "查询对象", required = false)
    SysTaskLogQueryVo sysTaskLogQueryVo) {
        Page<SysTaskLog> pageParam = new Page<>(page, limit);
        IPage<SysTaskLog> pageModel = sysTaskLogService.selectPage(pageParam, sysTaskLogQueryVo);
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.sysTaskLog.list')")
    @ApiOperation(value = "查询列表")
    @GetMapping("/list")
    public Result list(@ApiParam(name = "sysTaskLogQueryVo", value = "查询对象", required = false)
        SysTaskLogQueryVo sysTaskLogQueryVo) {
          List<SysTaskLog> list = sysTaskLogService.queryList(sysTaskLogQueryVo);

        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysTaskLog.list')")
    @ApiOperation(value = "所有定时任务日志列表")
    @GetMapping("findAll")
    public Result findAllSysTaskLog() {
    //调用service的方法实现查询所有的操作
        List<SysTaskLog> list = sysTaskLogService.list(null);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysTaskLog.list')")
    @ApiOperation(value = "获取定时任务日志")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable String id) {
        SysTaskLog sysTaskLog = sysTaskLogService.getById(id);
        return Result.ok(sysTaskLog);
    }

    @PreAuthorize("hasAuthority('bnt.sysTaskLog.list')")
    @ApiOperation(value = "获取定时任务日志集合")
    @PostMapping("/getByIds")
    public Result getByIds(@RequestBody List<String> idList) {
        List<SysTaskLog> list = sysTaskLogService.getByIds(idList);
        return Result.ok(list);
     }



}
