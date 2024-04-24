package com.project.semi.main.model.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class LectureReview {

	private Integer lectureReviewNo;	
    private Integer reviewWriteMember;
	private Integer lectureNo;
    private String reviewContent;
    private String reviewImg;
    private String formattedCreateDateReview;
    private Integer parentReviewNo;
    
    private int memberNo;
    private String memberNickname;
    private String profileImg;
    
    private LocalDateTime createDateReview;
    private String stringCreateDate;
    
    private List<LectureReview> replies = new ArrayList<>();
}
