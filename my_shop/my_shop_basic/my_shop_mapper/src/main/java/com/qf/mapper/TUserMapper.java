package com.qf.mapper;


import com.qf.common.IBaseDAO;
import com.qf.entity.TUser;

public interface TUserMapper extends IBaseDAO<TUser> {
    TUser selectByUname(String uname);

}