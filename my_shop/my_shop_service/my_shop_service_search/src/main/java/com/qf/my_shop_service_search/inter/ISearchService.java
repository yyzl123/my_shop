package com.qf.my_shop_service_search.inter;

import com.qf.DTO.ResultBean;
import com.qf.VO.Productvo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

public interface ISearchService {
    public ResultBean Search();
    public ResultBean searchProductByKeyword(String keyword);
    public ResultBean addSearchVOToSolr(Productvo product, Channel channel, Message message);
    public ResultBean deleteById(Long id,Channel channel, Message message);
}
