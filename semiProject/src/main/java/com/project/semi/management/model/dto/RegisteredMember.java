package com.project.semi.management.model.dto;

import java.util.List;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.main.model.dto.LectureInquiry;
import com.project.semi.main.model.dto.LectureRestnum;
import com.project.semi.main.model.dto.LectureReview;
import com.project.semi.member.model.dto.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder // 인스턴스 쉽게 만들게해줌.

public class RegisteredMember {
	
	private Integer registeredMemberNo;
	private Integer memberNo;
	private Integer lectureNo;
	private String lectureDateStr;
	private String merchantUid;
	
	private String registeredMemberFl;
	private Integer quantity; 

	// MEMBER 테이블 
	private String memberNickname;
	private String profileImg;
	
	
	

}
