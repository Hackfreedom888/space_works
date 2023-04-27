package com.Bivin.r.config;

import com.Bivin.r.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 *  扩展消息转换器
 */

@Configuration  // 定义SpringMvc容器注解
public class WebMvcConfig implements WebMvcConfigurer {


    /**
     *  扩展消息转换器
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 设置具体的对象映射器
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 通过设置索引，让自己的转换器放在最前面，否则默认的jackson转换器会在前面，用不上我们设置的转换器
        converters.add(0,messageConverter);
    }
}
