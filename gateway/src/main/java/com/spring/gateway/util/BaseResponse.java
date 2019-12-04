package com.spring.gateway.util;

/**
 * @Author: jianchen
 * @Date: 2019/8/28 18:03
 * @Description: 响应类
 */
public class BaseResponse<T> {

    // 响应码
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
