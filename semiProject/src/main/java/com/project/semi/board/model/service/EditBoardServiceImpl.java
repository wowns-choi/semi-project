package com.project.semi.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.dto.BoardImg;
import com.project.semi.board.model.exception.BoardInsertException;
import com.project.semi.board.model.mapper.EditBoardMapper;
import com.project.semi.common.util.Utility;

import lombok.RequiredArgsConstructor;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class EditBoardServiceImpl implements EditBoardService {

	private final EditBoardMapper mapper;

	@Value("${my.board.web-path}")
	private String webPath;
	
	@Value("${my.board.folder-path}")
	private String folderPath;
	
	@Override
	public int boardInsert(Board inputBoard, List<MultipartFile> images) throws IllegalStateException, IOException {

		// 게시글 제목, 내용 먼저 INSERT
		int result = mapper.boardInsert(inputBoard);
		
		// INSERT 실패 시
		if(result == 0) {
			return 0;
		}
		
		// SQL 문 실행 후 얕은 복사로 inputBoard 에 boardNo 저장된 상태
		int boardNo = inputBoard.getBoardNo();
		
		List<BoardImg> uploadList = new ArrayList<>();
		
		for(int i = 0 ; i < images.size() ; i++) {
			// images 가져올 때 빈 칸도 가져와짐 존재하는지 검사
			if(!images.get(i).isEmpty()) {
				String originalName = images.get(i).getOriginalFilename();
				String rename = Utility.fileRename(originalName);
				
				BoardImg img = BoardImg.builder()
						.imgOriginalName(originalName)
						.imgRename(rename)
						.imgPath(webPath)
						.boardNo(boardNo)
						.imgOrder(i)
						.uploadFile(images.get(i))
						.build();
				
				uploadList.add(img);
			}
		}
		
		// 이미지가 없다면 제목, 내용만 보여줄 거
		if(uploadList.isEmpty()) {
			return boardNo;
		}
		
		// 이미지가 있다면 이미지 넣어주기
		result = mapper.insertUploadList(uploadList);
		
		// 다중 INSERT 성공 확인
		if(result == uploadList.size()) {
			
			for(BoardImg img : uploadList) {
				img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
			}
		} else {
			throw new BoardInsertException("이미지가 정상 삽입되지 않음");
		}
		
		return boardNo;
	}

	@Override
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) {

		int result = mapper.boardUpdate(inputBoard);
		
		return 0;
	}
}
