package com.cdroho.config;

import com.cdroho.utils.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolUitls {
    @Bean
    public Tool myTool(){

        return  new Tool();
    }
}
