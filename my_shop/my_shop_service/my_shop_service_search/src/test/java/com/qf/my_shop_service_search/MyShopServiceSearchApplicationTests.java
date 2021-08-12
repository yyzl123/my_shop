package com.qf.my_shop_service_search;

import com.qf.DTO.ResultBean;
import com.qf.VO.Searchvo;
import com.qf.my_shop_service_search.inter.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MyShopServiceSearchApplicationTests {

    @Autowired
    private ISearchService searchService;

    @Autowired
    private SolrClient solrClient;

    @Test
    void test(){
        ResultBean resultBean = searchService.searchProductByKeyword("手机");
        System.out.println(resultBean);
    }


    @Test
    void contextLoads() {
        searchService.Search();
    }

    @Test

    void hight(){
        SolrQuery query = new SolrQuery();
        query.setQuery("手机");
        query.set("dl","tb_item_keywords");
        query.setStart(0);
        query.setRows(10);
        query.setHighlight(true);
        query.addHighlightField("tb_item_pname");
        query.setHighlightSimplePre("<span style='color:red'>");
        query.setHighlightSimplePost("</span>");


        try {
            QueryResponse response = solrClient.query(query);
            SolrDocumentList list = response.getResults();
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<Searchvo> products = new ArrayList<>();
            for (SolrDocument document : list) {
                Searchvo product = new Searchvo();
                product.setId(Long.parseLong((String) document.get("id")));
                Map<String, List<String>> map = highlighting.get(document.get("id"));
                List<String> strings = map.get("tb_item_pname");
                if(strings != null && strings.size()>0){
                    String tb_itme_pname = strings.get(0);
                    product.setTbItemPname(tb_itme_pname);
                }else{

                    product.setTbItemPname((String) document.get("tb_itme_pname"));
                }

                product.setTbItemPrice(new BigDecimal((Float)document.get("tb_item_price")));
                product.setTbItemPdesc((String) document.get("tb_item_pdesc"));
                product.setTbItemPimage((String) document.get("tb_item_pimage"));
                products.add(product);
            }
            System.out.println(products);

        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
