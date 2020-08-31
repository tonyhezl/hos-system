package com.cdroho.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid监控配置类
 * @date 2020-6-21
 * @author HZL
 */
@Configuration
public class DruidConfig {
    /**
     * @ConfigurationProperties 是将阿里巴巴druid的数据源 配置的属性和属性文件绑定在一起
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druid(){
        return new DruidDataSource();
    }

    /**
     * 配置 Druid 的监控
     * 配置一个管理后台的Servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map<String,String> map = new HashMap<>();
        map.put("loginUsername","admin");
        map.put("loginPassword","123456");
        //不写或者为null 默认允许所有,IP白名单 (没有配置或者为空，则允许所有访问)
        //map.put("allow","192.168.1.4");
        //IP黑名单(存在共同时，deny优先于allow)
        //map.put("deny","192.168.1.4");
        bean.setInitParameters(map);
        return  bean;
    }

    /**
     * 配置一个监控的Filter
     * @return
     */
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean initParam = new FilterRegistrationBean();
        initParam.setFilter(new WebStatFilter());
        Map<String,String> map = new HashMap<>();
        //初始化不拦截请求的参数
        map.put("exclusions","*.css,*.png,*.jpg,*.js,/druid/*");
        initParam.setInitParameters(map);
        return initParam;
    }
}
