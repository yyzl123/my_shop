package com.qf.VO;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName Searchvo
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/22 18:54
 * @Version 1.0
 **/
public class Searchvo implements Serializable {

    private Long id;
    private String tbItemPname;
    private BigDecimal tbItemPrice;
    private String tbItemPdesc;
    private String tbItemPimage;

    public String getTbItemPimage() {
        return tbItemPimage;
    }

    public void setTbItemPimage(String tbItemPimage) {
        this.tbItemPimage = tbItemPimage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTbItemPname() {
        return tbItemPname;
    }

    public void setTbItemPname(String tbItemPname) {
        this.tbItemPname = tbItemPname;
    }

    public BigDecimal getTbItemPrice() {
        return tbItemPrice;
    }

    public void setTbItemPrice(BigDecimal tbItemPrice) {
        this.tbItemPrice = tbItemPrice;
    }

    public String getTbItemPdesc() {
        return tbItemPdesc;
    }

    public void setTbItemPdesc(String tbItemPdesc) {
        this.tbItemPdesc = tbItemPdesc;
    }
}
