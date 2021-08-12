package com.qf.DTO;

import com.qf.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName TProductCart
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/30 21:57
 * @Version 1.0
 **/
public class TProductCart implements Serializable {
    private Long pid;
    private String pname;
    private Long price;
    private String pimage;
    private int count;
    private Date createdTime;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
