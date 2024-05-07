package com.project.semi.register.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.register.model.dto.RegisterDTO;

@Mapper
public interface LectureRegisterMapper {

	int addAuth(Map<String, Integer> map);

	int countRow(int memberNo);

	void deleteRow(int memberNo);

	String findAuthKey(Integer memberNo);

	void updatePassFlag(Integer memberNo);

	int checkPassFlag(Integer memberNo);

	int addLecture(RegisterDTO register);

	int addLectureFile(LectureFile lectureFile);

	int addLectureAddress(RegisterDTO register);

	int addLectureMap(RegisterDTO register);

	void addRestNumberPerDate(Map<String, Object> paramMap);


	
	
}

