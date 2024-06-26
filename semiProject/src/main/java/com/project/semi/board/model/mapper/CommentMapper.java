package com.project.semi.board.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.board.model.dto.Comment;

@Mapper
public interface CommentMapper {

	/** 댓글 목록 조회
	 * @param boardNo
	 * @return commentList
	 */
	List<Comment> select(int boardNo);

	/** 댓글/답글 등록
	 * @param comment
	 * @return result
	 */
	int insert(Comment comment);

	/** 댓글/답글 수정
	 * @param comment
	 * @return result
	 */
	int update(Comment comment);

	/** 댓글/답글 삭제
	 * @param commentNo
	 * @return result
	 */
	int delete(int commentNo);

}
