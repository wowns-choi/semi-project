package com.project.semi.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.dto.Pagination;
import com.project.semi.board.model.mapper.BoardMapper;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardMapper mapper;

	// 자유 게시판 페이지 목록 조회
	@Override
	public Map<String, Object> selectBoardList(int cp) {

		// 자유 게시판에서 삭제되지 않은 게시글 수 조회
		int listCount = mapper.getListCount();
		
		// listCount + cp 이용해서 Pagination 객체 생성
		Pagination pagination = new Pagination(cp,listCount);
		
		int limit = pagination.getLimit();
		int offset = (cp-1)*limit;
		RowBounds rowBounds = new RowBounds(offset, limit);
		
		List<Board> boardList = mapper.selectBoardList(cp, rowBounds);
		
		// 목록 조회 결과 + Pagination 객체를 Map 으로 묶어서 돌려줌
		Map<String, Object> map = new HashMap<>();
		
		map.put("pagination", pagination);
		map.put("boardList", boardList);
		
		return map;
	}

	// 자유 게시판 게시글 상세 조회
	@Override
	public Board selectOne(Map<String, Integer> map) {
		return mapper.selectOne(map);
	}

	@Override
	public int boardLike(Map<String, Integer> map) {

		int result = 0;
		
		if(map.get("likeCheck") == 1) {
			
			result = mapper.deleteBoardLike(map);
			
		} else {
			
			result = mapper.insertBoardLike(map);
			
		}

		if(result > 0) {
			return mapper.selectLikeCount(map.get("boardNo"));
		}
		
		return -1;

	}
}
