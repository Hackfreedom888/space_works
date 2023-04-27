package com.Bivin.r.controller;

import com.Bivin.r.pojo.*;
import com.Bivin.r.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



@RestController
@RequestMapping(value = "/order")
@Slf4j
public class OrderController {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;


    @PostMapping(value = "/submit")
    @Transactional
    public R submit(@RequestBody Orders orders, HttpServletRequest request){


        Long id = (Long) request.getSession().getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        log.info("xxxx）:"+list);


        User user = userService.getById(id);
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        log.info("dwadwa:"+user);
        log.info("dwadwa:"+addressBook);



        AtomicInteger amount = new AtomicInteger(0);

        for (ShoppingCart data : list) {

            BigDecimal amount1 = data.getAmount();
            Integer number = data.getNumber();

            amount.addAndGet(amount1.multiply(new BigDecimal(number)).intValue());


        }


        long orderId = IdWorker.getId();
        log.info("xxxx："+orderId);

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime (LocalDateTime.now()) ;
        orders.setStatus(2) ;
        orders.setAmount (new BigDecimal(amount.get()));
        orders.setNumber(String.valueOf(orderId));
        orders. setUserName(user.getName());
        orders.setUserId(id);
        orders.setAddress("1");
        orders.setUserName(user.getName());
        orders.setPhone(addressBook.getPhone());



        orderService.save(orders);


        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(queryWrapper);

        return new R(1,"订单成功");
    }



    @GetMapping(value = "/page")
    public R order(int page, int pageSize, String number,
                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date beginTime,
                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime){

        log.info("beginTime:"+beginTime);   // 接收到的格式： Wed Sep 28 00:00:00 CST 2022
        log.info("endTime:"+endTime);       // 接收到的格式： Thu Oct 20 23:59:59 CST 2022

        // 将接收到的格式转换成中文时间格式
        String s =null;
        String s1 =null;
        if (beginTime!=null && endTime!=null){
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             s = sdf.format(beginTime);
             s1 = sdf.format(endTime);
            log.info("测试是否转换成中文时间:"+s);   // 转换成了中文时间格式如 ： 2022-09-28 00:00:00
        }


        Page<Orders> page1 = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.between(Orders::getOrderTime,s,s1);    // 加入开始和结束之间的时间条件
        orderService.page(page1, queryWrapper);

        return new R(1,page1);  // 把查询出来的数据响应给前端即可

    }
}
