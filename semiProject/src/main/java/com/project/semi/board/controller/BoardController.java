package com.project.semi.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.semi.board.model.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("board")
@RequiredArgsConstructor
@Slf4j
public class BoardController {
	
	private final BoardService service;
	
	@GetMapping("all")
	public String selectBoardList(
			@RequestParam(value="cp", required=false, defaultValue="1") int cp,
			Model model) {
		
		log.debug("현재페이지 : " + cp);
		
		System.out.println(cp);
		
		
		// 자유 게시판 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = service.selectBoardList(cp);
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
//		System.out.println(map.get("pagination"));
		
		return "board/boardList";
	}
}
