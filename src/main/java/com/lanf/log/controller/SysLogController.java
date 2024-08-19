package com.lanf.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.common.result.Result;
import com.lanf.log.model.SysLog;
import com.lanf.log.vo.SysLogQueryVo;
import com.lanf.log.service.SysLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
* @author hy.qiu
* @version 1.0
* @description 定时任务日志
* @date 2024-08-11 17:02:27
*/
@Api(tags = "定时任务日志")
@RestController
@RequestMapping("/system/sysLog")
public class SysLogController {
    @Autowired
    private SysLogService sysLogService;

    @PreAuthorize("hasAuthority('bnt.sysLog.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true)
    @PathVariable Long page,
    @ApiParam(name = "limit", value = "每页记录数", required = true)
    @PathVariable Long limit,
    @ApiParam(name = "sysLogQueryVo", value = "查询对象", required = false)
                        SysLogQueryVo sysLogQueryVo) {
        Page<SysLog> pageParam = new Page<>(page, limit);
        IPage<SysLog> pageModel = sysLogService.selectPage(pageParam, sysLogQueryVo);
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.sysLog.list')")
    @ApiOperation(value = "查询列表")
    @GetMapping("/list")
    public Result list(@ApiParam(name = "sysLogQueryVo", value = "查询对象", required = false)
                       SysLogQueryVo sysLogQueryVo) {
          List<SysLog> list = sysLogService.queryList(sysLogQueryVo);

        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysLog.list')")
    @ApiOperation(value = "所有定时任务日志列表")
    @GetMapping("findAll")
    public Result findAllSysLog() {
    //调用service的方法实现查询所有的操作
        List<SysLog> list = sysLogService.list(null);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.sysLog.list')")
    @ApiOperation(value = "获取定时任务日志")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable String id) {
        SysLog sysLog = sysLogService.getById(id);
        return Result.ok(sysLog);
    }

    @PreAuthorize("hasAuthority('bnt.sysLog.list')")
    @ApiOperation(value = "获取定时任务日志集合")
    @PostMapping("/getByIds")
    public Result getByIds(@RequestBody List<String> idList) {
        List<SysLog> list = sysLogService.getByIds(idList);
        return Result.ok(list);
     }



}
