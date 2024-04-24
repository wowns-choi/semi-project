package com.project.semi.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	//스프링 부트 2.0 이후 버전에서는 RestTemplate 을 자동으로 빈으로 등록해주지 않음. -> 수동 빈 설정 필요.
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}