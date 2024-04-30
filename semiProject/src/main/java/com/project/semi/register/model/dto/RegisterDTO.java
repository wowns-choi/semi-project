package com.project.semi.register.model.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class RegisterDTO {
	

	private MultipartFile main;
	private MultipartFile sub1;
	private MultipartFile sub2;
	private MultipartFile sub3;
	private MultipartFile sub4;
	
	private Integer lectureNo;
	
	private String lectureHomeTitle;
	private String lectureHomeContent;
	
	private String lectureTitle;
	private String lectureContent;
	
	private String instructor;
	
	private String field;
	
	private String level;
	
	private String startHour;
	private String startMin;
	
	private String startTime;
	
	private String howLong;
	
	private String acceptableNumber;
	
	private String startDate;
	private String endDate;
	
	private String latitude;
	private String hardness;
	
	private String lecturePostCode;
	private String lectureRoadAddress;
	private String lectureJibunAddress;
	private String lectureDetailAddress;
	
	
	private String price;
	
	// 세션에서 꺼내온 값
	private int memberNo;
	
}
