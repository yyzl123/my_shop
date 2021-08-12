package com.qf.my_shop_web_cart.interceptor;

import com.qf.Util.FileUtils;
import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.entity.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName CartInterceptor
 * @Description TODO
 * @Author 86139
 * @Data 2020/6/1 16:04
 * @Version 1.0
 **/
@Component
public class CartInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies!=null){
            for (Cookie cookie : cookies) {
              if(CookieConstant.LOGIN_USER.equals(cookie.getName())){
                  String uid = cookie.getValue();
                  TUser user  = (TUser) redisTemplate.opsForValue().get(FileUtils.crtRedisKey(RedisConstant.LONG_USER_KEY_PRE,uid));
                  request.setAttribute("user",user);
                  return true;
              }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
