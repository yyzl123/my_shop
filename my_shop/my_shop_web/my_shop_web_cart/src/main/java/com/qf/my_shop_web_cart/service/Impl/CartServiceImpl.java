package com.qf.my_shop_web_cart.service.Impl;

import com.qf.DTO.ResultBean;
import com.qf.DTO.TProductCart;
import com.qf.Util.FileUtils;
import com.qf.VO.CartItemvo;
import com.qf.constant.RedisConstant;
import com.qf.entity.TProduct;
import com.qf.entity.TUser;
import com.qf.mapper.TProductMapper;
import com.qf.my_shop_web_cart.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Service;

import java.util.*;

/**
 * @ClassName CartServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/30 20:51
 * @Version 1.0
 **/
@Component
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TProductMapper mapper;

    @Override
    public ResultBean addCart(String uuid, Long productId, int count) {
        String redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE, uuid);//拼接user:cart:uuid
        List<CartItemvo> cart = (List<CartItemvo>) redisTemplate.opsForValue().get(redisKey);//从redis中根据redisKey取出cart
        if (cart != null) {
            //当前用户已有购物车
            for (CartItemvo cartItemVO : cart) {
                if (productId.longValue() == cartItemVO.getPid().longValue()) {
                    //当前用户已有购物车，购物车中已有该商品
                    cartItemVO.setCount(cartItemVO.getCount() + count);//将原有数量加上新添加的数量
                    cartItemVO.setCreatedTime(new Date());//更新加入的时间
                    redisTemplate.opsForValue().set(redisKey, cart);//更新redis中的数据
                    //更新当前用户的该商品的item到redis
                    createCartItemDTOIntoRedis(uuid, productId, cartItemVO);
                    return ResultBean.success(cart, "添加购物车成功");
                }
            }
            //当前用户已有购物车，购物车中没有该商品
            CartItemvo cartItemVO = new CartItemvo();
            cartItemVO.setPid(productId);
            cartItemVO.setCount(count);
            cartItemVO.setCreatedTime(new Date());
            cart.add(cartItemVO);//将商品的信息存入cart
            redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE, uuid);//拼接user:cart:uuid
            redisTemplate.opsForValue().set(redisKey, cart);//根据redisKey将cart存入redis中
            //更新当前用户的该商品的item到redis
            createCartItemDTOIntoRedis(uuid, productId, cartItemVO);
            return ResultBean.success(cart, "添加购物车成功");
        }
        // 当前用户没有购物车
        cart = new ArrayList<>();//创建一个购物车cart
        CartItemvo cartItemVO = new CartItemvo();
        cartItemVO.setPid(productId);
        cartItemVO.setCount(count);
        cartItemVO.setCreatedTime(new Date());
        cart.add(cartItemVO);//将商品的信息存入cart
        redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE, uuid);//拼接"user:cart:uuid
        redisTemplate.opsForValue().set(redisKey, cart);//根据redisKey将cart存入redis中
        createCartItemDTOIntoRedis(uuid, productId, cartItemVO);
        return ResultBean.success(cart, "添加购物车成功");

    }


    public void createCartItemDTOIntoRedis(String uuid, Long productId, CartItemvo cartItemVO) {
        //更新当前用户的该商品的item到redis
        String redisCartItemKey = FileUtils.getRedisKey("user:", uuid, ":cart:item", productId.toString());
        TProductCart item = (TProductCart) redisTemplate.opsForValue().get(redisCartItemKey);
        if (item == null) {
            item = new TProductCart();
            item.setPid(productId);
            TProduct product = mapper.selectByPrimaryKey(productId);
            if (product != null) {
                //把product对象封装成item
                item.setPimage(product.getPimage());
                item.setPname(product.getPname());
                item.setPrice(product.getPrice());
            }
        }
        item.setCount(cartItemVO.getCount());
        item.setCreatedTime(cartItemVO.getCreatedTime());
        redisTemplate.opsForValue().set(redisCartItemKey, item);


    }


    @Override
    public ResultBean clean(String uuid) {
        return null;
    }

    /*@Override
    public ResultBean update(String uuid, Long productId, int count) {
         if (uuid !=null && !"".equals(uuid)){
             String redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE,uuid);
             TUser user = (TUser) redisTemplate.opsForValue().get(redisKey);
             if(user!= null){
                 List<CartItemvo> cart = (List<CartItemvo>) user;
                 for (CartItemvo cartItemvo : cart) {
                     cartItemvo.setCount(count);
                     cartItemvo.setCreatedTime(new Date());
                     redisTemplate.opsForValue().set(redisKey,cart);
                     return ResultBean.success(cart,"更新购物车成功");
                 }
             }
         }
        return ResultBean.error("当前用户没有购物车");
    }*/

    @Override
    public ResultBean show(String uuid) {
        String redisKey = FileUtils.crtRedisKey(RedisConstant.USER_CART_PRE, uuid);//将user:cart: 和 uuid拼接 成redisKey
        List<CartItemvo> cart = (List<CartItemvo>) redisTemplate.opsForValue().get(redisKey);//用redisKey 去redis中找有没有购物车
        List<TProductCart> productCarts = new ArrayList<>();
        if (cart != null && cart.size() > 0) {//cart不为空 说明用户已经添加过商品到购物车中
            for (CartItemvo cartItemvo : cart) {
                Long pid = cartItemvo.getPid();//获取购物车id
                String redisCartItemKey = FileUtils.getRedisKey("user:", uuid, ":cart:item", pid.toString());//拼接 user:uuid:cart:item pid
                TProductCart item = (TProductCart) redisTemplate.opsForValue().get(redisCartItemKey);//根据redisCartItemKey获取商品信息 item
                if (item == null) {
                    item = new TProductCart();
                    item.setPid(pid);
                    item.setCount(cartItemvo.getCount());
                    item.setCreatedTime(cartItemvo.getCreatedTime());
                    TProduct product = mapper.selectByPrimaryKey(pid);//根据商品id查询商品信息
                    if (product != null) {//如果商品存在则将商品图片 价格 名称 存入item
                        item.setPimage(product.getPimage());
                        item.setPrice(product.getPrice());
                        item.setPname(product.getPname());
                    }
                    redisTemplate.opsForValue().set(redisCartItemKey, item);//以redisCartItemKey 为键 item 为值存入redis中
                }
                productCarts.add(item);//添加商品
            }
            Collections.sort(productCarts, new Comparator<TProductCart>() {//对集合中的元素进行排序
                @Override
                public int compare(TProductCart o2, TProductCart o1) {//按照商品加入购物车时间做排序
                    return (int) (o1.getCreatedTime().getTime() - o2.getCreatedTime().getTime());
                }
            });
            return ResultBean.success(productCarts);//返回商品集合
        }
        return ResultBean.error("当前没有购物车");
    }

