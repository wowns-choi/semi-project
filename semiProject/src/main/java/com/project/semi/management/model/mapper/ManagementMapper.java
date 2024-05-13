package com.project.semi.management.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.main.model.dto.LectureRestnum;
import com.project.semi.management.model.dto.RegisteredMember;
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

	int deleteLectureFile(Integer lectureFileNo);



	// ----------------------------------------------
	
	List<RegisteredMember> findRegisteredMembers(String lectureNo);

	List<LectureRestnum> findLectureRestNums(String lectureNo);

	Integer findMemberNo(String lectureNo);

	void updateFlag(Map<String, String> paramMap);

	Integer checkLecturerMemberNo(String lectureNo);

	int deleteLecture(String lectureNo);

	List<Integer> findRegisteredMember(Map<String, Object> paramMap);

	void updateFlagSpecific(Map<String, Object> paramMap);

	void minusFeeSettlement(Map<String, Integer> paramMap);

	String findMerchantUid(Integer registeredMemberNo);

	void addRefundCustomer(Map<String, Object> paramMap);

	
	

	




}