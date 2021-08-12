package com.qf.my.shop.service.handler;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.utils.StringUtils;
import com.qf.Util.FileUtils;
import com.qf.constant.RabbitConstant;
import com.qf.constant.RedisConstant;
import com.qf.my.shop.service.Properties.SmsProperties;
import com.qf.my.shop.service.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName SmsListener
 * @Description TODO
 * @Author 86139
 * @Data 2020/6/3 16:30
 * @Version 1.0
 **/
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    private SmsUtil smsUtils;

    @Autowired
    private SmsProperties prop;

    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConstant.SMS_SEND_QUEUE)
    public void proccess(String phone)throws Exception{
        String code = String.valueOf((int) (((Math.random() * 9 + 1) * 1000)));
        // 发送消息
        SendSmsResponse resp = this.smsUtils.sendSms(phone, code,
                prop.getSignName(),
                prop.getVerifyCodeTemplate());

        //2.组织键值对，存入到redis中
        String redisKey = FileUtils.crtRedisKey(RedisConstant.REGISTER_PHONE, phone);
        //设置键值对和有效期
        redisTemplate.opsForValue().set(redisKey,code,1, TimeUnit.DAYS);
    }
}
