package com.project.semi.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.project.semi.board.model.service.EditBoardService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("editBoard")
@RequiredArgsConstructor
public class EditBoardController {

	private final EditBoardService service;
	
	@GetMapping("insert")
	public String boardInsert() {
		return "board/boardWrite";
	}
}
