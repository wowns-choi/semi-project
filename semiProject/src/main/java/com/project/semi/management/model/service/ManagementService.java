package com.project.semi.management.model.service;

import java.util.List;

import org.springframework.ui.Model;

import com.project.semi.main.model.dto.Lecture;

public interface ManagementService {

	void showMyLectures(int memberNo, String page, Model model);

	int findOwner(Integer lectureNo, Integer memberNo);

	Lecture findLectureAllData(Integer lectureNo);

}