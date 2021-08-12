package com.qf.my_shop_service_search.Impl;

import com.qf.DTO.ResultBean;
import com.qf.VO.Productvo;
import com.qf.VO.Searchvo;
import com.qf.constant.QueueConstant;
import com.qf.constant.SolrConstant;
import com.qf.mapper.TProductSearchMapper;
import com.qf.my_shop_service_search.inter.ISearchService;
import com.rabbitmq.client.Channel;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SearchServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/22 18:48
 * @Version 1.0
 **/
@Service
public class SearchServiceImpl implements ISearchService {

  @Autowired
  private TProductSearchMapper mapper;

  @Autowired
  private SolrClient client;

  //添加solr的索引库
  public ResultBean Search(){
    List<Searchvo> searchvoList = mapper.selectAll();//查询数据库中所有的商品
   if (searchvoList!=null){
     for (Searchvo searchvo : searchvoList) {
       SolrInputDocument document = new SolrInputDocument();
       document.addField("id",searchvo.getId());
       document.addField("tb_item_pname",searchvo.getTbItemPname());
       document.addField("tb_item_price",searchvo.getTbItemPrice().floatValue());
       document.addField("tb_item_pdesc",searchvo.getTbItemPdesc());
       document.addField("tb_item_pimage",searchvo.getTbItemPimage());
       try {
         client.add(document);
       } catch (SolrServerException e) {
         e.printStackTrace();
       } catch (IOException e) {
         e.printStackTrace();
       }
     }
     try {
       client.commit();
     } catch (SolrServerException e) {
       e.printStackTrace();
     } catch (IOException e) {
       e.printStackTrace();
     }
   }

    return null;
  }

  //对查询的内容进行分词并高亮显示
  @Override
  public ResultBean searchProductByKeyword(String keyword) {
    SolrQuery query = new SolrQuery();
    query.setQuery(keyword);
    query.set("df","tb_item_keywords");
    query.setStart(0);
    query.setRows(10);
    query.setHighlight(true);
    query.addHighlightField("tb_item_pname");
    query.setHighlightSimplePre("<span style='color:red'>");
    query.setHighlightSimplePost("</span>");
    //使用solrClient执行查询
    List<Searchvo> list = null;
    try {
      QueryResponse response = client.query(query);
      SolrDocumentList results = response.getResults();  //我想要List<TProduct>
      Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();//高亮
      list = new ArrayList<Searchvo>();
      for (SolrDocument document : results) {
        Searchvo product = new Searchvo();
        product.setId(Long.parseLong((String) document.getFieldValue("id")));
        List<String> strings = highlighting.get(document.getFieldValue("id")).get("tb_item_pname");
        if(strings!=null && strings.size()>0){
          product.setTbItemPname(strings.get(0));
        }else{
          product.setTbItemPname((String) document.getFieldValue("tb_item_pname"));
        }

        product.setTbItemPdesc((String) document.getFieldValue("tb_item_pdesc"));
        product.setTbItemPimage((String) document.getFieldValue("tb_item_pimage"));
        product.setTbItemPrice(new BigDecimal((Float) document.getFieldValue("tb_item_price")));
        list.add(product);
      }
      return ResultBean.success(list);
    } catch (SolrServerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResultBean.error();
  }

  //在添加商品时添加solr索引
  @RabbitListener(queues = QueueConstant.PRODUCT_SEARCH_ADD_QUEUE_NAME)
  public ResultBean addSearchVOToSolr(Productvo product, Channel channel, Message message){

      //封装document对象
      SolrInputDocument document = new SolrInputDocument();
      document.setField(SolrConstant.ID,product.getPid().toString());
      document.setField(SolrConstant.TB_ITEM_PNAME,product.getPname());
      document.setField(SolrConstant.TB_ITEM_PRICE,Float.parseFloat(product.getPrice().toString()));
      document.setField(SolrConstant.TB_ITEM_PIMAGE,product.getPimage());
      document.setField(SolrConstant.TB_ITEM_PDESC,product.getPdesc());

      try {
          client.add(document);
          client.commit();
      } catch (SolrServerException e) {
          e.printStackTrace();
      } catch (IOException e) {
          e.printStackTrace();
      }

    try {
      channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return ResultBean.success();
  }


  //在删除商品时删除solr索引
  @Override
  @RabbitListener(queues = QueueConstant.PRODUCT_DELETE_ID)
  public ResultBean deleteById(Long id, Channel channel, Message message) {
    try {
      client.deleteById(id.toString());
      client.commit();
    } catch (SolrServerException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ResultBean.success();
  }
}
