package com.project.semi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.semi.common.interceptor.CategoryTypeInterceptor;
import com.project.semi.common.interceptor.MessageExistInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public CategoryTypeInterceptor categoryTypeInterceptor() {
		return new CategoryTypeInterceptor();
	}
	
	@Bean
	public MessageExistInterceptor messageExistInterceptor() {
		return new MessageExistInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(categoryTypeInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**",
				"/js/**",
				"/images/**",
				"/favicon.ico");
		
		registry.addInterceptor(messageExistInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**",
				"/js/**",
				"/images/**",
				"/favicon.ico");
	}
	
}
