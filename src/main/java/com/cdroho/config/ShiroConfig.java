package com.cdroho.config;

import java.util.HashMap;
import java.util.Map;

import com.cdroho.utils.EnceladusShiroRealm;
import com.cdroho.utils.PasswordHelper;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 * 提供基础算法与散列次数这两个变量
 * ShiroFilter入口来拦截需要安全控制的URL
 * 如果是护士登陆，则除了分诊台管理模块其他所有模块无权访问
 * @author HZL
 * @date 2020-4-7
 */
@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
        //设置登录失败，授权成功、授权失败之后的url
        //如果是未登录，则会跳转至登陆界面；如果是登陆后的用户无权限，则跳转至授权失败url
        shiroFilterFactoryBean.setLoginUrl("/cdroho/testone/loinFailed");
        shiroFilterFactoryBean.setUnauthorizedUrl("/cdroho/testone/failed");
        shiroFilterFactoryBean.setSuccessUrl("/home/index");
        //authc指定需要认证的uri，anon指定排除认证的url
        filterChainDefinitionMap.put("/*", "authc");
        filterChainDefinitionMap.put("/webservice", "anon");
        filterChainDefinitionMap.put("/wait/*", "anon");
        filterChainDefinitionMap.put("/authc/index", "authc");
        //问题：无法跳转至指定url
        filterChainDefinitionMap.put("/query/queryTriage", "roles[admin]");
        filterChainDefinitionMap.put("/authc/admin", "roles[admin]");
        filterChainDefinitionMap.put("/authc/renewable", "perms[Create,Update]");
        filterChainDefinitionMap.put("/authc/removable", "perms[Delete]");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法
        hashedCredentialsMatcher.setHashAlgorithmName(PasswordHelper.ALGORITHM_NAME);
        // 散列次数
        hashedCredentialsMatcher.setHashIterations(PasswordHelper.HASH_ITERATIONS);
        return hashedCredentialsMatcher;
    }

    @Bean
    public EnceladusShiroRealm shiroRealm() {
        EnceladusShiroRealm shiroRealm = new EnceladusShiroRealm();
        // 原来在这里
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    @Bean
    public PasswordHelper passwordHelper() {
        return new PasswordHelper();
    }
}
