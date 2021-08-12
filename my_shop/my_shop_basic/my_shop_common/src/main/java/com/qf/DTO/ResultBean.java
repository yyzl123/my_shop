package com.qf.DTO;

import java.io.Serializable;

/**
 * @ClassName ResultBean
 * @Description TODO
 * @Author 86139
 * @Data 2020/5/13 15:41
 * @Version 1.0
 **/
public class ResultBean<T> implements Serializable {
    private Long serialVersionUID = 1L;

     private static  ResultBean resultBean = new ResultBean();

     private  int errno;

     private  T data;

     private  String message;

    public static ResultBean success(Object data){
        resultBean.setErrno(0);
        resultBean.setData(data);
        resultBean.setMessage("success");
        return resultBean;
    }

    public static ResultBean success(){
        resultBean.setErrno(0);
        resultBean.setData(null);
        resultBean.setMessage("success");
        return resultBean;
    }



    public static ResultBean success(Object data,String message){
        resultBean.setErrno(0);
        resultBean.setData(data);
        resultBean.setMessage(message);
        return resultBean;
    }


    public static ResultBean error(String message){
        resultBean.setErrno(1);
        resultBean.setData(null);
        resultBean.setMessage(message);
        return resultBean;
    }

    public static ResultBean error(){
        resultBean.setErrno(1);
        resultBean.setData(null);
        resultBean.setMessage("error");
        return resultBean;
    }



    public ResultBean(int errno, T data, String message) {
        this.errno = errno;
        this.data = data;
        this.message = message;
    }

    public ResultBean(int errno, T data) {
        this.errno = errno;
        this.data = data;
    }

    public ResultBean() {
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
