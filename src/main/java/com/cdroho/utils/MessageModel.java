package com.cdroho.utils;

import java.io.Serializable;

public class MessageModel implements Serializable {

    /**
     * 状态码
     * 200正常返回
     * 400参数错误
     * 500服务器异常
     */
    private int stutas;

    /**
     * 发生的消息
     */
    private Object data;

    /**
     * 发送的消息类型
     * heart:心跳检查
     * data:业务数据
     * error:错误信息
     */
    private String type;

    public int getStutas() {
        return stutas;
    }

    public MessageModel setStutas(int stutas) {
        this.stutas = stutas;
        return this;
    }

    public Object getData() {
        return data;
    }

    public MessageModel setData(Object data) {
        this.data = data;
        return this;
    }

    public String getType() {
        return type;
    }

    public MessageModel setType(String type) {
        this.type = type;

        return this;
    }



}
