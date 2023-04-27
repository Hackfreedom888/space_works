package com.Bivin.r.common;


/**
 *  基于ThreadLocal封装工具类，用来封装数据。
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    // 用来封装session域当中的数据。（因为session域当中储存的数据：登录员工id是Long型，因此这里泛型是Long）

    /**
     *  该方法的作用就是：ThreadLocal线程通过set方法把一些数据封装到ThreadLocal提供的储存空间中（上面那个new ThreadLocal<>();就可以认为是ThreadLocal线程的储存空间）
     *
     */
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    /**
     *  该方法的作用就是：ThreadLocal线程通过get方法把封装到ThreadLocal储存空间中的数据获取出来
     */
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}

