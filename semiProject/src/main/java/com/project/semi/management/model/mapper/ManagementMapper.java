package com.project.semi.management.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.main.model.dto.Lecture;

@Mapper
public interface ManagementMapper {

	List<Lecture> showMyLectures(Map<String, Integer> paramMap);

	int countMyLecture(int memberNo);

}
