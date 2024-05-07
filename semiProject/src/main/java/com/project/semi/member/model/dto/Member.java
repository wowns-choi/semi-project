package com.project.semi.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
public class Member {
	private Integer memberNo;
	private String memberEmail;
	private String memberPw;
	private String memberNickname;
	private String memberTel;
	private String memberAddress;
	private String profileImg;
	private String enrollDate; //오라클에서는 Date 타입이었으나, String 타입으로 가져올거임. 
	private String memberDelFl;
	private int authority; // 관리자인지 여부를 따지는 컬럼임. 
	
	
}
