package com.project.semi.email.model.service;

import java.util.Map;

public interface EmailService {

	/** 이메일을 보내는 
	 * @param inputEmail
	 * @return
	 */
	String sendEmail(String htmlName, String inputEmail);

	int checkAuthKey(Map<String, String> map);

}