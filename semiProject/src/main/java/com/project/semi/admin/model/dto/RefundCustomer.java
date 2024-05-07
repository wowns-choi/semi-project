package com.project.semi.admin.model.dto;

import com.project.semi.member.model.dto.KakaoTokenResponseDTO;

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
public class RefundCustomer {

	private Integer refundCustomerNo;
	private Integer lectureNo;
	private String merchantUid;
	private Integer refundCustomerMemberNo;
	private Integer refundAmount;
	private String refundReason;
	private String refundStatus;
	private String refundRequestDateStr;
	private String refundProcessingDateStr;
	
	private String memberNickname; // 환불 해줄 고객의 이름 
	private String memberTel; // 전화번호
	private String profileImg; // 프로필 이미지
	
	private String lectureHomeTitle; // 강의 제목	
	private Integer lecturerMemberNo;  // 강사의 MEMBER 테이블 MEMBER_NO 
	
}
