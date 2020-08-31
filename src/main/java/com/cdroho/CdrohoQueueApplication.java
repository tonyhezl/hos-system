package com.cdroho;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.ResourceUtils;


/**
 * "@EnableScheduling"-------开启定时器注解
 * “@DubboComponentScan”-----扫描含有服务注解的类
 * @DubboComponentScan(value = "com.cdroho.dubboservice.dubboserviceimpl")
 * 自动配置简要实现类(注解处理程序)找到jar里对应的实现类上的指定注解@Configuration，
 * 完成IOC功能，并且：通过AnnotationMetadata的实现实例化(猜测：无须进行注入操作)
 * @author HZL
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class CdrohoQueueApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {

		SpringApplication.run(CdrohoQueueApplication.class, args);


	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(CdrohoQueueApplication.class);
	}

}
