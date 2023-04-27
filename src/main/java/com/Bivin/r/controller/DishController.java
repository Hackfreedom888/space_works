package com.Bivin.r.controller;

import com.Bivin.r.dto.DishDto;
import com.Bivin.r.pojo.Category;
import com.Bivin.r.pojo.Dish;
import com.Bivin.r.pojo.DishFlavor;
import com.Bivin.r.service.impl.CategoryServiceImpl;
import com.Bivin.r.service.impl.DishFlavorServiceImpl;
import com.Bivin.r.service.impl.DishServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;



@RestController // @Controller和@ResponseBody注解 （表现层bean注解和响应前端数据注解）
@RequestMapping(value = "/dish")
@Slf4j
public class DishController {


    @Autowired
    private DishServiceImpl dishService;
    @Autowired
    private DishFlavorServiceImpl dishFlavorService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping
    public R dish(@RequestBody DishDto dishDto){
        log.info("是否接收到了携带的数据："+dishDto.toString());

        /**
         *  调用业务层做一些处理逻辑问题
         */
        dishService.DishWith(dishDto);

        String key = "dish_"+dishDto.getCategoryId() +"_"+ "status_" +dishDto.getStatus();
        redisTemplate.delete(key);

        return new R(1,"添加成功");
    }



    @GetMapping(value = "/page")
    public R page(int page,int pageSize,String name){

        /**
         *   构造分页构造器
         */
        IPage<Dish> page1 = new Page<>(page,pageSize);


        /**
         *  构造条件构造器
         */
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件 （相当于为sql拼接like语句）
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name); // 客户端传递的条件查询的name不为null时，对name进行条件分页查询，

        // 添加排序条件 （相当于为sql拼接order by语句）
        queryWrapper.orderByAsc(Dish::getUpdateTime);   // 根据修改时间进行降序排列（也就是说等会前端拿到数据之后，这个修改时间的地方是以降序的形式排序的）

        //  调用业务层分页查询功能方法
        dishService.page(page1,queryWrapper);



        // 1、构建一个DTO分页构造器
        Page<DishDto> dishDtoPage = new Page<>();



        BeanUtils.copyProperties(page1,dishDtoPage,"records");


        List<Dish> records = page1.getRecords();

        List<DishDto> list = records.stream().map((item) ->{    // 这相当于增强for循环

            Long categoryId = item.getCategoryId();// 然后一个一个获取其records集合中封装的categoryId数据
            log.info("dwadwa:"+categoryId);


            Category category = categoryService.getById(categoryId);    // 因此我们就可以让category数据库对应的数据层调用通过id查询数据的方法功能

            DishDto dishDto = new DishDto();    // 把DTO实体类对象new出来，供下面封装数据

            if (category != null){
                String name1 = category.getName();

                dishDto.setCategoryName(name1); // 获取的name菜品分类数据封装到DTO实体类的属性当中
            }


            BeanUtils.copyProperties(item,dishDto);


            return dishDto;


                }).collect(Collectors.toList());



        dishDtoPage.setRecords(list);


        return new R(1,dishDtoPage);
    }


    @GetMapping(value = "/{id}")
    public R dish(@PathVariable Long id){


        DishDto dishDto = dishService.dishWith(id);
        return new R(1,dishDto);
    }



    @PutMapping
    public R update(@RequestBody DishDto dishDto){  // RequestBody注解： 接收前端传递的json数据注解


        // 调用业务层，在业务层做一些逻辑处理。
        dishService.UpdateDish(dishDto);


        // 第二种： 通过动态获取key进行删除。我们知道存入到redis缓存中的时候就是通过动态设定key的，我们现在拿到动态设定的key，然后进行删除清空即可。
        String key = "dish_"+dishDto.getCategoryId() +"_"+ "status_" +dishDto.getStatus();
        redisTemplate.delete(key);


        return new R(1);
    }




    @GetMapping(value = "/list")
    public R list(DishDto dishDto){ // 客户端传递的川菜分类id可以封装在这个DTO实体类属性当中

        List<DishDto> dtoList =null;


        String key = "dish_"+dishDto.getCategoryId() +"_"+ "status_" +dishDto.getStatus();
        dtoList = (List<DishDto>) redisTemplate.opsForValue().get(key); // 通过key获取value值，也就是获取客户端想要的那些dtoList集合中的数据
        // 通过key判断一下redis缓存中是否有客户端想要的数据库中的那些数据

        if (dtoList !=null){


            return new R(1,dtoList);
        }
        else {



            Long id = dishDto.getCategoryId();


            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getCategoryId,id);    // 拼接sql： select * from dish = category_id = id;

            List<Dish> list = dishService.list(queryWrapper);


            dtoList =list.stream().map((item) -> {

                Long id1 = item.getId();



                LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(DishFlavor::getDishId,id1);  // 拼接sql： select * from dish_flavor = dish_id = id1;

                List<DishFlavor> list1 = dishFlavorService.list(wrapper);

                DishDto dto = new DishDto();

                dto.setFlavors(list1);

                BeanUtils.copyProperties(item,dto);

                return dto;
            }).collect(Collectors.toList());



            redisTemplate.opsForValue().set(key,dtoList,60, TimeUnit.MINUTES);


            return new R(1,dtoList);

        }

    }

}



