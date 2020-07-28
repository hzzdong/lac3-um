package com.linkallcloud.um.web.oapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;

@Configuration
@SpringBootApplication(scanBasePackages = { "com.linkallcloud.um.web.oapi" })
public class UmOapiApplication implements WebMvcConfigurer, CommandLineRunner {
	private static final Log log = Logs.get();

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(fastConverter);
	}

	public static void main(String[] args) {
		SpringApplication.run(UmOapiApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.error("Track:UmOapiApplication服务器启动完成!");
	}

}
