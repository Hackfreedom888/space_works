package com.Bivin.r.service;

import com.Bivin.r.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *      业务层接口  ----- 基于 mybatis-plus
 *
 *       用法： 继承IService<Category>  并且泛型为实体类
 *
 */
public interface CategoryService extends IService<Category> {

    /**
     *  通过id删除功能方法
     */
    public void remove(Long ids);
}
