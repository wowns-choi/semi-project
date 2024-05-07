package com.project.semi.payment.model.dto;

import java.util.List;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.main.model.dto.LectureInquiry;
import com.project.semi.main.model.dto.LectureRestnum;
import com.project.semi.main.model.dto.LectureReview;

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
public class Order {
	
	private Integer lectureOrdersNo;
	private Integer memberNo;
	private Integer lectureNo;
	private String merchantUid;
	private Integer amount;
	private Integer quantity;
	private String status;
	private String createdStr;
	
	//MEMBER 
	private String memberNickname;
	private String memberTel;
	private String profileImg;
	
	//LECTURE
	private String lectureHomeTitle;
	
}
