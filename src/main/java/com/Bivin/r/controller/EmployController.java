package com.Bivin.r.controller;

import com.Bivin.r.common.BaseContext;
import com.Bivin.r.pojo.Employee;
import com.Bivin.r.service.impl.EmployeeServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *  表现层
 */


@Slf4j  // 开启日志 出bug好调试
@RestController // @Controller和@ResponseBody注解 （表现层bean注解和响应前端数据注解）
@RequestMapping(value = "/employee")  // 请求访问路径
//写成/employee的目的就是：刚才前端用户在登陆页面点击登录的时候(3.1需求分析),访问后台的资源路径为post请求..8080/employee/login
public class EmployController {

    /**
     *   自动装配业务层对象 （也可以说拿到业务层对象引用，然后调用业务层对象当中的增删改查等一些功能方法）
     */
    @Autowired
    private EmployeeServiceImpl employeeService;


    @PostMapping(value = "/login")
    public R login(HttpServletRequest request, @RequestBody Employee employee){ // json格式注解

        // 1、将客户端访问提交的密码password进行md5加密处理
        String password = employee.getPassword();   // 拿到了封装到Employee实体类属性中封装到客户端请求的数据（登录密码数据）
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // 将客户端登录的密码进行md5加密,并且把加密后的密码赋给password变量


        // 2、根据页面提交的用户名username查询数据库中对应的该用户名的所有字段数据 （  用的方法是：springboot整合SSMP笔记当中的Lambda对象方式的条件查询  ）
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Employee::getUsername, employee.getUsername());
        // 调用业务层的条件查询功能
        Employee one = employeeService.getOne(queryWrapper);

        // 3、如果条件查询没有查询到结果，说明用户名不存在
        if (one == null){
            return new R("用户名不存在，登录失败");
        }

        if (! password.equals(one.getPassword())){
            return new R("密码错误，登录失败");
        }

        // 5、到这里说明用户名存在并且登录的密码也正确，那么我们就判断一下员工状态，如果为已禁用状态，则响应给客户端信息（0： 禁用  1： 启用）
        if (one.getStatus() == 0){
            return new R("账号已被禁用");
        }

        // 6、如果到了这里，就说明用户登录密码也正确，账号也没有被禁用，
        request.getSession().setAttribute("employee",one.getId());

        return new R(one,1);
    }


    @PostMapping(value = "/logout")
    public R logout(HttpServletRequest request){

        // 清理登录成功时存储在Session域当中的用户id
        request.getSession().removeAttribute("employee");   // 通过key键删除存储的用户id值（value值）
        return new R("退出成功",1);
    }


    @PostMapping
    public R save(HttpServletRequest request,@RequestBody Employee employee){
        employee.setIdNumber("123456");
        employee.setPassword("123456");
        System.out.println("adwadaw:"+employee);

        employeeService.save(employee);
        return new R(1,"添加成功");
    }



    @GetMapping(value = "/page")
    public R page(int page,int pageSize,String name){
        log.info("page:"+page +"pageSize:"+pageSize +"name:"+name);
        /**
         *  构造分页查询构造器
         */
        IPage<Employee> page1 = new Page<>(page,pageSize);



        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //  调用业务层分页查询功能方法
        employeeService.page(page1,queryWrapper);


        return new R(1,page1);
    }


    @PutMapping
    public R update(HttpServletRequest request,@RequestBody Employee employee){


        // 获取session域当中的employee对应的数据
        Object o = request.getSession().getAttribute("employee");

        BaseContext.setCurrentId((Long) o);   // session域当中的数据封装到ThreadLocal线程的储存空间中



        employeeService.updateById(employee);

        return new R("员工信息修改成功",1);
    }


    @GetMapping(value = "/{id}")
    public R getById(@PathVariable long id){

        // 接收到员工传递的id后，就可以调用业务层的通过id查询该id对应在数据库中的整条数据的方法了
        Employee employee = employeeService.getById(id);

        // 查询完之后，我们就可以把查询出来的该id在数据库中对应的整条数据响应给前端了，并且把成功状态码也响应给前端,前端拿到数据后就可以进行回显数据操作了
        if (employee !=null){
            return new R(employee,1);
        }
        return new R("该id对应的员工信息不存在");
    }

}
