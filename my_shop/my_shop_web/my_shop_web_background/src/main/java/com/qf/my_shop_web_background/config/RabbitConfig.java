package com.qf.my_shop_web_background.config;

import com.qf.constant.RabbitConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitConfig
 * @Description 生产者配置交换机
 * @Author 86139
 * @Data 2020/5/29 18:10
 * @Version 1.0
 **/
@Configuration
public class RabbitConfig {

    @Bean   //定义增加的TopicExchange交换机
    public TopicExchange productAddTopicExchange(){

        return  new TopicExchange(RabbitConstant.PRODUCT_ADD_TOPIC_EXCHANGE,true,false);
    }
}
