package com.cdroho.config;


import com.cdroho.utils.CheckWebSocketCall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CheckWebSocketCallConfig {
    @Bean(name = "checkWebSocketCall")
    public CheckWebSocketCall myWebSocketCall(){

        return  new CheckWebSocketCall();
    }
}
