package com.qf.mapper;

import com.qf.common.IBaseDAO;
import com.qf.entity.TProduct;

import java.util.List;

public interface TProductMapper extends IBaseDAO<TProduct> {
    List<TProduct> selectAll();
}