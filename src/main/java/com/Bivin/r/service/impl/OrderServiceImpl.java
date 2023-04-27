package com.Bivin.r.service.impl;

import com.Bivin.r.mapper.OrderMapper;
import com.Bivin.r.pojo.Orders;
import com.Bivin.r.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
}
