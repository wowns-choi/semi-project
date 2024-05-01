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

	int updateLecture(RegisterDTO register) throws IllegalStateException, IOException;

}