package com.Bivin.r.service.impl;

import com.Bivin.r.mapper.EmployeeMapper;
import com.Bivin.r.pojo.Employee;
import com.Bivin.r.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;



@Service    // bean注解


public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