/*
  判断购物车中是否有商品 用户是否登录 如果未登录状态下购物车中有商品 则不合并 如果用户已登陆那么合并购物车
 */
    public ResultBean merge(String uid, String unLoginRedisKey, String loginRedisKey) {
        List<CartItemvo> unLoginCart = (List<CartItemvo>) redisTemplate.opsForValue().get(unLoginRedisKey);//根据unLoginRedisKey 取出redis中的值
        List<CartItemvo> LoginCart = (List<CartItemvo>) redisTemplate.opsForValue().get(loginRedisKey);//根据loginRedisKey 取出redis中的值
        if (unLoginCart == null || unLoginCart.size() == 0) {//如果unLoginCart 为空 说明在redis中没有存放
            return ResultBean.success("用户未登录,无需合并购物车");
        }
        if (LoginCart == null || LoginCart.size() == 0) {//如果LoginCart 为空 说明在redis中没有存放
            redisTemplate.opsForValue().set(loginRedisKey, unLoginCart);//将已登陆的购物车中的商品合并未登录中的商品 存入redis
            redisTemplate.delete(unLoginRedisKey);//删除unLoginRedisKey
            return ResultBean.success("合并成功");
        }
        Map<Long, CartItemvo> map = new HashMap<Long, CartItemvo>();
        for (CartItemvo cartItemvo : LoginCart) {//将数据存入已登录的购物车的map中
            map.put(cartItemvo.getPid(), cartItemvo);
        }
        for (CartItemvo cartItemvo : unLoginCart) {//将数据存入未登录的购物车的map中
            CartItemvo vo = map.get(cartItemvo.getPid());//获取商品id
            if (vo == null) {
                map.put(cartItemvo.getPid(), cartItemvo);
                //在redis中更新该条购物车中的商品数据
                createCartItemDTOIntoRedis(uid, cartItemvo.getPid(), cartItemvo);
            } else {
                vo.setCount(vo.getCount() + cartItemvo.getCount());
                createCartItemDTOIntoRedis(uid, vo.getPid(), vo);
                if (vo.getCreatedTime().compareTo(cartItemvo.getCreatedTime()) < 0) {
                    vo.setCreatedTime(cartItemvo.getCreatedTime());
                }
            }
        }
        Collection<CartItemvo> values = map.values();
        List<CartItemvo> mergedCart = new ArrayList<>(values);
        redisTemplate.opsForValue().set(loginRedisKey, mergedCart);
        redisTemplate.delete(unLoginRedisKey);
        return ResultBean.success("合并成功");
    }


}

