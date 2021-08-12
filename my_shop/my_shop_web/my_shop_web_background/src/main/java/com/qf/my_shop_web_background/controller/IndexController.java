package com.qf.my_shop_web_background.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.DTO.ResultBean;
import com.qf.Util.FileUtils;
import com.qf.VO.Productvo;
import com.qf.api.product.IProductDescService;
import com.qf.api.product.IProductService;
import com.qf.constant.QueueConstant;
import com.qf.constant.RabbitConstant;
import com.qf.entity.TProduct;
import com.qf.entity.TProductDesc;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Controller
public class IndexController {

    @Reference
    private IProductService productService;

    @Reference
    private IProductDescService productDescService;

    @Autowired
    private FastFileStorageClient fastClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;



    @Value("${images.server}")
    private String imageServer;

    @RequestMapping("delById")
    public String delById(Long id) {
        //productService.deleteByPrimaryKey(id);
        //searchService.deleteById(id);
        rabbitTemplate.convertAndSend(RabbitConstant.PRODUCT_DELETE_TOPIC_EXCHANGE,"product.id.del",id);
        return "redirect:index";
    }


    @RequestMapping({"", "index"})
    public String showIndex(Model model) {
       /* List<TProduct> products =redisTemplate.opsForList().range("pro",0,-1);
        if(products.size()==0){
            products = productService.selectAll();

        }*/
       List<TProduct> products = productService.selectAll();
        model.addAttribute("products", products);
        return "index";
    }

    @ResponseBody
    @RequestMapping("uploadImage")
    public ResultBean uploadImage(MultipartFile dropzFile) {


        try {
            StorePath storePath = fastClient.uploadImageAndCrtThumbImage(
                    dropzFile.getInputStream(),
                    dropzFile.getSize(),
                    FileUtils.getExtName(dropzFile.getOriginalFilename()),
                    null
            );
            String imagePath = imageServer+storePath.getFullPath();
            return ResultBean.success(imagePath,"图片上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.error("图片上传失败");
        }


    }

   @RequestMapping("addProduct")
    public String addProduct(Productvo product){
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
       ResultBean resultBean = productService.insertSelective(product);
       product.setPid((Long) resultBean.getData());
       //此时需要做三件事 1.将商品描述落库
       // 2.将商品添加到solr库中 3.生成商品的静态页面

       rabbitTemplate.convertAndSend(RabbitConstant.PRODUCT_ADD_TOPIC_EXCHANGE,"product.add",product);
       /*
       TProductDesc tProductDesc  =new TProductDesc();
       tProductDesc.setPid((Long) resultBean.getData());
       tProductDesc.setPdesc(product.getPdesc());
       tProductDesc.setCreateTime(new Date());
       tProductDesc.setUpdateTime(new Date());
       productDescService.insertSelective(tProductDesc);

       //将商品添加到solr索引库中
       Searchvo productSearchVO = new Searchvo();
       productSearchVO.setId((Long) resultBean.getData());
       productSearchVO.setTbItemPname(product.getPname());
       productSearchVO.setTbItemPimage(product.getPimage());
       productSearchVO.setTbItemPdesc(product.getPdesc());
       productSearchVO.setTbItemPrice(new BigDecimal(product.getPrice()));
       searchService.addSearchVOToSolr(productSearchVO);
       //生成商品的静态页面
       product.setPid((Long) resultBean.getData());
       pageService.showPage(product);*/
       return "redirect:index";
   }


    @RequestMapping("updateById")
    public String editProduct(Productvo product){

        ResultBean resultBean = productService.editProduct(product);


        return "redirect:index";
    }

    @RequestMapping("uploadImageByEditor")
    @ResponseBody
    public ResultBean uploadImageByEditor(MultipartFile file){
        try {
            InputStream is = file.getInputStream();
            long size = file.getSize();
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            StorePath storePath = fastClient.uploadImageAndCrtThumbImage(is, size, extName, null);
            String[] paths = new String[]{imageServer+"/"+storePath.getFullPath()};
            return ResultBean.success(paths);//errno:0  data:[path]
        } catch (IOException e) {
            e.printStackTrace();
            return ResultBean.error();
        }
    }


    @RequestMapping("page/{pageNum}/{pageSize}")
    public String showIndexByPage(
            @PathVariable int pageNum,
            @PathVariable int pageSize,
            Model model){
        //该服务需要提供封装了前端所需的所有分页信息的对象
        PageInfo<TProduct> pageInfo= productService.getPageInfo(pageNum,pageSize);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("products",pageInfo.getList());
        model.addAttribute("imageServer",imageServer);
        return "index";
    }


}
