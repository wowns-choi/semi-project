package com.project.semi.management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.management.model.service.ManagementService;
import com.project.semi.member.model.dto.Member;

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
	
	
}
