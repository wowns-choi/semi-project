package com.project.semi.board.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.board.model.dto.Board;
import com.project.semi.board.model.dto.BoardImg;
import com.project.semi.board.model.exception.BoardInsertException;
import com.project.semi.board.model.exception.ImageDeleteException;
import com.project.semi.board.model.exception.ImageUpdateException;
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
	public int boardUpdate(Board inputBoard, List<MultipartFile> images, String deleteOrder) throws IllegalStateException, IOException {

		int result = mapper.boardUpdate(inputBoard);
		
		if(result == 0) return 0;
		
		// 삭제된 이미지가 있을 경우 (deleteOrder)
		if(deleteOrder != null && !deleteOrder.equals("")) {
			Map<String, Object> map = new HashMap<>();
			map.put("deleteOrder", deleteOrder);
			map.put("boardNo", inputBoard.getBoardNo());
			
			result = mapper.deleteImage(map);
		}
		
		// 삭제 실패시
		if(result == 0) {
			throw new ImageDeleteException();
		}
		
		// 선택한 파일이 존재할 경우
		//    해당 파일 정보만 모아두는 List 생성
		List<BoardImg> uploadList = new ArrayList<>();
				
		// images 리스트에서 하나씩 꺼내어 선택된 파일이 있는지 검사
		for(int i = 0 ; i < images.size() ; i++) {
					
			// 실제 선택된 파일이 존재하는 경우
			if( !images.get(i).isEmpty() ) {
						
				// 원본명
				String originalName = images.get(i).getOriginalFilename();
						
				// 변경명
				String rename = Utility.fileRename(originalName);
						
				// IMG_ORDER == i (인덱스 == 순서)
						
				// 모든 값을 저장할 DTO 생성 (BoardImg - Builder 패턴 사용)
				BoardImg img = BoardImg.builder()
								.imgOriginalName(originalName)
								.imgRename(rename)
								.imgPath(webPath)
								.boardNo(inputBoard.getBoardNo())
								.imgOrder(i)
								.uploadFile(images.get(i))
								.build();
						
				uploadList.add(img);
						
				// 업로드 하려는 이미지 정보(img)를 이용해서
				// 수정 또는 삽입 수행
						
				// 1) 기존에 있을 때 -> 새 이미지로 변경하면 -> 수정 성공
				result = mapper.updateImage(img);
						
				if(result == 0) {
					// 수정 실패 == 기존 해당 순서(IMG_ORDER)에 이미지가 없었음
					// (행이 없으면) -> 삽입 수행
					// 일치하는 행이 있으면 업데이트 해라 라고 명령을 보내서
					// 행이 없으면 0 이 반환됨
							
					// 2) 기존에 없었던 애 -> 새 이미지 추가
					result = mapper.insertImage(img);
				}
			}
					
			// 수정 또는 삽입이 실패한 경우
			if(result == 0) {
				throw new ImageUpdateException(); // 예외 발생 -> 롤백
			}
					
		}
				
		// 선택한 파일이 없을 경우
		if(uploadList.isEmpty()) {
			return result;
		}
				
		// 수정, 새 이미지 파일을 서버에 저장
		for(BoardImg img : uploadList) {
			img.getUploadFile().transferTo(new File(folderPath+img.getImgRename()));
		}
				
		return result;
		
	}

	// 게시글 삭제
	@Override
	public int boardDelete(Board board) {
		return mapper.boardDelete(board);
	}
}
