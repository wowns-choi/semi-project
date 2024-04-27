package com.project.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.dto.BoardImg;

@Mapper
public interface EditBoardMapper {

	/** 게시글 작성 (BOARD 테이블 INSERT)
	 * @param inputBoard
	 * @return result
	 */
	int boardInsert(Board inputBoard);

	/** 게시글 이미지 삽입
	 * @param uploadList
	 * @return result
	 */
	int insertUploadList(List<BoardImg> uploadList);

	/** 게시글 제목, 내용 수정
	 * @param inputBoard
	 * @return result
	 */
	int boardUpdate(Board inputBoard);

}
