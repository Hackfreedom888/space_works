package com.Bivin.r.controller;

import com.Bivin.r.pojo.User;
import com.Bivin.r.service.impl.UserServiceImpl;
import com.Bivin.r.utils.ValidateCodeUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    // 注入业务层对象
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/sendMsg")
    public R sendMsg(HttpServletRequest request, @RequestBody User user) {   // @RequestBody: json格式注解


        String phone = user.getPhone();

        if (StringUtils.isNotEmpty(phone)) {


            String s = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("随机生成的验证码：" + s);

            ValueOperations ops = redisTemplate.opsForValue();
            ops.set(phone,s,5, TimeUnit.MINUTES);


            return new R(1, "发送成功");

        }

        return new R(0, "发送失败");
    }


    @PostMapping("/login")
    public R login(@RequestBody Map map, HttpSession session) {
        //log.info(map.toString());


        String phone = map.get("phone").toString();

        String code = map.get("code").toString();

        ValueOperations ops = redisTemplate.opsForValue();
        Object codeInSession = ops.get(phone);


        if (codeInSession != null && codeInSession.equals(code)) {
            //如果能够比对成功，说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, phone);
            //根据用户的手机号去用户表获取用户
            User user = userService.getOne(queryWrapper);
            if (user == null) {

                user = new User();
                user.setPhone(phone);
                user.setStatus(1); //可设置也可不设置，因为数据库我们设置了默认值
                //注册新用户
                userService.save(user);
            }
            //这一行容易漏。。保存用户登录状态
            session.setAttribute("user", user.getId()); //在session中保存用户的登录状态,这样才过滤器的时候就不会被拦截了

            redisTemplate.delete(phone);

            return new R(1, user);
        }
        return new R(0, "ERROR");
    }
}
