package com.qf.api.product;

import com.github.pagehelper.PageInfo;
import com.qf.DTO.ResultBean;
import com.qf.VO.Productvo;
import com.qf.common.IBaseService;
import com.qf.entity.TProduct;
import com.qf.entity.TProductDesc;

import java.util.List;


/**
 * @ClassName IProductService
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/12 17:06
 * @Version 1.0
 **/
public interface IProductService extends IBaseService<TProduct> {


    ResultBean selectTProductInfo(Long pid);

    ResultBean editProduct(Productvo product);

    PageInfo<TProduct> getPageInfo(int pageNum, int pageSize);

}
