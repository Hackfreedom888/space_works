package com.Bivin.r.common;

import com.Bivin.r.controller.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 *  异常处理器
 */

@RestControllerAdvice   // REST风格的异常处理的注解
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)// 我们知道账户重复添加时报错的异常就是这个异常SQLIntegrityConstraintViolationException，
                                                // 因此我们可以把这个异常信息写在上面的注解当中就可以捕获到该异常，
                                                // 当出现该异常并且捕获到该异常的时候，就执行下面的方法中的信息
    public R doException(){

        return new R("兄弟，账户已存在，请重新输入");
    }


    /**
     *  业务异常处理器
     */
    @ExceptionHandler(BusinessException.class)
    public R doBusinessException(BusinessException bs){
        return new R(bs.getCode(),bs.getData()); // 把封装在BusinessException业务异常类属性中的数据响应给客户端
    }
}
