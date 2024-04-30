package com.project.semi.management.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.management.model.mapper.ManagementMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
public class ManagementServiceImpl implements ManagementService {
	
	private final ManagementMapper managementMapper;
	
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
	
	

	
	
}
