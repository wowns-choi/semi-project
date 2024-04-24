package com.project.semi.main.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.member.model.dto.Member;

public interface LectureService {

	List<Lecture> findLectures();

	Map<String, Object> findLectureDetail(String lectureNo);

	int addReview(String lectureNo, String reviewContent, MultipartFile reviewImg, Member loginMember);

	int updateReview(String reviewContent2, String wantDeleteImg, String updateReplyNo);

	int addReviewReply(int memberNo, String reviewContent, MultipartFile replyFile, String lectureNo, String parentReviewNo);

}