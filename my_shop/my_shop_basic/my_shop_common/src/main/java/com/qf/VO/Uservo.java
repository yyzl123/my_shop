package com.qf.VO;

import java.io.Serializable;

/**
 * @ClassName Uservo
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/21 11:50
 * @Version 1.0
 **/
public class Uservo implements Serializable  {

    private String uname;

    private String password;


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
