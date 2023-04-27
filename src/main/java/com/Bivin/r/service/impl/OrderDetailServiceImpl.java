package com.Bivin.r.service.impl;

import com.Bivin.r.mapper.OrderDetailMapper;
import com.Bivin.r.pojo.OrderDetail;
import com.Bivin.r.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
