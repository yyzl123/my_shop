package com.qf.VO;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName CartItemvo
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/30 9:51
 * @Version 1.0
 **/
public class CartItemvo implements Serializable {
    private Long pid;
    private int count;
    private Date createdTime;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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
