package com.project.semi.board.model.service;

import java.util.Map;

import com.project.semi.board.model.dto.Board;

public interface BoardService {

	/** 자유 게시판 페이지 목록 조회
	 * @param cp
	 * @return map
	 */
	Map<String, Object> selectBoardList(int cp);

	/** 자유 게시판 게시글 상세 조회
	 * @param map
	 * @return board
	 */
	Board selectOne(Map<String, Integer> map);

	/** 게시글 좋아요 체크/해제
	 * @param map
	 * @return count
	 */
	int boardLike(Map<String, Integer> map);

}
