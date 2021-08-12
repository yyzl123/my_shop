package com.qf.common;

import com.qf.DTO.ResultBean;
import com.qf.entity.TUser;

import java.util.List;

public interface IBaseService<T> {
    List<T> selectAll();

    int deleteByPrimaryKey(Long pid);

    int insert(T record);

    ResultBean insertSelective(T record);

    T selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);


}
