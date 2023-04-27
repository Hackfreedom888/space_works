package com.Bivin.r.service.impl;


import com.Bivin.r.dto.DishDto;
import com.Bivin.r.mapper.DishMapper;
import com.Bivin.r.pojo.Dish;
import com.Bivin.r.pojo.DishFlavor;
import com.Bivin.r.service.DishService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {


    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private DishFlavorServiceImpl dishFlavorService;




    public void DishWith(DishDto dishDto){


        dishService.save(dishDto);

        Long id = dishDto.getId();  // 获取到了菜品的id






        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor:flavors){

            dishFlavor.setDishId(id);

            dishFlavorService.save(dishFlavor);

        }

    }


    public DishDto dishWith(Long id){

        DishDto dishDto = new DishDto();


        Dish dish = dishService.getById(id);
        BeanUtils.copyProperties(dish,dishDto);



        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper);




        dishDto.setFlavors(list);

        return dishDto;
    }


    public void UpdateDish(DishDto dishDto){



        dishService.updateById(dishDto);



        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);


        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor dishFlavor:flavors){

            dishFlavor.setDishId(dishDto.getId());
            dishFlavorService.save(dishFlavor);
        }

    }
}
