package com.qf.my_shop_service_search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.qf.mapper")
public class MyShopServiceSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceSearchApplication.class, args);
    }

}
