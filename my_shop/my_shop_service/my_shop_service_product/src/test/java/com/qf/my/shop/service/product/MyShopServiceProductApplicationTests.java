package com.qf.my.shop.service.product;

import com.qf.entity.TProduct;
import com.qf.mapper.TProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MyShopServiceProductApplicationTests {
    @Autowired
    private TProductMapper mapper;
    @Test
    void contextLoads() {
        List<TProduct> products = mapper.selectAll();
        System.out.println(products);
    }

}
