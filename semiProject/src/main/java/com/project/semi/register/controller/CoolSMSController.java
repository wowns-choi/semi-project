
package com.project.semi.register.controller;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.project.semi.common.util.Utility;
import com.project.semi.member.model.dto.Member;
import com.project.semi.register.model.service.LectureRegisterService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("coolSMS")
public class CoolSMSController {

	private final DefaultMessageService messageService;
	private final LectureRegisterService lectureRegisterService;

	@Autowired
	public CoolSMSController(LectureRegisterService lectureRegisterService, @Value("${cool.sms.apikey}") String apiKey,
			@Value("${cool.sms.apisecretkey}") String apiSecretKey

	) {
		this.lectureRegisterService = lectureRegisterService;
		this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
	}

	/**
	 * 단일 메시지 발송 예제
	 */
	@PostMapping("/send-one")
	public SingleMessageSentResponse sendOne(@RequestBody Map<String, String> map,
			@SessionAttribute("loginMember") Member loginMember) {

		String phoneNum = map.get("phoneNum");

		Message message = new Message();

		int randomNum = Utility.phoneAuth();

		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		message.setFrom("01067015246");
		message.setTo(phoneNum);
		message.setText(String.valueOf(randomNum) + "재준오빠가 보냅니다");

		// DB 에 이 랜덤한 값을 저장해놨다가 빼와야함.
		int result = lectureRegisterService.addAuth(loginMember.getMemberNo(), randomNum);

		SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
		System.out.println(response);

		return response;
	}

	@PostMapping("verify")
	public int verifyAuth(@RequestBody Map<String, String> map, @SessionAttribute("loginMember") Member loginMember) {
		String authKey = map.get("authKey");
		int result = lectureRegisterService.verifyAuth(loginMember.getMemberNo(), authKey);

		int returnValue = 0;
		if (result > 0) {
			// 인증번호가 일치하는 경우
			returnValue = 1;

		}
		return returnValue;

	}

}
