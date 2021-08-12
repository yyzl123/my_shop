package com.qf.my.shop.service.product;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.qf.mapper")
public class MyShopServiceProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceProductApplication.class, args);
    }

}
