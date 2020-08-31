package com.cdroho.modle.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/myconfig.properties")
public class RuleConfigDto {

    @Value("${myconfig.beCompared}")
    public int beCompared;

    public int getBeCompared() {
        return beCompared;
    }

    public void setBeCompared(int beCompared) {
        this.beCompared = beCompared;
    }
}
