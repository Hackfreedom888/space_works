package com.Bivin.r.service.impl;

import com.Bivin.r.common.BusinessException;
import com.Bivin.r.dto.SetmealDto;
import com.Bivin.r.mapper.SetmealMapper;
import com.Bivin.r.pojo.Setmeal;
import com.Bivin.r.pojo.SetmealDish;
import com.Bivin.r.service.SetmealService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealServiceImpl setmealService;
    @Autowired
    private SetmealDishServiceImpl setmealDishService;


    public void saveWithDish(SetmealDto setmealDto){


        log.info("dwad:"+setmealDto);

       setmealService.save(setmealDto);

        Long id = setmealDto.getId();



        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish:setmealDishes){


            setmealDish.setSetmealId(id);

            setmealDishService.save(setmealDish);

        }

    }


    @Transactional  // spring事务注解
    public void deleteDish(List<Long> ids){


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ids!=null,Setmeal::getId,ids);  // 相当于拼接一个这样的sql语句条件：where id in (ids)；

        queryWrapper.eq(Setmeal::getStatus,0);  // 也就是说再为sql语句拼接一个 status = 0；



        List<Setmeal> list = setmealService.list(queryWrapper);



        if (list.size()>0){

            for (Setmeal item:list){
                Long id = item.getId();


                setmealService.removeById(id);


                LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1.eq(SetmealDish::getSetmealId,id);
                setmealDishService.remove(queryWrapper1);

            }
        } else throw new BusinessException("删除失败~",1);


    }

}
