package com.project.semi.main.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureReview;
import com.project.semi.main.model.service.LectureService;
import com.project.semi.member.model.dto.Member;

import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/lecture")
@Controller
@RequiredArgsConstructor
@Slf4j
@SessionAttributes({"loginMember"})
public class LectureController {

	private final LectureService lectureService;
	
	@GetMapping("/showLectureDetail")
	public String showLecture(@RequestParam("lectureNo") String lectureNo,
							@RequestParam(value="currentPage", required=false, defaultValue = "1") String currentPage, 
								Model model
			) {
		
		log.debug("currentPage=={}", currentPage);
		
		Map<String, Object> returnMap = lectureService.findLectureDetail(lectureNo, currentPage);
		Lecture lecture = (Lecture) returnMap.get("lecture");
		List<LectureReview> fiveParentReview = (List<LectureReview>) returnMap.get("fiveParentReview");
		Integer totalPosts = (Integer) returnMap.get("totalPosts");
		Integer pageGroupSize = (Integer) returnMap.get("pageGroupSize");
		Integer currentGroupFirstPage = (Integer) returnMap.get("currentGroupFirstPage");
		Integer currentGroupLastPage = (Integer) returnMap.get("currentGroupLastPage");
		
		model.addAttribute("lecture", lecture);
		model.addAttribute("fiveParentReview", fiveParentReview);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPosts", totalPosts);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		
		model.addAttribute("lectureNo", lectureNo);
		
		
		String startTime = lecture.getStartTime();
        String startFirstTwo = startTime.substring(0, 2);  // 0, 1 인덱스 추출
        String startLastTwo = startTime.substring(2, 4);  // 2, 3 인덱스 추출
        
        model.addAttribute("startFirstTwo", startFirstTwo);
        model.addAttribute("startLastTwo", startLastTwo);
        
        Integer lectureLevel = lecture.getLectureLevel();
        String lectureLevelString = "";
        if(lectureLevel == 1) {
        	lectureLevelString = "초급";
        }
        if(lectureLevel == 2) {
        	lectureLevelString = "중급";
        }
        if(lectureLevel == 3) {
        	lectureLevelString = "고급";
        }
        
        
        model.addAttribute("lectureLevelString", lectureLevelString);
        model.addAttribute("lectureNo", lectureNo);

        
		
		
		return "/lecture/detailLecture";
	}
	
	@PostMapping("/addReview")
	public String addReview(@RequestParam("lectureNo") String lectureNo,
							@RequestParam("reviewContent") String reviewContent,
							@RequestParam("reviewImg") MultipartFile reviewImg,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra
			) throws IllegalStateException, IOException{
		
		// log 로 잘 들어온 거 확인함. 
		
		int result = lectureService.addReview(lectureNo, reviewContent, reviewImg, loginMember);

		
		if(result == 0) {
			ra.addFlashAttribute("message", "리뷰 작성중 오류 발생");
			
		} 
		
		return "redirect:/lecture/showLectureDetail?lectureNo=" + lectureNo;
	}
	
	@PostMapping("updateReview")
	public String updateReview(@RequestParam("reviewContent2") String reviewContent2, 
							@RequestParam(value="wantDeleteImg", required=false) String wantDeleteImg,
							@RequestParam("updateReplyNo") String updateReplyNo,
							@RequestParam("lectureNo") String lectureNo,
							RedirectAttributes ra
			) {
		// wantDeleteImg 의 값이 true 면, 해당 댓글의 이미지를 null 로 변경하면 된다. 		
		log.debug("reviewContent2===={}", reviewContent2);
		log.debug("wantDeleteImg===={}", wantDeleteImg);
		log.debug("updateReplyNo", updateReplyNo);
		int result = lectureService.updateReview(reviewContent2, wantDeleteImg, updateReplyNo);
		if(result == 0) {
			ra.addFlashAttribute("message", "리뷰 수정 중 오류 발생");
		} 
		
		log.debug("최재준최재준최재준최재준최재준");
		
		return "redirect:/lecture/showLectureDetail?lectureNo=" + lectureNo;

		
	}
	
	@PostMapping("addReviewReply")
	public String addReviewReply( @SessionAttribute("loginMember") Member loginMember,
								@RequestParam("reviewContent2") String reviewContent,
								@RequestParam("replyFile") MultipartFile replyFile,
								@RequestParam("lectureNo") String lectureNo,
								@RequestParam("parentReviewNo") String parentReviewNo,
								RedirectAttributes ra
			) {
		int memberNo = loginMember.getMemberNo();
		
		int result = lectureService.addReviewReply(memberNo, reviewContent, replyFile, lectureNo, parentReviewNo);
		
		
		String path = null;
		if(result == 0) {
			ra.addFlashAttribute("message", "답글 작성 중 오류 발생");
		} 
		
		return "redirect:/lecture/showLectureDetail?lectureNo=" + lectureNo;
	}
	
	@PostMapping("replyUpdate")
	public String replyUpdate(
			@RequestParam("reviewContent") String reviewContent, 
			@RequestParam(value= "wantDeleteImg",  required=false) String wantDeleteImg,
			@SessionAttribute("loginMember") Member loginMember,
			@RequestParam("lectureNo") String lectureNo,
			@RequestParam("parentReviewNo") String parentReviewNo,
			@RequestParam("lectureReviewNo") String lectureReviewNo,
			RedirectAttributes ra 
			
			) {
		
		log.debug("wantDeleteImg=={}", wantDeleteImg);		
		int memberNo = loginMember.getMemberNo();
	
		int result = lectureService.replyUpdate(reviewContent, wantDeleteImg, memberNo, lectureNo, parentReviewNo, lectureReviewNo);
		
		if(result != 0) { // 제대로 업데이트 됬다. 
			ra.addFlashAttribute("message", "대댓글 수정 중 오류 발생");
		} 
		
		return "redirect:/lecture/showLectureDetail?lectureNo=" + lectureNo;

		
	}

	
}