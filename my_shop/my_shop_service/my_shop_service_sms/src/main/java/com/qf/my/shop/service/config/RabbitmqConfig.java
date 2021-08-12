package com.qf.my.shop.service.config;


import com.qf.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {


    @Bean
    public TopicExchange getExchange(){
        return new TopicExchange(RabbitConstant.SMS_TOPIC_EXCHANGE,true,false);
    }

    @Bean
    public Queue getQueue(){
        return new Queue(RabbitConstant.SMS_SEND_QUEUE,true,false,false);
    }



    @Bean
    public Binding getBinding(Queue getQueue, TopicExchange getExchange) {
        return BindingBuilder.bind(getQueue).to(getExchange).with("sms.send");
    }

}
