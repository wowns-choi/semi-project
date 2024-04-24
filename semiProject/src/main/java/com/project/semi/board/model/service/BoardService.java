package com.project.semi.board.model.service;

import java.util.Map;

public interface BoardService {

	/** 자유 게시판 페이지 목록 조회
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectBoardList(int cp);

}
