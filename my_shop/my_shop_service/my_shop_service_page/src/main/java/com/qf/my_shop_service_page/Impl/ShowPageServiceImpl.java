package com.qf.my_shop_service_page.Impl;


import com.qf.VO.Productvo;
import com.qf.constant.QueueConstant;
import com.qf.my_shop_service_page.inter.IPageService;
import com.rabbitmq.client.Channel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

/**
 * @ClassName ShowPageService
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/25 19:57
 * @Version 1.0
 **/
@Service
public class ShowPageServiceImpl implements IPageService {

    @Autowired
    private Configuration configuration;

    /*@Value("${resourcepath}")
    private String resourcepath;*/


    @Override
    @RabbitListener(queues = QueueConstant.PRODUCT_STATIC_PAGE_ADD_QUEUE_NAME)
    public void  showPage(Productvo product, Channel channel, Message message) {
        try {
            Template template =configuration.getTemplate("introduction.ftl");
            HashMap<Object, Object> map = new HashMap<>();
            map.put("pro",product);
            String pageName = product.getPid()+".html";
            Writer out = new FileWriter("D:\\mavenwork\\my_shop\\my_shop_service\\my_shop_service_page\\src\\main\\resources\\static\\"+pageName);
            template.process(map,out);
          channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }


    }
}
