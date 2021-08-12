package com.qf.my_shop_service_page.inter;

import com.qf.VO.Productvo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface IPageService {
    void showPage(Productvo product,Channel channel, Message messag);
}
