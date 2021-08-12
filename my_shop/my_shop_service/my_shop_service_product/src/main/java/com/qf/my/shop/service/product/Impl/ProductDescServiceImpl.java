package com.qf.my.shop.service.product.Impl;

import com.qf.DTO.ResultBean;
import com.qf.VO.Productvo;
import com.qf.api.product.IProductDescService;
import com.qf.common.BaseServiceImpl;
import com.qf.common.IBaseDAO;
import com.qf.constant.QueueConstant;
import com.qf.entity.TProductDesc;
import com.qf.mapper.TProductDescMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;

import java.io.IOException;
import java.util.Date;

/**
 * @ClassName ProductDescServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/13 17:17
 * @Version 1.0
 **/
@Service
public class ProductDescServiceImpl extends BaseServiceImpl<TProductDesc> implements IProductDescService {


    @Autowired
    private TProductDescMapper mapper;

    @Override
    public IBaseDAO<TProductDesc> getMapper() {
        return mapper;
    }

    @RabbitListener(queues = QueueConstant.PRODUCT_ADD_DESC_QUEUE)
    public void insertSelective(Productvo product, Channel channel, Message message){
        TProductDesc productDesc = new TProductDesc();
        //将product封装成record
        productDesc.setPid(product.getPid());
        productDesc.setPdesc(product.getPdesc());
        productDesc.setCreateTime(new Date());
        productDesc.setUpdateTime(new Date());
        mapper.insertSelective(productDesc);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ResultBean insertSelective(TProductDesc record) {
        return null;
    }


    @RabbitListener(queues = QueueConstant.PRODUCT_DELETE_PRODUCTDESC_ID)
   public void deleteProductDescId(Long id,Channel channel,Message message){
        mapper.deleteProductDescId(id);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
