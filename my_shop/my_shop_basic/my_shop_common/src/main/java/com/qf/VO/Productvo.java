package com.qf.VO;

import com.qf.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName Productvo
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/13 16:57
 * @Version 1.0
 **/

public class Productvo extends TProduct implements Serializable {

    private String pdesc;


    public String getPdesc() {
        return pdesc;
    }

    public void setPdesc(String pdesc) {
        this.pdesc = pdesc;
    }
}
