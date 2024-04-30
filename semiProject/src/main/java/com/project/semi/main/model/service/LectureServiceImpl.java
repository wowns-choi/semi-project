package com.project.semi.main.model.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.common.util.Utility;
import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.main.model.dto.LectureReview;
import com.project.semi.main.model.mapper.LectureMapper;
import com.project.semi.member.model.dto.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService{

	private final LectureMapper lectureMapper;
	
	@Value("${my.profile.web-path}")
	private String webPath; // 앞에 붙이는 조각
	
	@Value("${my.profile.folder-path}")
	private String folderPath; //찐 저장소
	
	@Override
	public List<Lecture> findLectures() {
		List<Lecture> lectureList = lectureMapper.findLecturesLatest();
		
		for(Lecture lec : lectureList) { //이 for 문으로 인해, 연관된 이미지가 없는 게시글은 files 라는 List자료구조를 담은 필드에 null 이 담기게 되었음. 
			log.debug(lec.toString());
			List<LectureFile> lectureFileList = lec.getFiles();
			if(lectureFileList.get(0).getLectureFileNo() == null) {
				lec.setFiles(null);
			}
		}
		
		return lectureList;
	}

	@Override
	public Map<String,Object> findLectureDetail(String lectureNo, String page) {
		Lecture lecture = lectureMapper.findLectureDetail(lectureNo);
		
		List<LectureReview> list = lectureMapper.findReview(lectureNo); // 최신순 6개 조회.
		
		// 현재 list 에는 해당 강의에 달린 대댓글을 포함한 모든 댓글이 담겨져 있다. 
		
		// 1. 댓글이 몇분전에 쓰여졌는지 계산해서 필드에 담아둔다.
        for (LectureReview review : list) {
            LocalDateTime createDate = review.getCreateDateReview();
            // 댓글들에 들어가있는 2024-04-20T23:20:10 이런 형태의 데이터를 가지고,
            // 현재 시각으로부터 얼마나 이전에 쓰여진 댓글인지를 구해서,
            // stringCreateDate 라는 필드에 담아줄 것이다.

            // 현재 시각 구하기
            LocalDateTime now = LocalDateTime.now();

            // "댓글이 생성된 시각" 과 "현재 시각" 의 차이를
            // 년도단위로, 월단위로, 일단위로, 시간단위로, 분단위로 계산.
            long years = ChronoUnit.YEARS.between(createDate, now);
            long months = ChronoUnit.MONTHS.between(createDate, now);
            long days = ChronoUnit.DAYS.between(createDate, now);
            long hours = ChronoUnit.HOURS.between(createDate, now);
            long minutes = ChronoUnit.MINUTES.between(createDate, now);

            // timeAgeStr 이라는 String 타입 변수를 하나 만들고,
            // 조건에 따라 다른 문자열이 바인딩 되도록 한다.
            String timeAgoStr;
            if (years > 0) {
                timeAgoStr = years + "년 전";
            } else if (months > 0) {
                timeAgoStr = months + "개월 전";
            } else if (days > 0) {
                timeAgoStr = days + "일 전";
            } else if (hours > 0) {
                timeAgoStr = hours + "시간 전";
            } else if (minutes > 0) {
                timeAgoStr = minutes + "분 전";
            } else {
                timeAgoStr = "방금 전";
            }

            // 이렇게 만들어진, timeAgeStr 을 해당 CommentDTO 타입 객체의
            // stringCreateDate 라는 필드에 담는다.
            review.setStringCreateDate(timeAgoStr);
        }
        
        // 2. reviewMap 을 이용하면, LectureReviw 객체의 lectureReviewNo 필드 값만 알면
        // 해당 LectureReview 객체가 뭔지 알 수 있음. 
        Map<Integer, LectureReview> reviewMap = new ConcurrentHashMap<>();
        for(LectureReview review : list) {
        	reviewMap.put(review.getLectureReviewNo(), review);
        }
        
        
        // 부모 리뷰에 자식 리뷰를 바인딩한다. 
        for (LectureReview review : list) {
        	if(review.getParentReviewNo() != null) {
        		LectureReview parent =reviewMap.get(review.getParentReviewNo());
        		if(parent != null) {
        			parent.getReplies().add(review);
        			
        		}
        	}
        }
        
        // 부모 리뷰만 골라내서 parentReviews 라는 List자료구조에 담아두었다. 
        List<LectureReview> parentReviews = new ArrayList<>();
        for (LectureReview review : list) {
        	if(review.getParentReviewNo() == null) {
        		parentReviews.add(review);
        	}
        }
        
        // parentReviews 에서 현재 보여질 review 들만 골라내야 함. 
        Integer currentPage = Integer.parseInt(page); // 현재 페이지
        Integer pageSize = 5; // 한 페이지당 리뷰 개수
        Integer totalPosts = parentReviews.size(); // 전체 review 개수
        Integer pageGroupSize = 5; // 페이지그룹당 페이지 수
        // 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
        // 1) 현재 페이지가 속한 페이지그룹 구하기
        Integer currentGroup = (int)Math.ceil((double) currentPage/pageGroupSize);
        // 2) 현재 페이지가 속한 페이지그룹의 첫번째 페이지 구하기 
        Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;
        // 현재 페이지가 속한 그룹의 마지막 페이지 구하기 
        // 그 전에, 마지막 페이지를 구해야 함. 
        Integer totalPages = (int)Math.ceil((double)totalPosts/pageSize);
        Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
        
        Integer startRow = (currentPage - 1) * pageSize; 
        
        
        List<LectureReview> fiveParentReview = new ArrayList<>();
        
        for(int i = startRow; i < Math.min(parentReviews.size(), startRow + pageSize); i++) {
        	LectureReview review = parentReviews.get(i);
        	fiveParentReview.add(review);
        }
        
        // 현재 fiveParentReview 에는 5개의 부모리뷰가 들어있음. 당연히 자식리뷰가 바인딩되어있음.
        
        //---------------------------------------------------------------
        
        Map<String, Object> returnMap = new ConcurrentHashMap<String, Object>();
        returnMap.put("lecture", lecture);
        returnMap.put("fiveParentReview", fiveParentReview); // 1
        //returnMap.put("currentPage", currentPage); // 2 컨트롤러에서 파라미터로 받은 거 쓰려고함. 
        returnMap.put("totalPosts", totalPosts); //3
        returnMap.put("pageGroupSize", pageGroupSize); // 6
        returnMap.put("currentGroupFirstPage", currentGroupFirstPage); // 4
        returnMap.put("currentGroupLastPage", currentGroupLastPage); //5
        
        
        
        
        for(LectureReview review : fiveParentReview) {
        	log.debug("aaaaaaaaaaaaaaaaaaaaa====={}", review.getMemberNo());
        }
        
        

		return returnMap;
	}

	@Override
	public int addReview(String lectureNo, String reviewContent, MultipartFile reviewImg, Member loginMember) throws IllegalStateException, IOException {
		int memberNo = loginMember.getMemberNo();
		
		String updatePath = null;
		String rename = null;
		
		if(!reviewImg.isEmpty()) {
			rename = Utility.fileRename(reviewImg.getOriginalFilename());
			
			updatePath = webPath + rename;
		}
		
		LectureReview lectureReivew = LectureReview.builder()
				.reviewWriteMember(memberNo)
				.reviewContent(reviewContent)
				.reviewImg(updatePath)
				.lectureNo(Integer.parseInt(lectureNo))
				.build();
				
		int result = lectureMapper.addReview(lectureReivew);
		
		if(result > 0) {
			reviewImg.transferTo(new File(folderPath + rename));
		}
		
		
		return result;
	}

	@Override
	public int updateReview(String reviewContent2, String wantDeleteImg, String updateReplyNo) {
		
		int result = 0;
		Map<String, String> map = new ConcurrentHashMap<String, String>();
		map.put("reviewContent2", reviewContent2);
		map.put("updateReplyNo", updateReplyNo);
		
		if(wantDeleteImg != null) {
			result = lectureMapper.updateReview(map);
		}else {
			result = lectureMapper.updateReview2(map);
		}
		
		return result;
		
	}

	@Override
	public int addReviewReply(int memberNo, 
				String reviewContent, 
				MultipartFile replyFile, 
				String lectureNo, 
				String parentReviewNo) {
		
		String updatePath = null;
		String rename = null; 
		
		if(!replyFile.isEmpty()) {
			rename = Utility.fileRename(replyFile.getOriginalFilename());
			
			updatePath = webPath + rename;
		}
		
		LectureReview lectureReivew = LectureReview.builder()
				.memberNo(memberNo)
				.reviewContent(reviewContent)
				.reviewImg(updatePath)
				.lectureNo(Integer.parseInt(lectureNo))
				.parentReviewNo(Integer.parseInt(parentReviewNo))
				.build();
		
		
		log.debug(lectureReivew.toString());
		
		return lectureMapper.addReviewReply(lectureReivew);
		
		
		
		

	}

	@Override
	public int replyUpdate(String reviewContent, String wantDeleteImg, int memberNo, String lectureNo,
			String parentReviewNo, String lectureReviewNo) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("reviewContent", reviewContent);
		paramMap.put("wantDeleteImg", wantDeleteImg);
		paramMap.put("memberNo", memberNo);
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("lectureReviewNo", lectureReviewNo);
		
		int result = 0;
		
		if(wantDeleteImg != null) {
			result = lectureMapper.replyUpdate(paramMap);
		}else {
			result = lectureMapper.replyUpdate2(paramMap);
		}
		
		
		
		
		return 0;
	}
	
	
	
}