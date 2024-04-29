package com.project.semi.main.model.dto;

import java.util.Date;
import java.util.List;

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
public class Lecture {
    private Integer lectureNo;
    private Integer memberNo;
    
    private Integer lectureCategoryNum;
    
    private String lectureHomeTitle;
    private String lectureHomeContent;
    
    private String lectureTitle;
    private String lectureContent;
    
    private Integer price;

    private String instructorIntroduction;
    
    private Integer lectureLevel;
        
    private String startTime;
    
    private Integer howLong;
    
    private Integer acceptableNumber;
    
    private Integer restNumber;
    
    private String formattedStartDate;
    private String formattedEndDate;
    private String formattedEnrollDate;

    private char lectureDelFl;    

    // 조인해서 가져올 것들
    // 1. LECTURE_FILE
    private List<LectureFile> files; // 관련 파일 정보 리스트
    
    // 2. MEMBER
    private String memberNickname;

    // 3. LECTURE_DAYS
    private Integer dayOfWeek;
    
    // 4. LECTURE_REVIEW
    private List<LectureReview> reviews;

    // 5. LECTURE_INQUIRY
    private List<LectureInquiry> inquirys;
    
    // 6. LECTURE_ADDRESS
    private String postCode;
    private String roadAddress;
    private String jibunAddress;
    private String detailAddress;
    
    // 7. LECTURE_MAP
    private String latitude;
    private String hardness;
    
    
    
    
    
}