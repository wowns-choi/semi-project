package com.project.semi.email.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.project.semi.email.model.mapper.EmailMapper;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
	
	// EmailConfig.class 에서 수동으로 등록한 빈이 주입됨.
	// 이 빈(객체)에 대한 설정은 EmailConfig.java 에서 해줌. 
	private final JavaMailSender mailSender;
	
	private final SpringTemplateEngine templateEngine;
	
	private final EmailMapper emailMapper;
	
	/**
	 * 이메일을 보내주는 기능을 하는 서비스계층 메서드.
	 */
	@Override
	public String sendEmail(String htmlName, String inputEmail) {
		
		// htmlName 용도 : 이메일을 보낼 때에는, html 파일을 이메일로 보내는 건데, 
		// 				  내가 가진 파일 중 어떤 html 파일을 이메일로 보낼건지를 파라미터로 받도록 해놨음. 
		
		// inputEmail 의미 : inputEmail 은 사용자가 입력한 이메일로서, 지금 발송할 이메일이 도착할 이메일 주소를 의미한다.
		
		// --------------------------- 시작 -------------------------------
		// 1) 이메일 보내기
		// 1-1) 난수 생성
		String authKey = createAuthKey(); // 이메일 인증의 내용으로 보낼 난수 6자리 생성.
		
		try {
		// 1-2) 보낼 메일의 제목 정하기
			String subject = null;
		
			switch(htmlName) { // 어떤 html 을 이메일로 보낼것인지에 따라, 제목을 달리함.
			case "signup" :
				subject = "[CHee : Mi] 회원 가입 인증번호 입니다.";
				break;
			case "findPwAuthKey" :
				subject = "[CHee : Mi] 비밀번호 찾기 인증번호 입니다.";
			}
			
		// 1-3) 메일 보내기
			// 1-3-1) jakarta.mail.internet.MimeMessage : 자바에서 메일보낼 때 쓰는 객체를 만들때 씀.
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			// 1-3-2) MimeMessageHelper : spring 에서 제공하는 메일발송 도우미. 
			// 이걸 이용하면 좀 더 간단히 메일을 보낼 수 있다.
			// MimeMessageHelper 타입 객체를 만들 때에 넣어준 파라미터들은 뭐냐면, 
			// 1번 파라미터 : MimeMessage
			// 2번 파라미터 : 이메일 보낼때 이미지 파일 같은 파일을 첨부할 것인지를 나타냄. true 이므로 파일첨부하겠다는 뜻.
			// 3번 파라미터 : 이메일의 문자 인코딩.
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			
			// 1-3-3) 받는 사람 이메일 지정
			helper.setTo(inputEmail);
			// 1-3-4) 이메일 제목 지정
			helper.setSubject(subject);
			
			// 1-3-5) CID(Content-ID) 를 이용해서 메일에 이미지 첨부할 수 있음.
			// 보낼 html 파일에서 이 이미지를 사용하려면, <img src="cid:logo"> 이런식으로 쓰면 됨. 
			// 파일을 넣는 게 아니라, html 파일에 그려질 이미지를 첨부한다는거야.
			helper.addInline("logo", new ClassPathResource("static/images/logo.png"));
			
			// 1-3-6) helper 의 setText 메서드를 호출하면서 넘겨준 파라미터의 의미.
			// 1번 파라미터 : 메일로 보낼 문자열. loadHtml 이라는 메서드를 실행하면, 메일로 보낼 문자열이 리턴되야함. 
			// 2번 파라미터 : HTML 코드를 HTML 코드로 인식할 것인지, 그냥 문자로 인식할 것인지 여부.
			helper.setText(loadHtml(authKey, htmlName), true);
			
			// 1-3-7) 메일 보내기. 특이한 건, helper 를 파라미터로 넘겨주는 게 아니라, MimeMessage타입 객체를 넘겨줌.
			// MimeMessageHelper 의 setTo, setSubject, addInline, setText 메서드를 호출하면, 
			// MimeMessageHelper 타입 객체를 만들 때 넘겨줬던, MimeMessage 타입 객체의 내부 데이터를 변경하는 방식으로 동작함.
			mailSender.send(mimeMessage);
			
		}catch(Exception e) {
			e.printStackTrace();
			return null; // 이메일 보내다가 예외가 나면, null 을 리턴하도록 함. 
		}
		
		
		// 2) AUTH_KEY 라는 테이블에 방금 생성되어 이메일로 보낸 인증번호(authKey)와 사용자의 이메일을 삽입
		// 마이바티스 xml 파일에는 데이터를 하나밖에 못주는데, 전달할 데이터가 2개인 경우
		// DTO 를 만들던지, 한번밖에 안쓰는 경우라면 그냥 Map 자료구조를 쓰면 됨. 
		
		Map<String, String> map = new HashMap<>();
		map.put("authKey", authKey);
		map.put("inputEmail", inputEmail);
		
		// 2-1) 
		// 흐름이 어떻게 될거냐면, 
		// 일단 업데이트를 시도함. 
		int result = emailMapper.updateAuthKey(map);
		
		// 이때, 1이 반환된 경우, 업데이트가 됬다는 거고, 사용자가 인증번호 발급하기 버튼을 처음 누른게 아니라는 
		// => 이 경우, 업데이트가 잘됬으므로, 더 이상 해줄 건 없음. 
		
		// 근데, 0이 반환된 경우, 업데이트가 안됬다는 거고, 사용자가 해당 이메일로 인증번호 발급하기 버튼을 처음 누른 거라는 뜻.
		// => 이 경우, insert 쿼리로 행을 삽입한다. 
		if(result == 0) {
			result = emailMapper.insertAuthKey(map);
		}
		
		if(result == 0) {
			// 여기까지 왔는데도, result 가 0 인 경우 insert 과정 중 오류가 발생했음을 의미.
			return null;
		}
		
		
		return authKey;
		
	}

	
	/** HTML 파일을 읽어와서 String 으로 변환해주는 메서드(타임리프 적용)
	 * @param authKey
	 * @param htmlName
	 * @return
	 */
	private String loadHtml(String authKey, String htmlName) {
		// org.thymeleaf.Context
		// Context 타입 객체는 타임리프가 적용된 HTML 상에서 사용할 값을 세팅할 수 있는 객체이다.
		Context context = new Context();
		
		// 타임리프가 적용된 HTML 에서 사용할 값(authKey : 난수) 추가.
		context.setVariable("authKey", authKey);  
		
		// .process 메서드를 호출하면서 넘겨준 파라미터는 어떤 의미일까? 
		// 1번 파라미터 : html 파일의 경로다. 
		// 2번 파라미터 : 그 html 파일에 th:text = "${변수1}" 이런 코드가 있다고 했을 때, 
		// 거기에 바인딩할 데이터를 담아준 org.thymeleaf.Context 타입 객체를 의미한다. 
		// 그래서, .process 를 호출하면서, html 파일과 그 html 파일에 바인딩될 데이터를 넘겨주면, 
		// html 파일을 "문자열 형태"로 변환하여 반환하게 된다. 
		return templateEngine.process("email/" + htmlName, context);   
	}


	/** 난수 생성 메서드
	 * @return String <- 난수 6자리
	 */
	private String createAuthKey() {
		String key = "";
		for(int i=0 ; i< 6 ; i++) {
            int sel1 = (int)(Math.random() * 3); // 0:숫자 / 1,2:영어
            if(sel1 == 0) {
            	int num = (int)(Math.random() * 10); // 0~9
            	key += num;
            }else {
            	char ch = (char)(Math.random() * 26 + 65); // A~Z
            	int sel2 = (int)(Math.random() * 2); // 0:소문자 / 1:대문자
            	if(sel2 == 0) {
            		ch = (char)(ch + ('a' - 'A')); // 대문자로 변경
            	}
            	key += ch;
            }
		}
		return key;
	}


	/** 인증번호가 일치하는지 검사하는 메서드.*/
	@Override
	public int checkAuthKey(Map<String, String> map) {
		return emailMapper.checkAuthKey(map);
	}
	
	
	

}