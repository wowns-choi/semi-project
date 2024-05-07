package com.project.semi.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.admin.model.service.AdminService;
import com.project.semi.member.model.dto.Member;
import com.project.semi.payment.model.dto.Payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("admin")
public class AdminController {
	
	private final AdminService adminService;

	@GetMapping("adminHome")
	public String adminHome() {
		return "/admin/adminHome";
	}
	
	@GetMapping("order")
	public String order(@SessionAttribute("loginMember") Member loginMember,
			@RequestParam(value="page", required=false, defaultValue="1") String page,
			RedirectAttributes ra,
			Model model) {
		
		// 1. 지금 요청 보낸 사람이 admin 이 맞나? 
		Integer memberNo = loginMember.getMemberNo();
		int authority = adminService.findAuthority(memberNo);
		
		if(authority == 1) {
			// 일반 사용자인 경우 
			ra.addFlashAttribute("message", "잘못된 접근입니다. ");
			return "redirect:/";
		}
		
		// 2. 주문내역 리스트를 가져올 것. 페이지네이션 할 거임. 
		adminService.findOrderList(Integer.parseInt(page), model);
		
		
		
		return "/admin/orderPage";
		
	}
	
	@RequestMapping("orderByNameOrTel")
	public String orderByNameOrTel(@SessionAttribute("loginMember") Member loginMember,
			@RequestParam(value="page", required=false, defaultValue="1") String page,
			RedirectAttributes ra,
			@RequestParam("findOption") String findOption,
			@RequestParam("option") String option,
			Model model) {
		
		
		log.debug("!!!!!!@@@@@@@@@@@=={}", findOption);
		log.debug("!!!!!!@@@@@@@@@@@=={}", option);
		log.debug("!!!!!!@@@@@@@@@@@=={}", findOption.equals("memberNickName"));
		
		// 1. 지금 요청 보낸 사람이 admin 이 맞나? 
		Integer memberNo = loginMember.getMemberNo();
		int authority = adminService.findAuthority(memberNo);
		
		if(authority == 1) {
			// 일반 사용자인 경우 
			ra.addFlashAttribute("message", "잘못된 접근입니다. ");
			return "redirect:/";
		}
		
		// 2. 주문내역 리스트를 가져올 것. 페이지네이션 할 거임. 
		adminService.findOrderList(Integer.parseInt(page), model, findOption, option);
		
		model.addAttribute("findOption", findOption);
		model.addAttribute("option", option);
		
		
		return "/admin/orderPageOptionVersion";
	}
	
	
	
	@GetMapping("refund")
	public String refund(@SessionAttribute("loginMember") Member loginMember,
						@RequestParam(value="page", required=false, defaultValue="1") String page,
						RedirectAttributes ra,
						Model model
			) {
		// 1. 지금 요청 보낸 사람이 admin 이 맞나? 
		Integer memberNo = loginMember.getMemberNo();
		int authority = adminService.findAuthority(memberNo);
		
		if(authority == 1) {
			// 일반 사용자인 경우 
			ra.addFlashAttribute("message", "잘못된 접근입니다. ");
			return "redirect:/";
		}
		
		// 2. 환불 리스트를 가져올 것. 페이지네이션 할 거임. 
		adminService.findRefundList(Integer.parseInt(page), model);
		
		return "/admin/refundPage";
		
	}
	
	@GetMapping("payment")
	public String payment(@SessionAttribute("loginMember") Member loginMember,
						RedirectAttributes ra) {
		// 1. 지금 요청 보낸 사람이 admin 이 맞나? 
		Integer memberNo = loginMember.getMemberNo();
		int authority = adminService.findAuthority(memberNo);
		
		if(authority == 1) {
			// 일반 사용자인 경우 
			ra.addFlashAttribute("message", "잘못된 접근입니다. ");
			return "redirect:/";
		}
		
		return "/admin/paymentHome";
	}
	
	@PostMapping("payment")
	public String payment(@RequestParam("merchantUid") String merchantUid, 
						Model model,
						@SessionAttribute("loginMember") Member loginMember,
						RedirectAttributes ra) {
		
		// 1. 지금 요청 보낸 사람이 admin 이 맞나? 
		Integer memberNo = loginMember.getMemberNo();
		int authority = adminService.findAuthority(memberNo);
		
		if(authority == 1) {
			// 일반 사용자인 경우 
			ra.addFlashAttribute("message", "잘못된 접근입니다. ");
			return "redirect:/";
		}
		
		Payment payment = adminService.payment(merchantUid);
		model.addAttribute("payment", payment);
		
		return "/admin/paymentResult";		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
