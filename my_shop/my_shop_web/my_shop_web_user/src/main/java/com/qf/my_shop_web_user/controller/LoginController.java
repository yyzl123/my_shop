package com.qf.my_shop_web_user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.DTO.ResultBean;
import com.qf.Util.FileUtils;
import com.qf.Util.HttpClientUtils;
import com.qf.VO.Uservo;
import com.qf.api.user.IUserService;
import com.qf.constant.CookieConstant;
import com.qf.constant.RedisConstant;
import com.qf.entity.TUser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/21 11:26
 * @Version 1.0
 **/
@Controller
public class LoginController {

    @Reference
    private IUserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 跳转登陆页面
     * @return
     */
    @RequestMapping("showLogin")
    public String showLogin(){
        return "login";
    }

    /**
     * 用户登陆
     * @param uuid
     * @param user
     * @param password
     * @param response
     * @return
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultBean login(@CookieValue(name = CookieConstant.USER_CART,required = false)String uuid,//uuid 登录时生成的一次唯一标识
                            Uservo user,String password, HttpServletResponse response){
        System.out.println("uname="+user.getUname().toString());
       TUser user1 = userService.selectByUname(user.getUname());//根据输入的name 获取存放在数据库的User实体类
        //System.out.println("user1="+user1);
      if (user1!=null){
          if(user!=null&&!"".equals(password)&&encoder.matches(password,user1.getPassword())){
              //判断用户名不为空 并且 密码不为空 并且 将输入的密码与数据库中的密码做比较
                user1.setPassword(null);
                String redisKey = FileUtils.crtRedisKey(RedisConstant.LONG_USER_KEY_PRE,user1.getUid().toString());//将long:user: 与用户的uid拼接
                System.out.println("redisKey:="+redisKey);
              System.out.println("user1:="+user1);
                redisTemplate.opsForValue().set(redisKey,user1);//将组成的key和用户实体信息存入redis中 redisKey为主键
                Cookie cookie = new Cookie(CookieConstant.LOGIN_USER,user1.getUid().toString());//将login_user与用户的uid拼接
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*7);
                response.addCookie(cookie);//将用户信息存入cookie中保留7天

                //合并购物车调用cart中的merge
                StringBuilder Cliencookie = new StringBuilder().append(CookieConstant.USER_CART)
                        .append("=").append(uuid).append(";").append(CookieConstant.LOGIN_USER)
                        .append("=").append(user1.getUid().toString());//拼接user_cart=uuid;login_user=uid
                HttpClientUtils.doGet("http://localhost:9097/cart/merge",Cliencookie.toString());//发送GET请求 携带url和cookie
                return ResultBean.success(user1,"登录成功");
            }
        }
        return ResultBean.error("用户名或密码错误");
    }





    @RequestMapping("checkIsLogin")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        String uid = null;
        Cookie[] cookies = request.getCookies();//取出Cookies
        if(cookies!=null){
            for (Cookie cookie : cookies) {//遍历cookies
                if(CookieConstant.LOGIN_USER.equals(cookie.getName())){//将定义的LOGIN_USER和cookie中的name比较
                    uid=cookie.getValue();//取出存在cookie中的uid
                }
            }
        }
        if(uid!=null){//判断uid不为null
            //根据LONG_USER_KEY_PRE和uid拼成的KEY去redis中找到存放的user
            TUser user = (TUser) redisTemplate.opsForValue().get(FileUtils.crtRedisKey(RedisConstant.LONG_USER_KEY_PRE,uid));
            if(user!=null) {//如果user不为空 说明用户已经登陆了
                return ResultBean.success(user, "用户已登录");
            }
        }
        return ResultBean.error("用户未登录");
    }

    /**
     * 退出
     * @param request
     * @param response
     * @return
     */
     @RequestMapping("myLogout")
     @ResponseBody
    public ResultBean Logout(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();//取出Cookies
        if(cookies!=null){
            for (Cookie cookie : cookies) {//遍历cookies
                if(CookieConstant.LOGIN_USER.equals(cookie.getName())){//将定义的LOGIN_USER和cookie中的name比较
                  String  uid = cookie.getValue();//取出存在cookie中的uid
                  redisTemplate.delete(FileUtils.crtRedisKey(RedisConstant.LONG_USER_KEY_PRE,uid));//根据LONG_USER_KEY_PRE和uid拼成的KEY去redis中删除存放的信息
                }
            }
        }
        Cookie cookie = new Cookie(CookieConstant.LOGIN_USER,"");//重新设置空cookie
        cookie.setDomain("qf.com");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
      return  ResultBean.success("用户已退出");
     }


}
