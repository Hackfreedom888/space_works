package com.Bivin.r.service.impl;

import com.Bivin.r.common.BusinessException;
import com.Bivin.r.mapper.CategoryMapper;
import com.Bivin.r.pojo.Category;
import com.Bivin.r.pojo.Dish;
import com.Bivin.r.pojo.Setmeal;
import com.Bivin.r.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  业务层实现类      ---- 基于 mybatis-plus
 *
 *  用法： 继承mybatis-plus提供的ServiceImpl，并且泛型第一个是数据层接口，第二个是实体类。
 *      并且也要实现业务层接口
 */
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private SetmealServiceImpl setmealServicel;


    /**
     *  通过id删除功能
     *
     */

    @Override
    public void remove(Long ids) {




        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, ids);


        int i = dishService.count(dishLambdaQueryWrapper);
        if (i > 0) {


            throw new BusinessException("当前分类下关联了，不能删除~",1);
        }



        // 构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, ids);


        int i1 = setmealServicel.count(queryWrapper);
        if (i1 > 0) {


            throw new BusinessException("当前分类下关联了，不能删除~");

        }



        super.removeById(ids);

    }
}
