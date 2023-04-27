package com.Bivin.r.controller;


import com.Bivin.r.common.BusinessException;
import com.Bivin.r.dto.DishDto;
import com.Bivin.r.dto.SetmealDto;
import com.Bivin.r.pojo.*;
import com.Bivin.r.service.impl.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



@RestController // @Controller和@ResponseBody注解 （表现层bean注解和响应前端数据注解）
@RequestMapping(value = "/setmeal")
@Slf4j  // 日志注解
public class SetmealController {

    @Autowired
    private SetmealServiceImpl setmealService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private SetmealDishServiceImpl setmealDishService;

    @Autowired
    private DishServiceImpl dishService;

    @Autowired
    private DishFlavorServiceImpl dishFlavorService;


    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){    // 使用DTO实体类接收客户端请求携带的所有数据

        // 调用业务层，在业务层做一些逻辑的处理。
        setmealService.saveWithDish(setmealDto);


        return new R(1);
    }


    @GetMapping(value = "/page")
    public R page(Integer page,Integer pageSize,String name){

        // 1、构造分页查询构造器
        IPage<Setmeal> page1 = new Page<>(page,pageSize);


        // 2、构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        setmealService.page(page1,queryWrapper);

        IPage<SetmealDto> page2 = new Page<>();

        BeanUtils.copyProperties(page1,page2,"records");


        List<Setmeal> records = page1.getRecords();

        List<SetmealDto> list = records.stream().map((item) ->{

            Long id = item.getCategoryId();
            Category category = categoryService.getById(id);

            SetmealDto setmealDto = new SetmealDto();


            if (category !=null){
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);

            }

            BeanUtils.copyProperties(item,setmealDto);

            return setmealDto;



        }).collect(Collectors.toList());

        page2.setRecords(list);



        return new R(1,page2);
    }


    @Transactional  // spring事务注解
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){

        setmealService.deleteDish(ids);

        return new R(1,"xxx数据删除成功");

    }


    @GetMapping(value = "/list")
    public R list(DishDto dishDto){

        throw new BusinessException(1,"此xxx暂停服务~");
    }
}
