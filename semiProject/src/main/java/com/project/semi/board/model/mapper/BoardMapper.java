package com.project.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	/** 자유 게시판 삭제되지 않은 게시글 수 조회
	 * @return count
	 */
	int getListCount();

	/** 자유 게시판 목록 조회
	 * @return boardList
	 */
	List<Board> selectBoardList();

}
