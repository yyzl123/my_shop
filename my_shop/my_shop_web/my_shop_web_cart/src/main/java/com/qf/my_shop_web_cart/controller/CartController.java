package com.qf.my_shop_web_cart.controller;


import com.qf.DTO.ResultBean;
import com.qf.Util.FileUtils;
import com.qf.VO.CartItemvo;
import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.entity.TUser;
import com.qf.mapper.TProductMapper;
import com.qf.my_shop_web_cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName CartController
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/30 9:35
 * @Version 1.0
 **/
@Controller
@RequestMapping("cart")
public class CartController {


   @Autowired
   private RedisTemplate redisTemplate;

   @Autowired
   private TProductMapper mapper;

   @Autowired
   private ICartService cartService;
/*
添加商品到购物车
 */
    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean addCart(@CookieValue(name = CookieConstant.USER_CART,required = false) String uuid,
                              @PathVariable Long productId,
                              @PathVariable int count,
                              HttpServletRequest request,
                              HttpServletResponse response){

        TUser user = (TUser) request.getAttribute("user");
        if(user==null){ //user为空说明是未登录
            if(uuid==null||"".equals(uuid)){
                //未登录状态下的第一次加购物车
                uuid = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(CookieConstant.USER_CART,uuid);//user_cart uuid
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            cartService.addCart(uuid, productId, count);
        }else{
            cartService.addCart(user.getUid().toString(),productId,count);
        }
        return ResultBean.success(user,"添加购物车成功");
    }

    /**
     * 跳转购物车首页
     * @param uuid
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("show")
        public String show(@CookieValue(name=CookieConstant.USER_CART,required = false)String uuid, HttpServletRequest request, Model model){
        TUser user = (TUser) request.getAttribute("user");//获取User
        ResultBean resultBean = new ResultBean();
        if (user == null){//首次打开购物页面 没有登录是没有购物车的
            if(uuid!=null&&!"".equals(uuid)){//当前没有购物车
                resultBean = cartService.show(uuid);
            }
        }else{//说明用户已经操作过购物车
            resultBean = cartService.show(user.getUid().toString());
        }

        if(resultBean.getErrno()!=1){
            model.addAttribute("cart",resultBean.getData());
            return "shopcart";
        }
        return "shopcart";

    }

    @RequestMapping("merge")
    @ResponseBody
    public ResultBean merge(@CookieValue(name = CookieConstant.USER_CART,required = false)String uuid,//uuid 浏览器请求的一次唯一标识
                            HttpServletRequest request,
                            HttpServletResponse response){
        TUser user = (TUser) request.getAttribute("user");//从域中取出user
        if(user==null||"".equals(user)){
            return null;
        }
        String unLoginRedisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE,uuid);//将user:cart: 和 uuid拼接 作为key
        String loginRedisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE,user.getUid().toString());//将user:cart: 和 用户的uid 拼接作为key
        ResultBean resultBean = cartService.merge(user.getUid().toString(), unLoginRedisKey, loginRedisKey);
        Cookie cookie = new Cookie(CookieConstant.USER_CART,"");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return resultBean;
    }

    @RequestMapping("update/{productId}/{count}")
    @ResponseBody
    public ResultBean update(@CookieValue(name=CookieConstant.USER_CART,required = false) String uuid,
                             HttpServletRequest request,
                             @PathVariable Long productId,
                             @PathVariable int count){
        if(uuid==null||"".equals(uuid)){
            return ResultBean.error("更新失败");
        }
        TUser user = (TUser) request.getAttribute("user");
        if(user!=null){
            uuid = user.getUid().toString();
        }
        String redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE,uuid);
        List<CartItemvo> cart = (List<CartItemvo>) redisTemplate.opsForValue().get(redisKey);
        for (CartItemvo cartItemVO : cart) {
            if(cartItemVO.getPid().equals(productId)){
                cartItemVO.setCount(count);
                cartItemVO.setCreatedTime(new Date());
                cartService.createCartItemDTOIntoRedis(uuid,productId,cartItemVO);
                return ResultBean.success(count,"更新成功");
            }
        }
        return ResultBean.error("更新失败");
    }

}
