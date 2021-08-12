package com.qf.my_shop_service_user.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.DTO.EmailMessageDTO;
import com.qf.DTO.ResultBean;
import com.qf.Util.FileUtils;
import com.qf.Util.SpringSecurityUtil;
import com.qf.api.user.IUserService;
import com.qf.common.BaseServiceImpl;
import com.qf.common.IBaseDAO;
import com.qf.constant.RabbitConstant;
import com.qf.constant.RedisConstant;
import com.qf.entity.TUser;
import com.qf.mapper.TUserMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/21 11:54
 * @Version 1.0
 **/
@Component
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService {

    @Autowired
    private TUserMapper mapper;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${activeAccountServer}")
    String activeAccountServer;

    @Override
    public IBaseDAO<TUser> getMapper() {
        return mapper;
    }

    @Override
    public ResultBean insertSelective(TUser record) {
        return null;
    }

    @Override
    public TUser selectByUname(String uname) {
        System.out.println("2222"+uname.toString());
        TUser user = mapper.selectByUname(uname);
        return user;
    }


    //注册
    @Override
    public ResultBean regist(String phone, String code, String password) {
        //1.校验验证码
        String redisKey = FileUtils.crtRedisKey(RedisConstant.REGISTER_PHONE, phone);
        String redisCode = (String) redisTemplate.opsForValue().get(redisKey);
        if(code.equals(redisCode)){//验证码正确
            TUser user = new TUser();
            user.setPassword(encoder.encode(password));
            user.setPhone(phone);
            //插入到数据库中
            mapper.insertSelective(user);
            return ResultBean.success(user,"注册成功");
        }
        return ResultBean.error("验证码错误");
    }
    //邮箱注册
    @Override
    public ResultBean registByEmail(String email, String password) {

        try {
            //1.发邮件
            EmailMessageDTO message = new EmailMessageDTO();//里面有两样东西： username   url
            message.setEmail(email);
            //生成uuid
            String uuid = UUID.randomUUID().toString();
            //创建url
            String url = activeAccountServer+uuid;
            message.setUrl(url);
            rabbitTemplate.convertAndSend(RabbitConstant.EMAIL_TOPIC_EXCHANGE,"email.regist",message);
            //2.将数据插入到数据库中
            TUser user = new TUser();
            user.setEmail(email);
            user.setPassword(SpringSecurityUtil.getEncodePassword(password));
            mapper.insertSelective(user);
            //3.存入到redis中
            //组织键
            String redisKey = FileUtils.crtRedisKey(RedisConstant.REGISTER_EMAIL, uuid);
            redisTemplate.opsForValue().set(redisKey,email,10, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.error("注册失败");

        }


        return ResultBean.success("注册成功");
    }


}
