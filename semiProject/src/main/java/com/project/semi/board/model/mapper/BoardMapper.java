package com.project.semi.board.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.project.semi.board.model.dto.Board;

@Mapper
public interface BoardMapper {

	/** 자유 게시판 삭제되지 않은 게시글 수 조회
	 * @return count
	 */
	int getListCount();

	/** 자유 게시판 목록 조회
	 * @param rowBounds 
	 * @param cp 
	 * @return boardList
	 */
	List<Board> selectBoardList(int cp, RowBounds rowBounds);

	/** 자유 게시판 게시물 상세 조회
	 * @param map
	 * @return board
	 */
	Board selectOne(Map<String, Integer> map);

	/** 좋아요 해제
	 * @param map
	 * @return result
	 */
	int deleteBoardLike(Map<String, Integer> map);

	/** 좋아요 체크
	 * @param map
	 * @return  result
	 */
	int insertBoardLike(Map<String, Integer> map);

	/** 게시글 좋아요 개수 조회
	 * @param integer
	 * @return result
	 */
	int selectLikeCount(Integer integer);

}
