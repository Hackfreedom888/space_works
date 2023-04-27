package com.Bivin.r.mapper;


import com.Bivin.r.pojo.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;


@Mapper // 必须要配上该注解
public interface EmployeeMapper extends BaseMapper<Employee> {
}
