package com.Bivin.r.controller;

import com.Bivin.r.pojo.AddressBook;
import com.Bivin.r.service.AddressBookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    @PostMapping
    public R save(@RequestBody AddressBook addressBook, HttpServletRequest request){

        log.info("接收到了客户端传递的参数数据:"+addressBook);

        // 1、首先我们把移动端客户手机登录成功时储存在session域当中的用户id拿出来。
        Object user = request.getSession().getAttribute("user");


        addressBook.setUserId((Long) user);

        // 3、直接调用保存功能，把封装在实体类addressBook属性中的数据保存到数据库当中去。
        addressBookService.save(addressBook);

        return new R(1,addressBook);
    }


    @GetMapping(value = "/list")
    public R List(HttpServletRequest request){


        Long user = (Long) request.getSession().getAttribute("user");


        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,user);   // 拼接这个sql语句：select * from address_book where user_id = ?   (?对应的是：user 用户名id)

        List<AddressBook> list = addressBookService.list(queryWrapper);


        return new R(1,list);
    }




    @PutMapping(value = "/default")
    public R set_default(@RequestBody AddressBook addressBook){


        Long id = addressBook.getId();

        AddressBook data = addressBookService.getById(id);

        data.setIsDefault(1);

        addressBookService.updateById(data);

        return new R(1,addressBook);    // 然后把修改过isDefault字段的数据响应给前端，让前端展示页面即可

    }


    @GetMapping(value = "/default")
    public R default_g(){

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault,1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        return new R(1,one);
    }

}
