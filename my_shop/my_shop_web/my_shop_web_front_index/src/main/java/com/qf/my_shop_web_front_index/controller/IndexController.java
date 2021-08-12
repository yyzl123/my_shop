package com.qf.my_shop_web_front_index.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.api.product.type.IProductTypeService;
import com.qf.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName IndexController
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/20 18:28
 * @Version 1.0
 **/
@Controller
public class IndexController {

    @Reference
    private IProductTypeService productTypeService;

    @RequestMapping({"","index"})
    public String toIndex(Model model){
        List<TProductType> tProductTypes = productTypeService.selectAll();
        model.addAttribute("tProductTypes",tProductTypes);
        return "index";
    }
}
