package com.taotao.common.pojo;

import java.io.Serializable;

public class TaotaoResult implements Serializable {
    private Integer status;// 响应业务状态
    private String msg; // 响应的消息
    private Object data; // 响应的数据

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    //构建其他状态的taotaoresult对象
    public static TaotaoResult build(Integer status, String msg, Object data) {
        return new TaotaoResult(status, msg, data);
    }

    public static TaotaoResult ok(Object data) {
        return new TaotaoResult(data);
    }

    public static TaotaoResult ok() {
        return new TaotaoResult(null);
    }

    public static TaotaoResult build(Integer status, String msg) {
        return new TaotaoResult(status, msg, null);
    }


    public TaotaoResult() {}

    public TaotaoResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public TaotaoResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }




    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
