package com.lanf.common.result;

import lombok.Getter;

/**
 * 统一返回结果状态信息类
 */
@Getter
public enum ResultCodeEnum {
    /**
     * 异常码第一位为1, 2~4位业务功能编码,5~7位自定义
     */

    //系统模块
    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SYSTEM_ERROR(100000, "系统异常，请从控制台或日志中查看具体错误信息"),
    DATA_ERROR(100001, "数据异常"),
    ILLEGAL_REQUEST(100002, "非法请求"),
    REPEAT_SUBMIT(100004, "重复提交"),
    ARGUMENT_VALID_ERROR(100005, "参数校验异常"),
    NEED_USER_NAME(100006, "用户名不能为空"),
    NEED_PASSWORD(100007, "密码不能为空"),
    PASSWORD_TOO_SHORT(100008, "密码长度不能小于8位"),
    NAME_EXISTED(100009, "用户名不允许重名"),
    INSERT_FAILED(100010, "用户新增失败，请重试"),
    WRONG_PASSWORD(100011, "密码错误"),
    NEED_LOGIN(100012, "用户未登录"),
    NEED_ADMIN(100013, "无管理员权限"),
    LOGIN_AUTH(100014, "请先登录"),
    PERMISSION(100015, "没有权限"),
    CACHEEXPIRED(100016, "缓存过期"),
    TOKENEXPIRED(100017, "登录过期,请重新登录"),
    TOKENEXPIREDBYMENU(100018, "登录过期,请重新登录"),
    LOGIN_MOBLE_ERROR(100019, "账号或密码错误"),
    ACCOUNT_STOP(100020, "账号已停用"),
    NODE_ERROR(100021, "该节点下有子节点，不可以删除"),
    ACCOUNT_ERROR(100022, "账号不正确"),
    PASSWORD_ERROR(100023, "密码不正确"),
    PARA_NOT_NULL(100024, "参数不能为空"),
    REQUEST_PARAM_ERROR(100025, "参数错误"),

    //数据库业务
    DATABASE_ERROR(1001001, "执行数据库错误"),
    STORED_PROCEDURE_NOT_FOUND_OR_EMPTY_RESULT(1001002, "未找到执行语句或语句返回的结果集为空"),
    UPDATE_ERROR(1001003, "并发修改异常"),
    DELETE_ERROR(1001004, "并发删除异常"),

    //文件处理业务
    MKDIR_FAILED(1002001, "文件夹创建失败"),
    FILE_NOT_FOUND_OR_INVALID(1002002, "该路径下未找到文件或不是文件"),
    IO_FILE_OPERATION_ERROR(1002003, "IO文件操作错误"),
    UPLOAD_ERROR(1002004, "上传文件失败"),

    //邮件业务
    ERROR_EMAIL(1010001, "发送邮件异常"),
    WRONG_EMAIL(1010002, "非法的邮件格式"),
    EMAIL_ALREADY_BEEN_REGISTERED(1010003, "邮件已被注册"),
    NO_RECIPIENT_EMAIL(1010004, "邮件发送失败, 没有收件人电子邮件或格式有误"),
    NO_CC_EMAIL(1010007, "邮件发送失败, 抄送人电子邮件格式有误"),
    EMAIL_CONTENT_EMPTY(1010005, "邮件发送失败, 邮件内容为空"),
    EMAIL_SUBJECT_NOT_SET(1010006, "邮件发送失败，没有设置邮件主题"),
    INVALID_CRON_EXPRESSION(1010008, "邮件发送失败，非法的Cron表达式");




    //异常码
    private Integer code;
    //异常信息
    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
