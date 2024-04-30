package com.project.semi.faq.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("faq")
public class FAQController {

	@GetMapping("/lecturer")
	public String faqLecturer() {
		return "faq/faqLecturer";
	}
	
	@GetMapping("/student")
	public String faqStudent() {
		return "faq/faqStudent";
	}
}
