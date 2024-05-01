package com.project.semi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.semi.common.interceptor.CategoryTypeInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public CategoryTypeInterceptor categoryTypeInterceptor() {
		return new CategoryTypeInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(categoryTypeInterceptor())
		.addPathPatterns("/**")
		.excludePathPatterns("/css/**",
				"/js/**",
				"/images/**",
				"/favicon.ico");
	}
}
