package com.cdroho.eventlistenser;

import org.springframework.context.ApplicationEvent;


public class MyEvents extends ApplicationEvent {

    public MyEvents(Object source,String messge){

        super(source);

        this.messge=messge;

    }

    private String messge;

    private Boolean flag;

    public void printStr() {
        System.out.println("---------呼叫事件开始----------");
        System.out.println("---------呼叫患者成功！----------");
        flag=true;
    }

    public String getMessge() {
        return messge;
    }

    public void setMessge(String messge) {
        this.messge = messge;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
