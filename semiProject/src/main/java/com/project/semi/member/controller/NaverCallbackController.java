package com.project.semi.member.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.member.model.dto.Member;
import com.project.semi.member.model.mapper.MemberMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@SessionAttributes({"loginMember"})
public class NaverCallbackController {
	
	private final MemberMapper memberMapper;

	@GetMapping("/naverCallback")
	public String naverCallback() {
		return "/member/naverLogin";
	}
	
	@PostMapping("/sendMemberNaverData")
	@ResponseBody
	public int getMemberNaverData(@RequestBody Map<String, String> map, HttpServletRequest request,
			RedirectAttributes ra, Model model) {
		
		String memberNickname = map.get("name"); // 네이버로 로그인한 사용자의 이름
		String memberEamil = map.get("email"); // 네이버로 로그인한 사용자의 네이버 이메일
		String id = map.get("id"); // 네이버 서버가 사용자별로 부여한 고유키 
		
		Member findMember = memberMapper.findMemberByEmail(memberEamil);
				
		if(findMember != null) {
			// 물론, 이메일로만 판단할 수는 없겠지만, 이메일이 동일한 행이 member 테이블에 존재한다면,
			// 적어도 그 사람은 네이버서버가 준 정보로 회원가입을 진행시키면 안됨. 
			return 1;
			
		}else {
			// 동일한 이메일이 없다면, 우리 애플리케이션에 "직접적으로"(내가 만든 회원폼을 이용해서 가입한 경우를 말함)
			// 회원가입한 적이 없다고 판단. 
			// 그렇다면, 네이버로 로그인해서 회원가입한적이 있는지 따져봐야함. 
			// 네이버서버가 준 데이터로 회원가입 진행을 시킬 때, 비밀번호를 네이버 서버가 준 id 값으로 할 것이므로,
			// 비밀번호가 현재 id 값과 동일한 행을 member 테이블에서 찾는다면, 
			// 네이버로 로그인해서 회원가입한적이 있는 사람인지가 구별되겠네?
			
			Member findMember2 = memberMapper.findMemberByPw(id);
			
			if(findMember2 != null) {
				// 네이버로 로그인해서 회원가입한적이 있는 사람인 경우
				model.addAttribute("naver", "true");
				model.addAttribute("loginMember", findMember2); // 세션스코프로 넣은거임.
				return 2;
			} else {
				// 네이버로 로그인해서 회원가입한적이 없는 사람인 경우
				// 회원가입 진행시킨다.
				Member member = new Member();
				member.setMemberEmail(id);
				member.setMemberPw(id);
				member.setMemberNickname(memberNickname);
				int result = memberMapper.signUp(member);

				
				Member findMember3 = memberMapper.findMemberByPw(id);
				model.addAttribute("naver", "true");
				model.addAttribute("loginMember", findMember3);  // 세션스코프로 넣은거임.
				
				if(result > 0) {
					// 정상적으로 회원가입이 된 경우
					return 3;
				} else {
					// 회원가입 도중 에러 발생
					return 4;
				}
			}
		}
		
		
	}
}