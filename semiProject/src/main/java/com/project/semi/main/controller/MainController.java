package com.project.semi.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.service.LectureService;
import com.project.semi.member.model.dto.Member;
import com.project.semi.register.controller.CoolSMSController;
import com.project.semi.register.model.service.LectureRegisterService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

	private final LectureService lectureService;
	
	private final LectureRegisterService lectureRegisterService;
	
	@Autowired
	private CoolSMSController ct;
	
	@GetMapping("/")
	public String home(Model model,
			@RequestParam(value="cp", required=false, defaultValue = "1") int cp,
			@RequestParam(value="key", required=false, defaultValue = "전체") String query,
			HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		int messageCount = 0;
		
		Map<String, Object> map = lectureService.viewAll(cp, query);
		
		if(session.getAttribute("loginMember") != null) {
			Member member = (Member)session.getAttribute("loginMember");
			messageCount = lectureRegisterService.messageCount(member.getMemberNo());
		}
		
		model.addAttribute("lectureList", map.get("lectureList"));
		model.addAttribute("mainPagination", map.get("mainPagination"));
		model.addAttribute("query", query);
		session.setAttribute("messageCount", messageCount);
		
		return "common/main";
	}
	
	@GetMapping("/{lectureCategoryNum:[0-9]+}")
	public String select(@PathVariable("lectureCategoryNum") int lectureCategoryNum,
			Model model,
			@RequestParam(value="cp", required=false, defaultValue = "1") int cp,
			@RequestParam(value="key", required=false, defaultValue = "전체") String query) {
		
		
		
		Map<String, Object> map = lectureService.selectView(lectureCategoryNum, cp, query);
		
//		List<Lecture> selectList = lectureService.selectList(lectureCategoryNum);
		model.addAttribute("lectureCategoryNum", lectureCategoryNum);
		model.addAttribute("lectureList", map.get("lectureList"));
		model.addAttribute("mainPagination", map.get("mainPagination"));
		model.addAttribute("query", query);
		
		return "/common/main";
	}
	
}
