package com.project.semi.management.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.register.model.dto.RegisterDTO;

@Mapper
public interface ManagementMapper {

	List<Lecture> showMyLectures(Map<String, Integer> paramMap);

	int countMyLecture(int memberNo);

	int findOwner(Map<String, Integer> paramMap);

	Lecture findLectureAllData(Integer lectureNo);

	int updateLecture(RegisterDTO register);
	
	List<Integer> findLectureNos(Integer lectureNo);

	int updateLectureFile(RegisterDTO register);

	int updateLectureAddress(RegisterDTO register);

	int updateLectureMap(RegisterDTO register);

	int updateLectureFile(LectureFile lectureFile);

	int addLectureFile(LectureFile lectureFile);




}