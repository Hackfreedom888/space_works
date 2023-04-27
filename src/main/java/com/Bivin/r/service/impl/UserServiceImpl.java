package com.Bivin.r.service.impl;

import com.Bivin.r.mapper.UserMapper;
import com.Bivin.r.pojo.User;
import com.Bivin.r.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
}
