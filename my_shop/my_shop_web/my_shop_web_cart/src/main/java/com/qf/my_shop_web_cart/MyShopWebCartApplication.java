package com.qf.my_shop_web_cart;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.mapper")
public class MyShopWebCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopWebCartApplication.class, args);
    }

}
