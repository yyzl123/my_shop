package com.qf.my_shop_web_cart.service;

import com.qf.DTO.ResultBean;
import com.qf.VO.CartItemvo;

public interface ICartService {
   //添加购物车
   ResultBean addCart(String uuid, Long productId, int count);
   //清除购物车
   ResultBean clean(String uuid);
   //跟新购物车
   /*ResultBean update(String uuid, Long productId, int count);*/
   //展示购物车
   ResultBean show(String uuid);

   void createCartItemDTOIntoRedis(String uuid, Long productId, CartItemvo cartItemVO);

   ResultBean merge(String uid,String unLoginRedisKey,String loginRedisKey);
}
