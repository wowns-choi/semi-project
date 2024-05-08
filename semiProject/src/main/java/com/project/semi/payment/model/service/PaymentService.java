package com.project.semi.payment.model.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.semi.payment.model.dto.TokenResponse;


public interface PaymentService {

	int minusRestNum(String lectureNo, String selectDate, String quantity);

	Map<String, Object> addOrder(Integer lectureNo, Integer totalPrice, Integer MemberNo, Integer quantity);

	TokenResponse getAccessToken();

	void preValidation(TokenResponse token, String merchantUid, Integer totalPrice);

	ResponseEntity<String> singlePaymentQuery(String impUid, TokenResponse token);

	void updateStatus(String merchantUid, String updateMessage);

	void addPayment(Integer memberNo, String responseBody);

	void postValidation(String responseBody)throws JsonProcessingException;

	void plusRestNum(String selectDate, String lectureNo, String quantity);

	void addRegisteredMember(Integer memberNo, Integer lectureNo, String lectureDate, String merchantUid);

	void plusFeeSettlement(Integer lectureNo, Integer totalPrice);



}
