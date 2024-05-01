package com.project.semi.management.model.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.project.semi.common.util.Utility;
import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.management.model.mapper.ManagementMapper;
import com.project.semi.register.model.dto.RegisterDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource("classpath:/config.properties")
public class ManagementServiceImpl implements ManagementService {
	
	private final ManagementMapper managementMapper;
	
	@Value("${my.profile.web-path}")
	private String webPath;
	
	@Value("${my.profile.folder-path}")
	private String folderPath;
	
	@Override
	public void showMyLectures(int memberNo, String page, Model model) {
		
		// 여기서 6가지 챙겨놔야함. 
		
		// 2. 현재 페이지
		Integer currentPage = Integer.parseInt(page);
		
		// 3. 총 페이지 수 
		// 몇 개의 강의가 있는지 확인 
		 int totalLectures = managementMapper.countMyLecture(memberNo);
		 // 한 페이지당 12개의 게시물이 보여지도록 할 것 
		 int pageSize = 12;
		 // 총 페이지수 
		 int totalPages = (int) Math.ceil((double)totalLectures/pageSize);
		 
		 // 6. 그룹당 페이지의 개수 
		 Integer pageGroupSize = 5;
		 
		 // 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		 // 현재 페이지가 속한 페이지그룹부터 계산해야함 
		 Integer currentGroup = (int) Math.ceil((double) currentPage/pageGroupSize);
		 Integer currentGroupFirstPage = (currentGroup-1) * pageGroupSize + 1;
		 
		 // 5. 현재 페이지가 속한 페이지그룹의 마지막 페이지
		 Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		 
		 // 1. 현재 페이지에 보여질 게시글(PostsDTO) 을 담은 List 자료구조
		 Integer startRow = (currentPage - 1)*pageSize;		 
		 
		 Map<String, Integer> paramMap = new HashMap<>();
		 paramMap.put("memberNo", memberNo);
		 paramMap.put("startRow", startRow);
		 paramMap.put("pageSize", pageSize);
		 
		 List<Lecture> lectureList = managementMapper.showMyLectures(paramMap);
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalLectures", totalLectures);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("lectureList", lectureList);
		 
		
		

		
		
	}

	@Override
	public int findOwner(Integer lectureNo, Integer memberNo) {
		
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("memberNo", memberNo);
		
		
		return managementMapper.findOwner(paramMap);
	}

	@Override
	public Lecture findLectureAllData(Integer lectureNo) {

		Lecture lecture = managementMapper.findLectureAllData(lectureNo);
		
		log.debug("lecture", lecture.toString());
		
		return lecture;
	}

	@Override
	public int updateLecture(RegisterDTO register) throws IllegalStateException, IOException {
		// 업데이트될 테이블 목록
		// 1. lecture
		// 2. lecture_file
		// 3. lecture_address
		// 4. lecture_map 이렇게 4가지임. 
		
		// 순서대로 1. lecture
		int result1 = managementMapper.updateLecture(register);
		List<Integer> lectureFileLectureNoList = managementMapper.findLectureNos(register.getLectureNo());
		
		log.debug("aaaaaaaaaaaaaaaaa여기야!!={}", lectureFileLectureNoList.get(0));
		log.debug("aaaaaaaaaaaaaaaaa여기야!!={}", lectureFileLectureNoList.get(1));
		log.debug("aaaaaaaaaaaaaaaaa여기야!!={}", lectureFileLectureNoList.get(2));
		log.debug("aaaaaaaaaaaaaaaaa여기야!!={}", lectureFileLectureNoList.get(3));
		log.debug("aaaaaaaaaaaaaaaaa여기야!!={}", lectureFileLectureNoList.get(4));
		
		// 현재 lectureFileLectureNoList 라는 List자료구조에는 기존에 데이터베이스에 저장되어있던 해당 강의와
		// 관련된 이미지가 저장되어있는 테이블인 Lecture_FILE 테이블의 LECTURE_FILE_NO 이 0인덱스부터4인덱스까지 순서대로 들어있음. 
		
		// 그래서, 따져줄거임. 
		// 현재, 사용자가 이미지를 바꾸지 않겠다고 한 경우, NULL 이 들어와있고, 바꾸겠다고 한 경우 새로운 파일이 들어와있을것.
		
		if(!register.getMain().isEmpty()) {
			String fileRename = Utility.fileRename(register.getMain().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getMain().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result = managementMapper.updateLectureFile(lectureFile);
			
			lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
			
			int result2 = managementMapper.addLectureFile(lectureFile);
			
			register.getMain().transferTo(new File(folderPath + fileRename));
		}
		
		if(!register.getSub1().isEmpty()) {
			String fileRename = Utility.fileRename(register.getSub1().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub1().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result = managementMapper.updateLectureFile(lectureFile);
			
			lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
			
			int result2 = managementMapper.addLectureFile(lectureFile);
			
			register.getSub1().transferTo(new File(folderPath + fileRename));
		}
		if(!register.getSub2().isEmpty()) {
			String fileRename = Utility.fileRename(register.getSub2().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub2().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result = managementMapper.updateLectureFile(lectureFile);
			
			lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
			
			int result2 = managementMapper.addLectureFile(lectureFile);
			
			register.getSub2().transferTo(new File(folderPath + fileRename));
		}
		if(!register.getSub3().isEmpty()) {
			String fileRename = Utility.fileRename(register.getSub3().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub3().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result = managementMapper.updateLectureFile(lectureFile);
			
			lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
			
			int result2 = managementMapper.addLectureFile(lectureFile);
			
			register.getSub3().transferTo(new File(folderPath + fileRename));
		}
		if(!register.getSub4().isEmpty()) {
			String fileRename = Utility.fileRename(register.getSub4().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub4().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result = managementMapper.updateLectureFile(lectureFile);
			
			lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
			
			int result2 = managementMapper.addLectureFile(lectureFile);
			
			register.getSub4().transferTo(new File(folderPath + fileRename));
		}
		
		//-----------------------------------------------------------------------------------
		// lecture_address
		// 무조건 업데이트
		
		int result = managementMapper.updateLectureAddress(register);
		J
		
		/*
		 * 	private String lecturePostCode;
			private String lectureRoadAddress;
			private String lectureJibunAddress;
			private String lectureDetailAddress;
		 	
		 * 
		 * */
		
		
		
		
		
		
		int result2 = managementMapper.updateLectureFile(register);
		int result3 = managementMapper.updateLectureAddress(register);
		
		// 아래건, letitude 와 hardness 가 들어온 경우에만 그렇게 되도록 바꿔주자
		int result4 = managementMapper.updateLectureMap(register);
		
		
		
		return 0;
	}
	
	// 
	
	

	
	
}
