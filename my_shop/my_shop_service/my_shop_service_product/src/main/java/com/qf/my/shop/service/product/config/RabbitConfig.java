package com.qf.my.shop.service.product.config;

import com.qf.constant.QueueConstant;
import com.qf.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RabbitConfig
 * @Description
 * @Author 86139
 * @Data 2020/5/29 18:10
 * @Version 1.0
 **/
@Configuration
public class RabbitConfig {


    @Bean  //声明增加的交换机
    public TopicExchange productAddTopicExchange(){

        return  new TopicExchange(RabbitConstant.PRODUCT_ADD_TOPIC_EXCHANGE,true,false);
    }

    //创建增加队列
    @Bean
    public Queue productAddDescQueue(){
        return new Queue(QueueConstant.PRODUCT_ADD_DESC_QUEUE,true,false,false);

    }

    //创建绑定增加关系
    @Bean
    public Binding getBinding(TopicExchange productAddTopicExchange,Queue productAddDescQueue){
        return BindingBuilder.bind(productAddDescQueue).to(productAddTopicExchange).with("product.#");

    }

    @Bean //声明删除的交换机
    public TopicExchange productDeleteTopicExchange1(){

        return  new TopicExchange(RabbitConstant.PRODUCT_DELETE_TOPIC_EXCHANGE,true,false);
    }
     //创建删除队列
    @Bean
    public Queue productDeleteDescQueue1(){
        return new Queue(QueueConstant.PRODUCT_DELETE_PRODUCT_ID,true,false,false);

    }
    @Bean
    public Binding getBinding1(TopicExchange productDeleteTopicExchange1,Queue productDeleteDescQueue1){
        return BindingBuilder.bind(productDeleteDescQueue1).to(productDeleteTopicExchange1).with("product.#.del");

    }
    @Bean
    public Queue productDeleteDescQueue2(){
        return new Queue(QueueConstant.PRODUCT_DELETE_PRODUCTDESC_ID,true,false,false);

    }
    //创建绑定删除关系
    @Bean
    public Binding getBinding2(TopicExchange productDeleteTopicExchange1,Queue productDeleteDescQueue2){
        return BindingBuilder.bind(productDeleteDescQueue2).to(productDeleteTopicExchange1).with("product.#.del");

    }


}
