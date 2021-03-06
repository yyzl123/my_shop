package com.qf.my_shop_web_background;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)
@SpringBootApplication
@EnableDubbo
public class MyShopWebBackgroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShopWebBackgroundApplication.class, args);
    }

}
