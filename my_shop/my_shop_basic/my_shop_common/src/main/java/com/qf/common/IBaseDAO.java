package com.qf.common;


import com.qf.VO.Uservo;
import com.qf.entity.TUser;

import java.util.List;

public interface IBaseDAO<T>{

    List<T> selectAll();


    int deleteByPrimaryKey(Long pid);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long pid);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    TUser selectByUname(String uname);
    TUser selectByEamil(String email);
    void  register(TUser user);
    TUser selectByPhone(String phone);
}
