package com.qf.api.user;

import com.qf.DTO.ResultBean;
import com.qf.common.IBaseService;
import com.qf.entity.TUser;

public interface IUserService extends IBaseService<TUser> {
    TUser selectByUname(String uname);


    ResultBean regist(String phone, String code, String password);

    ResultBean registByEmail(String email, String password);
}
