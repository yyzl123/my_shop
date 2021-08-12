package com.qf.common;

import com.qf.DTO.ResultBean;
import com.qf.entity.TUser;

import java.util.List;

/**
 * @ClassName BaseServiceImpl
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/12 16:57
 * @Version 1.0
 **/
public abstract class BaseServiceImpl<T> implements IBaseService<T>  {

    public abstract IBaseDAO<T> getMapper();

    public List<T> selectAll() {
        return getMapper().selectAll();
    }

    public int deleteByPrimaryKey(Long pid) {
        return getMapper().deleteByPrimaryKey(pid);
    }

    public int insert(T record) {
        return getMapper().insert(record);
    }

    public T selectByPrimaryKey(Long pid) {
        return getMapper().selectByPrimaryKey(pid);
    }

    public int updateByPrimaryKeySelective(T record) {
        return getMapper().updateByPrimaryKeySelective(record);
    }

    public int updateByPrimaryKey(T record) {
        return getMapper().updateByPrimaryKey(record);
    }
}
