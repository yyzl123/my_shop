package com.qf.my_shop_web_order;

import com.qf.DTO.TProductCart;
import com.qf.Util.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class MyShopWebOrderApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;


    @Test
    void contextLoads() {

    }

}
