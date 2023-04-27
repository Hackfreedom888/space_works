package com.Bivin.r.controller;

import lombok.Data;

/**
 *  通用返回结果类
 *      把服务端响应给前端的数据封装到该R对象的属性当中响应给前端用户
 */

@Data    // lombok快速开发实体类（ 自动帮我们写好setter getter toString方法了 ）
public class R {

    private Integer code;   // 标记状态码（比如1：表示成功，2：表示失败）
    private Object data;    // 封装结果数据 （用Object万能,返回给前端数据的时候，可以把表现层获取到的所有类型的数据，如Book类型、查询所有功能返回的List集合类型的数据，都可以封装到Object类型的属性当中，然后返回响应给前端人员）
    private String msg;     // 消息（比如说：调用增删改查功能失败了，返回给前端一句话让前端知道失败了）


    // 把构造方法的几种情况都写出来（因为表现层响应给前端数据的时候，不一定这三个属性都用得到，有可能就用两个参数的构造方法，有可能用三个参数的构造方法）

    public R(Object data, Integer code) {
        this.data = data;
        this.code = code;
    }

    public R(String msg, Integer code) {
        this.msg = msg;
        this.code = code;
    }

    public R(Object data, Integer code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public R(Integer code) {
        this.code = code;
    }

    public R(Integer code,Object data) {
        this.code = code;
        this.data= data;
    }


    public R(Object data) {
        this.data = data;
    }


}
