package com.qf.my_shop_service_email.controller;


import com.qf.DTO.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("email")
public class EmailController {


    @RequestMapping("active")
    @ResponseBody
    public ResultBean actionAccount(String uuid){

        return ResultBean.success(uuid);
    }

}
