package com.project.semi.member.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.member.model.dto.Member;
import com.project.semi.member.model.mapper.MemberMapper;
import com.project.semi.member.model.service.GetKakaoTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})
public class KakaoController {

	private final GetKakaoTokenService getKakaoTokenService;
	private final MemberMapper memberMapper;
	
	@GetMapping("/get-auth-code")
	public String getAuthCode(@RequestParam("code") String code, 
			Model model, HttpServletRequest request, RedirectAttributes ra) {
		
		// code : 카카오 서버가 내려준 인가코드 
		// log.debug("code={}", code);
		Map<String, String> returnMap = getKakaoTokenService.getKakaoToken(code);
		if (returnMap.get("signatureError") != null || returnMap.get("decodingError") != null) {
			//서명이 일치하지 않았거나, 도착한 JWT 디코딩하다가 에러가 난 경우
			ra.addFlashAttribute("error", "카카오 로그인 중 오류발생");
			return "redirect:/";
		}

		// 뭘 해줘야 하냐면, 이미 이전에 카카오톡으로 로그인해서 회원가입이 된 경우가 있는지 여부에 따라 다르게 처리를 해줘야함. 
		
		
		
		// 그래서, 
		// 1) 최초로 카카오톡으로 로그인 한 경우 => 회원가입을 진행시켜줘야 함.
		// 그럼, 이메일(아이디역할), 비밀번호, 이름 만 insert 할건데, 
		// 현재, 사업자등록 안해서 이메일 정보를 얻을 수 없잖아? 
		// 근데, 이메일 정보가 필요한 이유가 아이디나 비밀번호 분실시 등록한 이메일로 보내주기 위함이라면, 
		// 카카오톡으로 로그인한 유저에게 그럴 필요는 없잖아? 카카오톡 아이디/비밀번호 분실했다면, 카카오톡서버에게 요청하면 되잖아. 
		// 따라서, 현재 카카오서버가 준 sub(사용자별 부여한 고유키) 를 이용하면 되겠다. 
		// 아이디 : "kakao_" + sub
		// 비밀번호 : sub
		// 이름 : nickname
		// 2) 최초 로그인이 아닌 경우 회원가입 진행시키지 않을거임. 
		// 어떻게 확인하지? MEMBER 테이블에서 조회해야지. 
        String sub = returnMap.get("sub");
        
        String memberEmail = "kakao_" + sub;
        String memberPw = sub;
        String memberNickname = returnMap.get("nickname");
        
        // MEMBER 테이블에서 조회를 해야하는데, 뭘로 조회할거임?
        // memberPw 로 조회하면 될거 같은데? 
        Member findMember = memberMapper.findMemberByPw(memberPw);
        

		if (findMember != null) {
			// 동일한 아이디가 있는 경우 == 이전에 카톡으로 로그인해서 내 애플리케이션에 회원가입이 되어 있는 경우
			// 세션객체에 로그인한 사용자의 데이터를 담아준다.
			model.addAttribute("loginMember",findMember); // 이거 model 을 세션스코프로 쓴거임.
			return "redirect:/";

		} else{
			// 동일한 아이디가 없는 경우 == 내 애플리케이션에서 처음으로 카톡 로그인한 경우
			// 회원가입 진행 + 세션객체에 로그인한 사용자의 데이터를 담아준다.
			// 나머지 핸드폰번호라든지, 주소같은 경우, 필요한 경우 받도록 null 값이 들어가도록 함. 
			Member member = new Member();
			member.setMemberEmail(memberEmail);
			member.setMemberPw(memberPw);
			member.setMemberNickname(memberNickname);
			int result = memberMapper.signUp(member);
			
			if(result > 0) {
				// 제대로 insert 된 경우
				Member findMember2 = memberMapper.findMemberByPw(memberPw);
				model.addAttribute("loginMember", findMember2);
				ra.addFlashAttribute("message", memberNickname + "님 가입을 환영합니다.");				
			} else {
				// insert 과정 중 오류 발생
				ra.addFlashAttribute("message", "카카오톡 아이디로 회원가입 진행 중 오류 발생");
			}
			return "redirect:/";
		}
		
	}
}