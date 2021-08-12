package com.qf.my_shop_service_search.config;

import com.qf.constant.QueueConstant;
import com.qf.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean   //声明交换机
    public TopicExchange productAddTopicExchange(){
        return new TopicExchange(RabbitConstant.PRODUCT_ADD_TOPIC_EXCHANGE,true,false);
    }

    @Bean   //声明队列
    public Queue productAddSearchQueue(){
        return new Queue(QueueConstant.PRODUCT_SEARCH_ADD_QUEUE_NAME,true,false,false);
    }


    @Bean  //声明绑定关系
    public Binding getBinding(TopicExchange productAddTopicExchange,Queue productAddSearchQueue){
        return BindingBuilder.bind(productAddSearchQueue).to(productAddTopicExchange).with("product.#");
    }


    @Bean   //声明删除交换机
    public TopicExchange productDeleteTopicExchange1(){
        return new TopicExchange(RabbitConstant.PRODUCT_DELETE_TOPIC_EXCHANGE,true,false);
    }

    @Bean   //声明删除solr队列
    public Queue productDeleteSearchQueue3(){
        return new Queue(QueueConstant.PRODUCT_DELETE_ID,true,false,false);
    }

    @Bean
    public Binding getBinding1(TopicExchange productDeleteTopicExchange1,Queue productDeleteSearchQueue3){
        return BindingBuilder.bind(productDeleteSearchQueue3).to(productDeleteTopicExchange1).with("product.#.del");
    }

}
