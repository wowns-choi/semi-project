package com.project.semi.board.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.dto.BoardImg;
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
			RedirectAttributes ra) {
		
		Map<String, Integer> map = new HashMap<>();
		
		map.put("boardNo", boardNo);
		
//		if(loginMember != null) {
//			map.put("memberNo", loginMember.getMemberNo());
//		}
		
		Board board = service.selectOne(map);
		
		String path = null;
		
		// 조회 결과가 없는 경우
		if(board == null) {
			path = "redirect:/board/all";
			ra.addFlashAttribute("message", "게시글이 존재하지 않습니다.");
		} else {
			// 쿠키를 이용한 조회 수 증가
			
			path = "board/boardDetail";
			
			model.addAttribute("board", board);
			
			if(!board.getImageList().isEmpty()) {
				BoardImg thumbnail = null;
				
				if(board.getImageList().get(0).getImgOrder() == 0) {
					thumbnail = board.getImageList().get(0);
				}
				
				model.addAttribute("thumbnail", thumbnail);
				model.addAttribute("start", thumbnail != null ? 1 : 0);
				// 썸네일이 있다면 start == 1, 없다면 start == 0 
			}
		}
		
		return path;
	}
	
	/** 게시글 좋아요 체크/해제
	 * @param map
	 * @return count
	 */
	@ResponseBody
	@PostMapping("like")
	public int boardLike(@RequestBody Map<String, Integer> map) {
		return service.boardLike(map);
	}
}
