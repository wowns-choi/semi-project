package com.project.semi.member.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.member.model.dto.Member;
import com.project.semi.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("member")
@SessionAttributes({"loginMember"})
public class MemberController {
	
	
	private final MemberService memberService;
	
	@GetMapping("signUp")
	public String signUp () {
	 	return "member/joinForm";
	}
	
	@GetMapping("checkEmail")
	@ResponseBody
	public String checkEmail(@RequestParam("memberEmail") String memberEmail) {
		// db 에 동일한 이메일로 등록된 member 가 있는지 검사 시작.
		int result = memberService.checkEmail(memberEmail);
		
		if(result > 0) {
			// 중복된 이메일로 회원가입한 member가 있는 경우.
			return "1";
		} 
		return "0";
	}
	
	@PostMapping("signUp")
	public String signUp(@ModelAttribute Member member,
			@RequestParam("memberAddress") String[] memberAddress,
			RedirectAttributes ra
			) {
		
		int result = memberService.signUp(member, memberAddress);
		
		String path = null;
		String message = null;
		
		if(result > 0) {
			// 회원가입 성공
			message = member.getMemberNickname() + "님의 가입을 환영 합니다.";
			path = "/";
		} else {
			// 회원가입 실패
			message = "회원가입 처리 중 오류 발생";
			path = "signUp";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
	
	@PostMapping("login")
	public String login(@RequestParam Map<String,String> map, RedirectAttributes ra, Model model) {
		String saveId = map.get("saveId"); // 아이디 저장 버튼 눌렀으면 on, 안눌렀으면 null
		String memberEmail = map.get("memberEmail"); // 이메일
		String memberPw = map.get("memberPw"); //비밀번호
		
		Member loginMember = memberService.login(memberEmail, memberPw);
		
		if( loginMember == null ) { // 로그인 실패
			ra.addFlashAttribute("message", "아이디 또는 비밀번호가 일치하지 않습니다");
		}
		
		if( loginMember != null ) { // 로그인 성공
			model.addAttribute("loginMember", loginMember); // @SessionAttributes() 와 함께했기에 세션스코프에 올라진것.
		}
		return "redirect:/"; // 메인 페이지 재요청
	}

	@GetMapping("logout")
	public String logout(SessionStatus status) {
		log.debug("logout");
		status.setComplete(); // 클래스레벨에 @SessionAttributes("loginMember") 이거 안쓰면 세션 무효화 안됨.
		return "redirect:/";
	}
	
	@GetMapping("myData")
	public String info(@SessionAttribute("loginMember") Member loginMember,
						Model model) {
		log.debug("imgimgimgimgimgimgimgimg====={}", loginMember.toString());		

		log.debug("imgimgimgimgimgimgimgimg====={}", loginMember.getProfileImg());		
		log.debug("imgimgimgimgimgimgimgimg====={}", loginMember.getProfileImg());		
		log.debug("imgimgimgimgimgimgimgimg====={}", loginMember.getProfileImg());		
		log.debug("imgimgimgimgimgimgimgimg====={}", loginMember.getProfileImg());		
		// 수정 후 다시 이 메서드로 오기 때문에, db 에서 조회해온다. 
		Member member = memberService.findMemberByMemberNo(loginMember.getMemberNo());
		
		// 주소만 꺼내옴
		String memberAddress = member.getMemberAddress();

		// 주소는 필수가 아니었기 때문에, null 일 수 있음. 
		// 따라서, 주소가 있을 경우에만~
		if(memberAddress != null) {
			
			log.debug("memberAddress = {}", memberAddress); //01076^^^서울 강북구 노해로13길 40^^^서울 강북구 수유동 31-67^^^정면 왼쪽 초록색 대문
			
			String[] arr = memberAddress.split("\\^\\^\\^"); // 이스케이프 문자. 왜 이스케이프를 써야 할까? 매개변수로 전달되는 걸 보면, 정규표현식임.
			log.debug("Arrays.toString(arr)={}", Arrays.toString(arr)); //Arrays.toString(arr)=[01076, 서울 강북구 노해로13길 40, 서울 강북구 수유동 31-67, 정면 왼쪽 초록색 대문]

			model.addAttribute("postCode", arr[0]);
			model.addAttribute("roadAddress", arr[1]);
			model.addAttribute("jibunAddress",arr[2]);
			model.addAttribute("detailAddress",arr[3]);
			
		}
		
		// 카카오톡으로 로그인할 경우에는 사업자 등록 못받아서 이메일 주소를 못받았거든? 
		// 그래서 카카오톡 서버가 보내준 데이터 중, 카카오 서버가 사용자마다 부여한 식별키 앞에다 "kakao" 를 붙여
		// MEMBER 테이블의 MEMBER_EMAIL 컬럼에다가 삽입했다. 
		// 따라서, loginMember.getMemberEmail() 값이 kakao_ 로 시작한다면, null 값으로 채워줄것.
		if(member.getMemberEmail() != null) {
			if(member.getMemberEmail().startsWith("kakao_")) {
				member.setMemberEmail(null);
			}
		}
		model.addAttribute("member", member);
		
		return "member/memberUpdateForm";
	}

	@PostMapping("myData")
	public String updateInfo(@ModelAttribute Member inputMember, 
			@RequestParam("memberAddress")String[] memberAddress,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra ) {
		
		log.debug(Arrays.toString(memberAddress)); //[01076, 서울 강북구 노해로13길 40, 서울 강북구 수유동 31-67, 정면 왼쪽 초록색 대문]
		
		int memberNo = loginMember.getMemberNo();
		int result = memberService.updateInfo(inputMember, memberNo, memberAddress);
		
		if(result > 0) {
			// 수정 성공할 경우
			ra.addFlashAttribute("message", "성공적으로 수정되었습니다.");
		} else {
			// 수정 실패
			ra.addFlashAttribute("message", "수정 중 오류 발생");
		}
		
		return "redirect:/member/myData";
	}
	
	
	@PostMapping("updateMyImg")
	public String updateImg(@RequestParam("profileImg") MultipartFile profileImg,
					@SessionAttribute("loginMember") Member loginMember,
					RedirectAttributes ra
			) throws Exception {
		
		log.debug("profileImg={}", profileImg);
		
		int result = memberService.updateImg(profileImg, loginMember);
		
		String message = null;
		
		if( result > 0 ) {
			message = "이미지가 변경되었습니다.";
		} else { 
			message = "이미지 변경 중 오류 발생";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:/member/myData";
	}
	
	


}
