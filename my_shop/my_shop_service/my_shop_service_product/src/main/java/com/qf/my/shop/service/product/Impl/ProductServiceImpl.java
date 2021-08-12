package com.qf.my.shop.service.product.Impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.DTO.ResultBean;
import com.qf.VO.Productvo;
import com.qf.api.product.IProductService;
import com.qf.common.BaseServiceImpl;
import com.qf.common.IBaseDAO;
import com.qf.constant.QueueConstant;
import com.qf.entity.TProduct;
import com.qf.entity.TProductDesc;
import com.qf.mapper.TProductDescMapper;
import com.qf.mapper.TProductMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ProductServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/12 17:36
 * @Version 1.0
 **/
@Component
@Service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService {

   @Autowired
   private TProductMapper mapper;

   @Autowired
   private TProductDescMapper descMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public IBaseDAO<TProduct> getMapper() {
        return mapper;
    }

    @Override
    public ResultBean insertSelective(TProduct record) {
        getMapper().insertSelective(record);
        return ResultBean.success(record.getPid());
    }

    @RabbitListener(queues = QueueConstant.PRODUCT_DELETE_PRODUCT_ID)
    public void  deleteByPrimaryKey(Long id, Channel channel, Message message){
         mapper.deleteByPrimaryKey(id);
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    public List<TProduct> selectAll(){
        /*while (redisTemplate.opsForList().size("pro")>0){
            redisTemplate.opsForList().rightPop("pro");
        }
        redisTemplate.opsForList().rightPushAll("pro", getMapper().selectAll());*/
        return getMapper().selectAll();
    }

    @Override
    public ResultBean selectTProductInfo(Long pid) {
        Productvo productvo = new Productvo();
        TProduct product = mapper.selectByPrimaryKey(pid);
        TProductDesc productDesc = descMapper.selectByPid(pid);
        //封装
        productvo.setPid(product.getPid());
        productvo.setCreateTime(product.getCreateTime());
        productvo.setCreateUser(product.getCreateUser());
        productvo.setFlag(product.getFlag());
        productvo.setPimage(product.getPimage());
        productvo.setPname(product.getPname());
        productvo.setPrice(product.getPrice());
        productvo.setStatus(product.getStatus());
        productvo.setUpdateTime(product.getUpdateTime());
        productvo.setUpdateUser(product.getUpdateUser());
        productvo.setPdesc(productDesc.getPdesc());
        return ResultBean.success(productvo);
    }

    @Override
    public ResultBean editProduct(Productvo product) {
        try {
        mapper.updateByPrimaryKey(product);
        Map map = new HashMap();
        map.put("pid",product.getPid());
        map.put("pdesc",product.getPdesc());
        descMapper.updateDescByPid(map);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultBean.error("更新失败");
        }
        return ResultBean.success("更新成功");
    }

    @Override
    public PageInfo<TProduct> getPageInfo(int pageNum, int pageSize) {
        //使用pageHelper开始分页
        PageHelper.startPage(pageNum,pageSize);

        List<TProduct> list = selectAll();
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list,5);

        return pageInfo;
    }
}
