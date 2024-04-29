package com.project.semi.board.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.service.BoardService;
import com.project.semi.board.model.service.EditBoardService;
import com.project.semi.member.model.dto.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class EditBoardController {

	private final EditBoardService service;
	
	private final BoardService bs;
	
	@GetMapping("insert")
	public String boardInsert() {
		return "board/boardWrite";
	}
	
	@PostMapping("insert")
	public String boardInsert(
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra) throws IllegalStateException, IOException {
		
		// inputBoard 에는 제목, 내용 넘어옴
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		// 글쓰기 버튼 눌렀을 때 BOARD 테이블에 새로운 행 삽입되는데
		// 시퀀스 넘버로 BOARD_NO 생성됨 (반환 받을 값)
		int boardNo = service.boardInsert(inputBoard, images);
		
		String path = null;
		String message = null;
		
		if(boardNo > 0) {
			path = "/board/all/" + boardNo;
			message = "게시글이 작성 되었습니다.";
		} else {
			path = "insert";
			message = "게시글 작성 실패";
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	// /editBoard/all/2002/update?cp=1
	@GetMapping("all/{boardNo:[0-9]+}/update")
	public String boardUpdate(
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			Model model,
			RedirectAttributes ra,
			@RequestParam(value="cp", required=false, defaultValue = "1") int cp) {
		
		Map<String, Integer> map = new HashMap<>();
		map.put("boardNo", boardNo);
		
		Board board = bs.selectOne(map);
		
		String message = null;
		String path = null;
		
		if(board == null) {
			message = "해당 게시글이 존재하지 않습니다.";
			path = "redirect:/"; // 메인페이지
			
			ra.addFlashAttribute("message", message);
		} else if(board.getMemberNo() != loginMember.getMemberNo()) {
			message = "자신이 작성한 글만 수정할 수 있습니다.";
			
			// 해당 글 상세조회 리다이렉트 /board/all/2002?cp=1
			path = "redirect:/board/all" + boardNo;
			
			ra.addFlashAttribute("message", message);
		} else {
			// 해당 글 존재 + 글쓴이 (수정하겠다.)
			path = "board/boardUpdate"; // templates/board/boardUpdate.html 로 forward
			model.addAttribute("board", board);
		}
		
		return path;
		
	}
	
	// /editBoard/all/2002/update POST방식
	@PostMapping("all/{boardNo:[0-9]+}/update")
	public String boardUpdate(
			@PathVariable("boardNo") int boardNo,
			@ModelAttribute Board inputBoard,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("images") List<MultipartFile> images,
			RedirectAttributes ra,
			@RequestParam(value="deleteOrder", required = false) String deleteOrder,
			@RequestParam(value="queryString", required = false, defaultValue="") String queryString
			) throws IllegalStateException, IOException {
		
		inputBoard.setBoardNo(boardNo);
		inputBoard.setMemberNo(loginMember.getMemberNo());
		
		int result = service.boardUpdate(inputBoard, images, deleteOrder);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			message = "게시글이 수정되었습니다.";
			path = "/board/all/" + boardNo + queryString;
		} else {
			message = "수정 실패";
			path = "update"; // 수정 화면 전환 상태로 redirect하는 상대 경로
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
	}
	
	@GetMapping("all/{boardNo:[0-9]+}/delete")
	public String boardDelete(
			@PathVariable("boardNo") int boardNo,
			@SessionAttribute("loginMember") Member loginMember,
			RedirectAttributes ra,
			@RequestParam(value="cp", required=false, defaultValue = "1") int cp) {
		
		Board board = Board.builder()
				.boardNo(boardNo)
				.memberNo(loginMember.getMemberNo())
				.build();
		
		// UPDATE
		int result = service.boardDelete(board);
		
		String message = null;
		String path = null;
		
		if(result > 0) {
			// 삭제 성공 시 
			message = "게시글이 삭제되었습니다.";
			path = "/board/all";
		} else {
			message = "삭제 실패";
			path = String.format("/board/all/%d?cp=%d", boardNo, cp);
		}
		
		ra.addFlashAttribute("message", message);
		
		return "redirect:" + path;
		
	}
}
