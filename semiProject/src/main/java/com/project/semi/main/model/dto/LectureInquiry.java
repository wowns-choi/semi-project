package com.project.semi.main.model.dto;

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
public class LectureInquiry {
	private Integer lectureInquiryNo;	
    private Integer inquiryWriteMember;
    private String inquiryContent;
    private String formattedCreateDateInquiry;
    private Integer parentInquiryNo;
}


