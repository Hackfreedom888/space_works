package com.Bivin.r.config;


import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  分页查询拦截器
 */

@Configuration  // 加载器注解  作用：使用该注解后，当运行springboot启动类的时候会自动加载到该分页查询拦截器
public class MPConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加个分页查询拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 添加完成后返回即可
        return interceptor;
    }
}
