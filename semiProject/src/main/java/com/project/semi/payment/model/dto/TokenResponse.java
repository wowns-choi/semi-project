package com.project.semi.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//이 DTO 로 받을 json 예시 :
//{"code":0,
//"message":null,
//"response":{"access_token":"14c6d0f2a5a84c9b4745b9cfbf9b59ce38582c3c","now":1709719486,"expired_at":1709721286}}
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder // 인스턴스 쉽게 만들게해줌.
public class TokenResponse {

	 private int code;
	 private String message;
	 private TokenDetails response;
	

}