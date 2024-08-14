package com.lanf.tasks.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lanf.common.result.Result;
import com.lanf.tasks.model.TaskScheduledEmail;
import com.lanf.tasks.service.EmailService;
import com.lanf.tasks.vo.TaskScheduledEmailQueryVo;
import com.lanf.tasks.service.TaskScheduledEmailService;
import com.lanf.log.type.BusinessType;
import com.lanf.tasks.vo.TaskScheduledEmailViewVo;
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
* @description 定时发送邮件任务参数表
* @date 2024-08-07 12:50:30
*/
@Api(tags = "定时发送邮件任务参数表")
@RestController
@RequestMapping("/tasks/taskScheduledEmail")
public class TaskScheduledEmailController {
    @Autowired
    private TaskScheduledEmailService taskScheduledEmailService;

    @Autowired
    private EmailService emailServiceImpl;

    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.list')")
    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public Result index(@ApiParam(name = "page", value = "当前页码", required = true)
    @PathVariable Long page,
    @ApiParam(name = "limit", value = "每页记录数", required = true)
    @PathVariable Long limit,
    @ApiParam(name = "taskScheduledEmailQueryVo", value = "查询对象", required = false)
    TaskScheduledEmailQueryVo taskScheduledEmailQueryVo) {
        Page<TaskScheduledEmail> pageParam = new Page<>(page, limit);
        System.out.println(page);
        System.out.println(page);
        IPage<TaskScheduledEmailViewVo> pageModel = taskScheduledEmailService.selectPage(pageParam, taskScheduledEmailQueryVo);
        for ( TaskScheduledEmailViewVo vo : pageModel.getRecords() ) {
            System.out.println(vo.toString());
        }
        return Result.ok(pageModel);
    }

    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.list')")
    @ApiOperation(value = "查询列表")
    @GetMapping("/list")
    public Result list(@ApiParam(name = "taskScheduledEmailQueryVo", value = "查询对象", required = false)
        TaskScheduledEmailQueryVo taskScheduledEmailQueryVo) {
          List<TaskScheduledEmail> list = taskScheduledEmailService.queryList(taskScheduledEmailQueryVo);

        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.list')")
    @ApiOperation(value = "所有定时发送邮件任务参数表列表")
    @GetMapping("findAll")
    public Result findAllTaskScheduledEmail() {
    //调用service的方法实现查询所有的操作
        List<TaskScheduledEmail> list = taskScheduledEmailService.list(null);
        return Result.ok(list);
    }

    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.list')")
    @ApiOperation(value = "获取定时发送邮件任务参数表")
    @GetMapping("/get/{id}")
    public Result get(@PathVariable String id) {
        TaskScheduledEmail taskScheduledEmail = taskScheduledEmailService.getById(id);
        return Result.ok(taskScheduledEmail);
    }

    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.list')")
    @ApiOperation(value = "获取定时发送邮件任务参数表集合")
    @PostMapping("/getByIds")
    public Result getByIds(@RequestBody List<String> idList) {
        List<TaskScheduledEmail> list = taskScheduledEmailService.getByIds(idList);
        return Result.ok(list);
     }

    @Log(title = "定时发送邮件任务参数表", businessType = BusinessType.INSERT)
    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.add')")
    @ApiOperation(value = "保存定时发送邮件任务参数表")
    @PostMapping("/save")
    public Result save(@RequestBody TaskScheduledEmail taskScheduledEmail) {
        taskScheduledEmailService.save(taskScheduledEmail);
        return Result.ok();
    }

    @Log(title = "定时发送邮件任务参数表", businessType = BusinessType.UPDATE)
    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.update')")
    @ApiOperation(value = "更新定时发送邮件任务参数表")
    @PutMapping("/update")
    public Result updateById(@RequestBody TaskScheduledEmail taskScheduledEmail) {
        taskScheduledEmailService.updateById(taskScheduledEmail);
        return Result.ok();
    }

    @Log(title = "定时发送邮件任务参数表", businessType = BusinessType.DELETE)
    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.remove')")
    @ApiOperation(value = "根据id列表删除")
    @DeleteMapping("/batchRemove")
    public Result batchRemove(@RequestBody List<String> idList) {
        boolean b = taskScheduledEmailService.removeByIds(idList);
        if (b) {
          return Result.ok();
        }else{
          return Result.fail();
        }
    }

    @Log(title = "定时发送邮件任务参数表", businessType = BusinessType.SENDMAIL)
    @PreAuthorize("hasAuthority('bnt.taskScheduledEmail.sendEmail')")
    @ApiOperation(value = "发送邮件")
    @PostMapping("/sendMail")
    public Result sendMail(@RequestBody  String id) {
        TaskScheduledEmail taskScheduledEmail = taskScheduledEmailService.getById(id);
        emailServiceImpl.sendMail(taskScheduledEmail);
        return Result.ok();
    }

}
