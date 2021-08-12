package com.qf.my_shop_web_order.controller;


import com.qf.DTO.TProductCart;
import com.qf.Util.FileUtils;
import com.qf.VO.CartItemvo;
import com.qf.constant.CookieConstant;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName OderServiceController
 * @Description TODO
 * @Author 86139
 * @Data 2020/6/4 12:53
 * @Version 1.0
 **/
@Component
@RequestMapping("to")
public class OrderServiceController {


    @Autowired
    private RedisTemplate redisTemplate;

  /*  @RequestMapping("pay")

    public String Order(Model model){
        List<AddressDTO>  showAddress = service.showAddress();
        model.addAttribute("showAddress",showAddress);
        return "pay" ;
    }*/

    @RequestMapping("Pay")
    public String toPay(String[] ids, Model model,
                        @CookieValue(name = CookieConstant.LOGIN_USER, required = false) String uuid){
        List<TProductCart> productList = new ArrayList<>();
        for (String pid : ids) {
            String rediskey = FileUtils.getRedisKey("user:", uuid, ":cart:item",pid);
            TProductCart item = (TProductCart) redisTemplate.opsForValue().get(rediskey);
            productList.add(item);
            System.out.println(item);
        }
        model.addAttribute("productItems",productList);
        System.out.println(productList);
               return "pay";
    }
}
