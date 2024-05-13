package com.project.semi.management.model.service;

import java.io.IOException;
import java.util.List;

import org.springframework.ui.Model;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.register.model.dto.RegisterDTO;

public interface ManagementService {

	void showMyLectures(int memberNo, String page, Model model);

	int findOwner(Integer lectureNo, Integer memberNo);

	Lecture findLectureAllData(Integer lectureNo);

	int updateLecture(RegisterDTO register, 
			String mainFlag,
			String sub1Flag,
			String sub2Flag,
			String sub3Flag,
			String sub4Flag
			) throws IllegalStateException, IOException;

	void findRegisteredMemberData(String lectureNo, Model model);

	Integer findMemberNo(String lectureNo);

	Integer updateFlag(String lectureNo, String lectureDate, String memberNo, String quantity);

	int checkLecturerMemberNo(String lectureNo, Integer memberNo);

	int deleteLecture(String lectureNo);

	Integer minusFeeSettlement(String lectureNo, String quantity);

	void addRefundCustomer(String lectureNo, String memberNo, Integer minusAmount, Integer registeredMemberNo);


}