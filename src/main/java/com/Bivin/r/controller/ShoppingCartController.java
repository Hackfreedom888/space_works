package com.Bivin.r.controller;

import com.Bivin.r.pojo.ShoppingCart;
import com.Bivin.r.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    @PostMapping(value = "/add")
    public R add(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request){


        Long id = (Long) request.getSession().getAttribute("user");

        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getDishId,dishId);
        queryWrapper.eq(ShoppingCart::getUserId,id);

        ShoppingCart one = shoppingCartService.getOne(queryWrapper);

        if (one !=null){



            Integer number = one.getNumber();
            one.setNumber(number+1);


            shoppingCartService.updateById(one);

        }else {

            shoppingCart.setNumber(1);
            shoppingCart.setUserId(id);
            shoppingCartService.save(shoppingCart);

        }

        return new R(1,one);
    }


    @GetMapping(value = "/list")
    public R list(HttpServletRequest request){


        Long id = (Long) request.getSession().getAttribute("user");


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        if (list!=null){

            return new R(1,list);

        }

        return new R(0,"没有数据~");
    }



    @DeleteMapping(value = "/clean")
    public R clean(HttpServletRequest request){


        Long id = (Long) request.getSession().getAttribute("user");


        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);

        shoppingCartService.remove(queryWrapper);

        return new R(1,"成功");
    }
}
