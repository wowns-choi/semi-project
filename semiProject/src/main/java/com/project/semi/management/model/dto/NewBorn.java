package com.project.semi.management.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.project.semi.main.model.dto.LectureRestnum;

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
public class NewBorn {

	private Integer lectureRestnumNo;
	private Integer lectureNo;
	private String lectureDateStr;
	private Integer restNum;
	
	private List<NewBornBaby> babyList = new ArrayList<>(); 
	
	


	
}