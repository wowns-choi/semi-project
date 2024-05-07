package com.project.semi.management.model.dto;

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
public class NewBornBaby {
	
	
	private Integer registeredMemberNo;
	private Integer memberNo;
	private String registeredMemberFl;

	// MEMBER 테이블 
	private String memberNickname;
	private String profileImg;
	
	// LECTURE_ORDERS 테이블
	private Integer quantity; 

}
