package com.Bivin.r.controller;

import com.Bivin.r.common.BaseContext;
import com.Bivin.r.pojo.Category;
import com.Bivin.r.service.impl.CategoryServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController // @Controller和@ResponseBody注解 （表现层bean注解和响应前端数据注解）
@RequestMapping(value = "/category")
public class CategoryController {

    /**
     *   自动装配业务层对象 （也可以说拿到业务层对象引用，然后调用业务层对象当中的增删改查等一些功能方法）
     */
    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 添加功能
     * @param category
     * @return
     */
    @PostMapping
    public R save(@RequestBody Category category){ // 将前端请求数据封装到了实体类的属性当中

        // 调用业务层的保存功能
        categoryService.save(category);

        return new R("新增分类成功",1);

    }

    /**
     * 查询功能
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/page")
    public R page(int page,int pageSize){


        IPage<Category> page1 = new Page<>(page,pageSize);

        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        // 添加排序条件，根据sort进行排序
        lqw.orderByAsc(Category::getSort);

        // 调用业务层的分页查询功能
        categoryService.page(page1,lqw);

        return new R(page1,1);    // 把调用业务层分页查询查询出来的数据和成功标识符响应给前端
    }


    /**
     *  删除功能
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(Long ids){
        log.info("ids:"+ids);   // 输出日志判断是否拿到了客户端发送的ids请求数据资源

        // 调用业务层的通过id删除功能
        categoryService.remove(ids);

        return new R("删除成功",1); // 如果到这里就说明在业务层确实把客户端想要删除掉了，然后这里提示信息即可。
    }

    /**
     * 修改功能
     * @param request
     * @param category
     * @return
     */
    @PutMapping
    public R update(HttpServletRequest request, @RequestBody Category category){    // @RequestBody ： 接收json格式数据注解

        // ThreadLocal 把session域当中的数据存入到ThreadLocal线程储存空间中
        Object o = request.getSession().getAttribute("employee");
        BaseContext.setCurrentId((Long) o); // 把session域当中的数据存入到ThreadLocal储存空间中。

        // 调用业务层的修改功能
        categoryService.updateById(category);

        return new R("分类信息修改成功~",1);
    }

    /**
     * 条件查询功能
     * @param type
     * @return
     */
    @GetMapping(value = "/list")
    public R list(Integer type){

        // 创建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加sql语句中“=”条件
        queryWrapper.eq(type!=null,Category::getType,type);

        // 调用业务层的查询功能
        List<Category> list = categoryService.list(queryWrapper);

        // 把数据响应给前端
        return new R(1,list);   // 把封装到list集合中查询的数据响应给前端
    }

}
