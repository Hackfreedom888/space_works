package com.Bivin.r;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j  // lombok提供的日志注解
@SpringBootApplication
@ServletComponentScan   // springboot提供Filter过滤器的时候，必须加该注解
public class WorksApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorksApplication.class);
        log.info("项目启动成功....");
        // 因为使用了lombok提供的日志注解，所以这里可以直接用log.info测试项目是否启动成功
    }
}
