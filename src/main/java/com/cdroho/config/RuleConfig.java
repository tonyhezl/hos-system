package com.cdroho.config;

import com.cdroho.domaindto.DomainRuleDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 定义被IOC容器管理的bean
 * @author HZL
 */
@Configuration
public class RuleConfig {
    @Bean
    public DomainRuleDto domainRuleDto(){

        return  new DomainRuleDto();
    }
}
