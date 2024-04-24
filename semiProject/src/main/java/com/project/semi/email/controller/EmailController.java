package com.project.semi.email.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.semi.email.model.service.EmailService;
import com.project.semi.member.controller.MemberController;
import com.project.semi.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("email")
public class EmailController {
	
	private final EmailService emailService;

	@PostMapping("sendEmail")
	@ResponseBody
	public int sendEmail(@RequestBody String inputEmail) { // fetch 로 application/json 형태의 데이터를 보내면서 사실은 body 에 json 형태가 아닌 그냥 데이터 하나만 담아 보내는 경우, 
														   // 이를 받는 컨트롤러에서는 일단, json 형태니까, @RequestBody 로 받고, 그 데이터를 담는 변수의 명은 자유롭게 가능하다. 
		//inputEmail 로 온 건 사용자가 입력한 이메일로서, 이 이메일로 인증번호를 보내줘야 함. 
		String authKey = emailService.sendEmail("signup",inputEmail);
		
		if(authKey != null) {
			// 이메일 전송 중 오류가 발생하지 않았다. + AUTH_KEY insert되던 중 오류가 발생하지 않았다. 
			// 즉, 정상적으로 이메일 보내기 성공했다. 
			return 1;
		}
		
		// 이메일 보내기 실패했다.
		return 0;
	} 
	
	@PostMapping("checkAuthKey")
	@ResponseBody
	public int checkAuthKey(@RequestBody Map<String, String> map) {
		// map.get("email") : 사용자가 입력한 이메일
		// map.get("authKey") : 사용자가 입력한 인증번호
		
		int result = emailService.checkAuthKey(map);
				
		if(result > 0) {
			// 조회된 것이 있다는 뜻 -> 사용자가 입력한 인증번호와 AUTH_KEY 테이블에 저장되어 있는 AUTH_KEY 컬럼값이 일치한다.
			// 즉, 인증번호가 일치한다.
			return 1;
		}
		
		//인증번호가 일치하지 않는다.		
		return 0;
	}
	
}