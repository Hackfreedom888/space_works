package com.Bivin.r.service.impl;


import com.Bivin.r.mapper.ShoppingCartMapper;
import com.Bivin.r.pojo.ShoppingCart;
import com.Bivin.r.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
