package com.qf.my_shop_service_page.config;

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

    @Bean
    public TopicExchange productAddTopicExchange(){
        return new TopicExchange(RabbitConstant.PRODUCT_ADD_TOPIC_EXCHANGE,true,false);
    }

    @Bean
    public Queue productAddSearchQueue(){
        return new Queue(QueueConstant.PRODUCT_STATIC_PAGE_ADD_QUEUE_NAME,true,false,false);
    }

    @Bean
    public Binding getBinding(TopicExchange productAddTopicExchange,Queue productAddSearchQueue){
        return BindingBuilder.bind(productAddSearchQueue).to(productAddTopicExchange).with("product.#");
    }


}
