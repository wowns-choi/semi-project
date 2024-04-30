package com.project.semi.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.service.LectureService;
import com.project.semi.register.controller.CoolSMSController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

	private final LectureService lectureService;
	
	@Autowired
	private CoolSMSController ct;
	
	@GetMapping("/")
	public String home(Model model
			) {
		
		
		//lecture 테이블과 그 테이블과 연관된 이미지들을 가져와서 model 에 잘 담아줘야 함. 
		
		
		List<Lecture> lectureList = lectureService.findLectures();
		
		//Lecture(lectureNo=1, 
		//memberNo=20, 
		//lectureName=나만의 향수 만들기, 
		//lectureTitle=나의 향기를 칠해봐요, 
		//lectureContent=향기로운 사람이 되고 싶다구요? 왜요? 왜죠? , 
		//enrollDate=Thu Apr 18 03:06:12 YAKT 2024, 
		//lectureDelFl=N, 
		//price=30000, 
		//files=[LectureFile(lectureFileNo=1, lectureNo=1, filePath=/lecture/file, originalName=perfume1.jpg, rename=perfume1.jpg, uploadDate=Thu Apr 18 03:22:15 YAKT 2024), 
		//LectureFile(lectureFileNo=2, lectureNo=1, filePath=/lecture/file, originalName=perfume2.jpg, rename=perfume2.jpg, uploadDate=Thu Apr 18 04:33:19 YAKT 2024)])
		model.addAttribute("lectureList", lectureList);
		
		
		
		return "common/main";
	}
	
	
	
}
