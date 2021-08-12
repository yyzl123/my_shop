package com.qf.my_shop_service_product_type.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.DTO.ResultBean;
import com.qf.api.product.type.IProductTypeService;
import com.qf.common.BaseServiceImpl;
import com.qf.common.IBaseDAO;
import com.qf.entity.TProductType;
import com.qf.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ProductTypeServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/20 19:09
 * @Version 1.0
 **/
@Component
@Service
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService {

    @Autowired
    private TProductTypeMapper mapper;

    @Override
    public IBaseDAO<TProductType> getMapper() {
        return mapper;
    }

    @Override
    public ResultBean insertSelective(TProductType record) {
        return null;
    }


}
