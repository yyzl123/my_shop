package com.qf.my_shop_web_cart;

import com.qf.DTO.TProductCart;
import com.qf.Util.FileUtils;
import com.qf.constant.CookieConstant;
import com.qf.mapper.TProductMapper;
import com.qf.my_shop_web_cart.service.ICartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.List;

@SpringBootTest
class MyShopWebCartApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TProductMapper mapper;
    @Test
    /*void contextLoads(@CookieValue(name = CookieConstant.USER_CART,required = false) String uuid) {

        String rediskey = FileUtils.getRedisKey("user:", "20", ":cart:item:","62");
        TProductCart item = (TProductCart) redisTemplate.opsForValue().get(rediskey);
        System.out.println(item);*/
    public void test(){
    List list =   mapper.selectAll();
    }

}
