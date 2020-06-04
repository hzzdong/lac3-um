package com.linkallcloud.um.kh;

import java.util.List;

import javax.servlet.MultipartConfigElement;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.linkallcloud.sso.client.validation.ServiceTicketValidator;
import com.linkallcloud.sso.client.web.filter.AuthenticationFilter;
import com.linkallcloud.sso.client.web.filter.TicketValidationFilter;
import com.linkallcloud.um.iapi.party.IKhUserManager;
import com.linkallcloud.um.kh.aop.LacPermissionInterceptor;
import com.linkallcloud.um.kh.aop.LoginFilter;
import com.linkallcloud.web.support.AppVisitorArgumentResolver;
import com.linkallcloud.web.support.TraceArgumentResolver;

@Configuration
@SpringBootApplication(scanBasePackages = { "com.linkallcloud.um.kh" })
public class UmKhApplication implements WebMvcConfigurer {

	@Value("${lac.lf.appclazz:1}")
	private int myAppClazz;

	@Value("${lac.lf.appcode}")
	private String myAppCode;

	@Value("${lac.lf.appServerName:localhost}")
	private String appServerName;

	@Value("${lac.lf.ssoServer:http://localhost/sso}")
	private String ssoServer;

	@Value("${lac.lf.loginUrl:/login}")
	private String loginUrl;

	@Reference(version = "${dubbo.service.version}", application = "${dubbo.application.id}")
	private IKhUserManager khUserManager;

	@Bean
	@Order(1)
	public FilterRegistrationBean<AuthenticationFilter> authenticationFilter() {
		FilterRegistrationBean<AuthenticationFilter> frb = new FilterRegistrationBean<AuthenticationFilter>();
		frb.setFilter(new AuthenticationFilter(myAppClazz, myAppCode, appServerName, ssoServer));
		frb.addUrlPatterns("/ssoauth");
		frb.setName("AuthenticationFilter");
		return frb;
	}

	@Bean
	@Order(2)
	public FilterRegistrationBean<TicketValidationFilter> serviceTicketValidationFilter() {
		FilterRegistrationBean<TicketValidationFilter> frb = new FilterRegistrationBean<TicketValidationFilter>();
		frb.setFilter(new TicketValidationFilter(myAppClazz, myAppCode, appServerName, null,
				new ServiceTicketValidator(ssoServer, false)));
		frb.addUrlPatterns("/ssoauth");
		frb.setName("ServiceTicketValidationFilter");
		return frb;
	}

	@Bean
	@Order(3)
	public FilterRegistrationBean<LoginFilter> loginFilterReg() {
		FilterRegistrationBean<LoginFilter> frb = new FilterRegistrationBean<LoginFilter>();
		frb.setFilter(new LoginFilter(myAppCode, khUserManager, loginUrl));
		frb.addUrlPatterns("/ssoauth");
		frb.setName("LoginFilter");
		return frb;
	}

	@Bean
	public LacPermissionInterceptor getLacPermissionInterceptor() {
		return new LacPermissionInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration pi = registry.addInterceptor(getLacPermissionInterceptor());
		pi.excludePathPatterns("/index", "/js/**", "/css/**", "/images/**", "/img/**", "/static/**", "/login/**",
				"/verifyCode", "/imageValidate", "/exit", "/unsupport", "/error", "/pub/**", "/nnl/**", "/face/**");
		pi.addPathPatterns("/face/**");
		pi.order(5);

		WebMvcConfigurer.super.addInterceptors(registry);
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new AppVisitorArgumentResolver());
		// resolvers.add(new TidHandlerMethodArgumentResolver());
		resolvers.add(new TraceArgumentResolver());
		WebMvcConfigurer.super.addArgumentResolvers(resolvers);
	}

	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastConverter.setFastJsonConfig(fastJsonConfig);
		return new HttpMessageConverters(fastConverter);
	}

	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("MYSESSION");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		return serializer;
	}

	public static void main(String[] args) {
		SpringApplication.run(UmKhApplication.class, args);
	}

	/**
	 * 文件上传配置
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(DataSize.parse("10240KB")); // KB,MB
		// 设置总上传数据总大小
		factory.setMaxRequestSize(DataSize.parse("102400KB"));
		return factory.createMultipartConfig();
	}

}
