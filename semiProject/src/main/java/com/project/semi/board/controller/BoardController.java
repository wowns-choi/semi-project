package com.project.semi.board.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.board.model.service.BoardService;
import com.project.semi.member.model.dto.Member;

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
		
		// 자유 게시판 조회 서비스 호출 후 결과 반환
		Map<String, Object> map = service.selectBoardList(cp);
		
		model.addAttribute("pagination", map.get("pagination"));
		model.addAttribute("boardList", map.get("boardList"));
		
		return "board/boardList";
	}
	
	@GetMapping("all/{boardNo:[0-9]+}")
	public String boardDetail(@PathVariable("boardNo") int boardNo,
			Model model,
			RedirectAttributes ra,
			@SessionAttribute("loginMember") Member loginMember) {
		
		
		return "";
	}
}
