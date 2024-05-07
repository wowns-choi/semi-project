package com.project.semi.main.model.dto;

import java.time.LocalDateTime;
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
public class LectureRestnum {
	
	private Integer lectureRestnumNo;
	private Integer lectureNo;
	private String lectureDateStr;
	private Integer restNum;
	
	
	
	
}
