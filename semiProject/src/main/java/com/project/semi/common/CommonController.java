package com.project.semi.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
