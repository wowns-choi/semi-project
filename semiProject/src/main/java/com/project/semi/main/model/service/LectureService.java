package com.project.semi.main.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.member.model.dto.Member;

public interface LectureService {

	List<Lecture> findLectures();

	Map<String, Object> findLectureDetail(String lectureNo, String page);

	int addReview(String lectureNo, String reviewContent, MultipartFile reviewImg, Member loginMember) throws IllegalStateException, IOException;

	int updateReview(String reviewContent2, String wantDeleteImg, String updateReplyNo);

	int addReviewReply(int memberNo, String reviewContent, MultipartFile replyFile, String lectureNo, String parentReviewNo) ;

	int replyUpdate(String reviewContent, String wantDeleteImg, int memberNo, String lectureNo,
			String parentReviewNo, String lectureReviewNo);

	/** 카테고리 리스트 조회
	 * @return categoryTypeList
	 */
	List<Map<String, Object>> selectCategoryTypeList();

	/** 카테고리 별 리스트 조회
	 * @param categoryNo
	 * @return selectList
	 */
	List<Lecture> selectList(int lectureCategoryNum);

	Map<String, Object> viewAll(int cp, String query);

	Map<String, Object> selectView(int lectureCategoryNum, int cp, String query);

}