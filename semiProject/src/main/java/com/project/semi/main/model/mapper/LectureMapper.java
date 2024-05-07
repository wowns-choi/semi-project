package com.project.semi.main.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureReview;
import com.project.semi.member.model.dto.Member;

@Mapper
public interface LectureMapper {
	
	List<Lecture> findLecturesLatest();

	Lecture findLectureDetail(String lectureNo);

	List<LectureReview> findReview(String lectureNo);

	int addReview(LectureReview lectureReview);

	int updateReview(Map<String, String> map);

	int updateReview2(Map<String, String> map);

	int addReviewReply(LectureReview lectureReivew);

	int replyUpdate(Map<String, Object> paramMap);

	int replyUpdate2(Map<String, Object> paramMap);

	List<Map<String, Object>> selectCategoryTypeList();

	List<Lecture> selectList(int lectureCategoryNum);

	int getListCount(Map<String, Object> map);

	List<Lecture> selectLectureList(Map<String, Object> map, RowBounds rowBounds);

	int getSelectCount(Map<String, Object> map);

	List<Lecture> selectCategoryList(Map<String, Object> map, RowBounds rowBounds);
	
	Integer checkRestNum(Map<String, Object> paramMap);

	int deleteReview(String lectureReviewNo);

}

