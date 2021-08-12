package com.qf.api.register;


import com.qf.DTO.ResultBean;


public interface IRegisterService {
    ResultBean regist(String phone, String code, String password);

    ResultBean registByEmail(String email, String password);
}
