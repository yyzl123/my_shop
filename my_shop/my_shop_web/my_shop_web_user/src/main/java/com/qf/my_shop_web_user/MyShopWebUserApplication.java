package com.qf.my_shop_web_user;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class MyShopWebUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopWebUserApplication.class, args);
    }

}
