package com.cdroho.config;

import com.cdroho.eventlistenser.MyEvents;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * 定义被IOC容器管理的bean
 * 对这里this含义的理解：应该是事件本身
 * @author HZL
 */


@Configuration
public class EventConfig {

    @Bean
    public MyEvents myEvents(){

        return  new MyEvents(this,"呼叫发生");
    }
}
