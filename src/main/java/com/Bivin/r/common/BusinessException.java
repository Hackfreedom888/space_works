package com.Bivin.r.common;


/**
 *  业务异常类
 */
public class BusinessException extends RuntimeException{

    private Integer code;   // // 加个code编号属性，来用状态码标注以后出现的是哪一种异常
    private Object data;

    /**
     *  把构造方法最好都写出来
     */


    public BusinessException(Integer code) {
        this.code = code;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public BusinessException(Integer code,Object data) {
        this.data = data;
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause, Integer code) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(Throwable cause, Integer code) {
        super(cause);
        this.code = code;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    /**
     *  getter and setter方法
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }
}
