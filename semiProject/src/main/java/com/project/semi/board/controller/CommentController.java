package com.project.semi.board.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.semi.board.model.dto.Comment;
import com.project.semi.board.model.service.CommentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

	private final CommentService service;
	
	/** 댓글 목록 조회
	 * @param boardNo
	 * @return commentList
	 */
	@GetMapping("")
	public List<Comment> select(@RequestParam("boardNo") int boardNo) {
		return service.select(boardNo);
	}
	
	/** 댓글/답글 등록
	 * @param comment
	 * @return result
	 */
	@PostMapping("")
	public int insert(@RequestBody Comment comment) {
		return service.insert(comment);
	}
	
	/** 댓글/답글 수정
	 * @return result
	 */
	@PutMapping("")
	public int update(@RequestBody Comment comment) {
		return service.update(comment);
	}
	
	/** 댓글/답글 삭제
	 * @param commentNo
	 * @return result
	 */
	@DeleteMapping("")
	public int delete(@RequestBody int commentNo) {
		return service.delete(commentNo);
	}
}
