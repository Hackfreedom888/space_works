package com.Bivin.r.common;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 *  第二步：数据对象处理器
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     *   该方法的作用：就是为第一步实体类中有FieldFill.INSERT的属性赋值（也就是说设定填充的值）
     *
     */
    @Override
    public void insertFill(MetaObject metaObject) {

        // ！ 为createTime属性赋填充值 （也对应着数据库中的create_time字段）
        metaObject.setValue("createTime", LocalDateTime.now()); // 为createTime属性设定填充的值为当前时间LocalDateTime.now()

        // ！ 为updateTime属性赋填充值
        metaObject.setValue("updateTime",LocalDateTime.now());

        // ！ 为createUser属性赋填充值
        /**
         *  这里注意： （我们知道createUser属性我们以前用set方法为其赋值的时候，设定的是session域当中的employee对应的数据，
         *        但是我们这里拿不到session域中的数据，因为没办法获取到request域，因此我们先随便设定一个值，等会再讲如何拿session域当中储存的登录成功的数据）
         */
        metaObject.setValue("createUser",new Long(1)); // 先赋一个new Long(1)，等会再处理怎么赋session域当中的数据

        // ！ 为updateUser属性赋填充值
        metaObject.setValue("updateUser",new Long(1)); // 同理先赋一个new Long(1)，等会再处理怎么赋session域当中的数据

    }

    /**
     *  该方法的作用：就是为第一步实体类中有FieldFill._UPDATE的属性赋值（也就是说设定填充的值）
     *
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        // ！ 为updateTime属性赋填充值
        metaObject.setValue("updateTime",LocalDateTime.now());

        // ！ 为updateUser属性赋填充值
        metaObject.setValue("updateUser",new Long(1));  // 同理先赋一个new Long(1)，等会再处理怎么赋session域当中的数据


    }
}
