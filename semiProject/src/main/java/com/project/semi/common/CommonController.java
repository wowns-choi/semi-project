package com.project.semi.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

// Footer -> 이용약관 , 개인정보보호방침, 고객센터
@Controller
@RequestMapping("common")
public class CommonController {

	@GetMapping("dldyddirrhks")
	public String dldyddirrhks() {
		
		return "common/dldyddirrhks";
	}
	
	@GetMapping("rodlswjdqh")
	public String rodlswjdqh() {
		
		return "common/rodlswjdqh";
	}
	
	@GetMapping("rhrortpsxj")
	public String rhrortpsxj() {
		
		return "common/rhrortpsxj";
	}

}

