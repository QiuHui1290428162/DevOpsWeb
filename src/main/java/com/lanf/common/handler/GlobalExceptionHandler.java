package com.lanf.common.handler;

import com.lanf.common.exception.CacheExpiredException;
import com.lanf.common.result.Result;
import com.lanf.common.result.ResultCodeEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * 	处理统一异常的handler
 * @author Administrator
 *	ControllerAdvice拦截控制器的异常
 */
@ControllerAdvice
@Order(2)
public class GlobalExceptionHandler {

    //打印错误的日志
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    ////拦截系统发生异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        logger.error("Default Exception", e);
        return Result.fail(ResultCodeEnum.DATA_ERROR);
    }

    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        logger.error("ArithmeticException", e);
        return Result.fail().message("除數不能為0");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result error(RuntimeException e){
        logger.error("RuntimeException", e);
        return Result.fail().message(e.getMessage());
    }

    //拦截业务异常
    @ExceptionHandler(CacheExpiredException.class)
    @ResponseBody
    public Result error(CacheExpiredException e){
        logger.error("CacheExpiredException", e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    //拦截校检错误异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        logger.error("MethodArgumentNotValidException", e);
        return handleBindingResult(e.getBindingResult());
    }

    //将校验错误的信息整理并返回给前端
    private Result handleBindingResult(BindingResult result) {
        //把异常处理为对外暴露的提示
        List<String> list = new ArrayList<>();
        //hasErrors()返回异常是否包含错误信息
        if (result.hasErrors()) {
            //获取异常错误信息，并添加到list集合中
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError objectError : allErrors) {
                list.add(objectError.getDefaultMessage());
            }
        }
        //list集合为0，返回参数错误
        if (list.size() == 0) {
            return Result.fail(ResultCodeEnum.REQUEST_PARAM_ERROR);
        }
        //返回校检错误的信息
        return Result.fail(ResultCodeEnum.REQUEST_PARAM_ERROR.getCode(), list.toString());
    }

}
