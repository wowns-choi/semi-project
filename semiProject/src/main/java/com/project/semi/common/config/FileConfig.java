package com.project.semi.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
@Slf4j	
@PropertySource("classpath:/config.properties")
public class FileConfig implements WebMvcConfigurer{
	
	
	// 파일 업로드 임계값
	@Value("${spring.servlet.multipart.file-size-threshold}")
	private long fileSizeThreshold;
	
	// 요청당 파일 최대 크기
	@Value("${spring.servlet.multipart.max-request-size}")
	private long maxRequestSize;
	
	// 개별 파일당 최대 크기
	@Value("${spring.servlet.multipart.max-file-size}")
	private long maxFileSize;
	
	// 파일 업로드 임계값 초과시 임시 저장 폴더 경로를 가리킴
	@Value("${spring.servlet.multipart.location}")
	private String location;
	
	//==================================================
	// 프로필 이미지
	@Value("${my.profile.resource-handler}")
	private String profileResourceHandler;
	
	// 프로필 이미지
	@Value("${my.profile.resource-location}")
	private String profileResourceLocation;
	
	//===================================================	

	// 게시판 이미지
	@Value("${my.board.resource-handler}")
	private String boardResourceHandler; // 요청 주소
	
	@Value("${my.board.resource-location}")
	private String boardResourceLocation; // 연결될 서버 폴더 경로
	
	// 요청 주소(a 태그의 href 나 img 태그의 src속성을 말함)에 따라
	// 서버 컴퓨터의 어떤 경로에 접근할지 설정해줄 수 있다. 
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		log.info("profileResourceHandler={}", profileResourceHandler);
		log.info("profileResourceLocation={}", profileResourceLocation);
		registry.addResourceHandler(profileResourceHandler)
		.addResourceLocations(profileResourceLocation);
		
		registry.addResourceHandler(boardResourceHandler)
		.addResourceLocations(boardResourceLocation);
	}
	
	@Bean
	public MultipartConfigElement configElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		
		factory.setFileSizeThreshold(DataSize.ofBytes(fileSizeThreshold));
		
		factory.setMaxFileSize(DataSize.ofBytes(maxFileSize));
		
		factory.setMaxRequestSize(DataSize.ofBytes(maxRequestSize));
		
		factory.setLocation(location);
		
		return factory.createMultipartConfig();
	} 
	
	@Bean
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver multipartResolver
			= new StandardServletMultipartResolver();
		return multipartResolver;
	}
	
}