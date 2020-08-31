package com.cdroho.config;


import com.cdroho.utils.WebSocketCall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketCallConfig {
    @Bean(name = "webSocketCall")
    public WebSocketCall myWebSocketCall(){

        return  new WebSocketCall();
    }
}
