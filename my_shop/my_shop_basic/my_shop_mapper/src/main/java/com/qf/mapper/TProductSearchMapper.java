package com.qf.mapper;

import com.qf.VO.Searchvo;
import com.qf.common.IBaseDAO;
import com.qf.entity.TProduct;

import java.util.List;

public interface TProductSearchMapper  {
    List<Searchvo> selectAll();

}