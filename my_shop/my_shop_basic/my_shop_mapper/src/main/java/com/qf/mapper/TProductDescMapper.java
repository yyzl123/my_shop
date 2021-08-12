package com.qf.mapper;

import com.qf.common.IBaseDAO;
import com.qf.entity.TProductDesc;

import java.util.Map;

public interface TProductDescMapper extends IBaseDAO<TProductDesc> {
     void  deleteProductDescId(Long id);
     TProductDesc selectByPid(Long pid);

     void updateDescByPid(Map map);
}