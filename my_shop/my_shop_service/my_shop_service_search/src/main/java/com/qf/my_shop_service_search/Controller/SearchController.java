package com.qf.my_shop_service_search.Controller;


import com.qf.DTO.ResultBean;

import com.qf.my_shop_service_search.inter.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName SearchController
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/23 16:33
 * @Version 1.0
 **/
@Controller
public class SearchController {

    @Autowired
    private ISearchService searchService;

   /* @RequestMapping("checkSearch")
    public String checkSearch(){
        return "search";
    }*/

    @RequestMapping("search")
    public String search(String keyword, Model model){
        ResultBean resultBean =searchService.searchProductByKeyword(keyword);
        model.addAttribute("products",resultBean.getData());
        return "search";
    }


}
