package com.qf.my_shop_service_pay.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName Suseccess
 * @Description TODO
 * @Author 86139
 * @Data 2020/6/4 17:34
 * @Version 1.0
 **/
@Component
@RequestMapping("pay")
public class SuccessController {

    @RequestMapping("success")
    public String success(){
        return "success";
    }

}
