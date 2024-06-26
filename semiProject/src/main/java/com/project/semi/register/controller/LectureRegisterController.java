package com.project.semi.register.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.member.model.dto.Member;
import com.project.semi.register.model.dto.RegisterDTO;
import com.project.semi.register.model.dto.RegisterMessage;
import com.project.semi.register.model.service.LectureRegisterService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.http.PUT;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("register")
public class LectureRegisterController {
	
	
	private final LectureRegisterService lectureRegisterService;
	

	@GetMapping("idVerification")
	public String idVerification(@SessionAttribute("loginMember") Member loginMember) {
		
		// REGISTER_AUTH 테이블에 행이 있고, 그 행의 FLAG 가 1 이라면 바로 /register/registerForm 으로 리다이렉트
		int result = lectureRegisterService.checkPassFlag(loginMember.getMemberNo());
		
		if(result > 0) {
			return "redirect:/register/registerForm";
		}
		
		return "/register/idVerification";
		
	}
	
	@GetMapping("registerForm")
	public String registerForm(@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra
			) {
		
		// 여기서, 지금 get 요청한놈이 REGISTER_AUTH 라는 테이블에 PASS_FLAG 가 1인 행을 소유하고 있는지 확인
		int result =lectureRegisterService.checkPassFlag(loginMember.getMemberNo());
		
		if(result > 0) {
			// 가지고 있는 경우 
			return "/register/registerForm";
			
			
		}else {
			// 가지고 있지 않은 경우 == url 로 비정상접근한 경우 
			ra.addFlashAttribute("message","잘못된 접근입니다");
			return "redirect:/register/idVerification";
		}

	}
	
	@GetMapping("aa")
	public String regi() {
		return "/register/registerForm";
	}
	
	@PostMapping("registerForm")
	public String registerForm(@ModelAttribute RegisterDTO register,
							@SessionAttribute("loginMember") Member loginMember
			) throws IllegalStateException, IOException{

		/* main=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@15faf8e4, 
		 * sub1=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@23e15338, 
		 * sub2=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@fac355, 
		 * sub3=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@4c810aa0, 
		 * sub4=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@efef60c, 
		 * lectureHomeTitle=홈 제목11, 
		 * lectureHomeContent=홈 내용11, 
		 * lectureTitle=상세 제목11, 
		 * lectureContent=상세 내용11, 
		 * instructor=강사 최재준 나를 소개하지, 
		 * field=3, 
		 * level=2, 
		 * startHour=09, 
		 * startMin=10, 
		 * howLong=6, 
		 * acceptableNumber=15, 
		 * startDate=2024-06-12, 
		 * endDate=2024-07-11, 
		 * latitude=37.6395131290992, 
		 * hardness=127.020326608826, 
		 * lecturePostCode=01076, 
		 * lectureRoadAddress=서울 강북구 노해로13길 40, 
		 * lectureJibunAddress=서울 강북구 수유동 31-67, 
		 * lectureDetailAddress=정면 왼쪽 초록색 대문
		 * 
		 * */
		
		// 이 데이터를 db 에 insert 해줄 것. 
		
		register.setMemberNo(loginMember.getMemberNo());
		
		int result = lectureRegisterService.registerForm(register);
		
		
		
		return "redirect:/";
	}
	
	@ResponseBody
	@PostMapping("selectMessage")
	public RegisterMessage selectMessage(@RequestBody int registeredMemberNo) {
		
		RegisterMessage message = lectureRegisterService.selectMessage(registeredMemberNo);

		if(message == null) {
			RegisterMessage temp = new RegisterMessage();
			return temp;
		}
		
		return message;
	}
	
	@ResponseBody
	@PostMapping("update")
	public RegisterMessage updateMessage(@RequestBody RegisterMessage message,
			@SessionAttribute("loginMember") Member loginMember) {
		
		int lectureMemberNo = loginMember.getMemberNo();
		return lectureRegisterService.updateMessage(message, lectureMemberNo);

	}
	
	@ResponseBody
	@DeleteMapping("delete")
	public int deleteMessage(@RequestBody int messageNo) {
		return lectureRegisterService.deleteMessage(messageNo);
	}
	
	@ResponseBody
	@GetMapping("showMessage")
	public RegisterMessage showMessage(@SessionAttribute("loginMember") Member loginMember) {
		return lectureRegisterService.showMessage(loginMember.getMemberNo());
	}
	
	@ResponseBody
	@GetMapping("showMessageList")
	public List<RegisterMessage> showMessageList(@SessionAttribute("loginMember") Member loginMember) {
		return lectureRegisterService.showMessageList(loginMember.getMemberNo());
	}
	
	@ResponseBody
	@GetMapping("showMessageHref")
	public RegisterMessage showMessage(@RequestParam(value="messageNo", defaultValue="0") int messageNo) {
		return lectureRegisterService.showMessageHref(messageNo);
	}
	
	@ResponseBody
	@PostMapping("onesideDelete")
	public int onesideDelete(@RequestBody int messageNo) {
		return lectureRegisterService.onesideDelete(messageNo);
	}
	
	/** 학생 쪽 메세지 삭제 경우
	 * @param messageNo
	 * @return result
	 */
	@ResponseBody
	@PostMapping("deleteStudent")
	public int deleteStudent(@RequestBody int messageNo) {
		return lectureRegisterService.deleteStudent(messageNo);
	}
}
