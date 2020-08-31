package com.cdroho.config;


import com.cdroho.utils.CheckWebSocketCall;
import com.cdroho.utils.PhaWebSocketCall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PhaWebSocketCallConfig {
    @Bean(name = "phaWebSocketCall")
    public PhaWebSocketCall myWebSocketCall(){

        return  new PhaWebSocketCall();
    }
}
