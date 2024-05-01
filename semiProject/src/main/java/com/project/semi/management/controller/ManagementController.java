package com.project.semi.management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.management.model.service.ManagementService;
import com.project.semi.member.model.dto.Member;
import com.project.semi.register.model.dto.RegisterDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("manage")
public class ManagementController {

	private final ManagementService managementService;
	
	@GetMapping("showMyLectures")
	public String showMyLectures(@SessionAttribute("loginMember") Member loginMember, Model model,
															@RequestParam(value="page", required=false, defaultValue="1") String page
			) {
		// 1. loginMember 에서 memberNo 를 꺼내서, DB 에서 관련된 강의들을 가져와야지. 
		// 이때, 가져올때에 페이지네이션을 진행해야함. 
		int memberNo = loginMember.getMemberNo();
		
		managementService.showMyLectures(memberNo, page, model);
		


		return "/management/myLectures";
	}
	
	@GetMapping("updateLecture")
	public String updateLecture(@RequestParam("lectureNo") Integer lectureNo,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra, Model model
			) {
		
		// 1. 요청한 사용자가 해당 강의의 주인이 맞는지 확인하는 코드
		int result = managementService.findOwner(lectureNo, loginMember.getMemberNo());
		
		if(result == 0) {
			ra.addFlashAttribute("message", "잘못된 접근입니다.");
			return "redirect:/";
		}
		
		// 2. 강의에 대한 정보를 가져와서 수정폼에 뿌려줄건데, 그걸 위해 강의에 대한 정보를 조회해온다.
		Lecture findLecture = managementService.findLectureAllData(lectureNo);
		
		log.debug(findLecture.toString());
		
		model.addAttribute("findLecture", findLecture);		
		
		String startTime = findLecture.getStartTime();
		
		
        String firstTwoChars = startTime.substring(0, 2); // 첫 번째에서 두 번째 문자까지 추출
        String lastTwoChars = startTime.substring(startTime.length() - 2); // 마지막 두 문자 추출

        model.addAttribute("firstTwoChars", firstTwoChars);
        model.addAttribute("lastTwoChars", lastTwoChars);
		
		return "/management/updateForm";
	}
	
	@PostMapping("updateLecture")
	public String updateLecture(@ModelAttribute RegisterDTO register) throws IllegalStateException, IOException{
		
		log.debug("register=={}", register); //lectureNo 을 hidden 타입 input 으로 전달받음. 
		int result = managementService.updateLecture(register);
		
		
		
		
		// 주소는 입력해도 입력하지 않아도 그냥 온 걸 그대로 업데이트 해주면 됨.
		
		
		
		// 위도경도는 null 이 아닌 경우에만 update 해주면 됨. 
		
		
		
		// 파일은?  파일은 현재 어떤 다른 이미지로 바꾼 경우, 그 이미지가 들어오도록 해놨고,
		// 다른 이미지로 바꾸지 않은 경우, 그냥 null 이 들어오도록 해놨다. 
		
		
		
		
		return null;
	}
	
	
}

