package com.cdroho.eventlistenser;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PublishEvents {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MyEvents myEvents;

    public void punlishEvents(){

        applicationContext.publishEvent(myEvents);

        System.out.println("事件监听结束！");

    }

}
