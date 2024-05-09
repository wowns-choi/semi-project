package com.project.semi.register.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.register.model.dto.RegisterDTO;
import com.project.semi.register.model.dto.RegisterMessage;

@Mapper
public interface LectureRegisterMapper {

	int addAuth(Map<String, Integer> map);

	int countRow(int memberNo);

	void deleteRow(int memberNo);

	String findAuthKey(Integer memberNo);

	void updatePassFlag(Integer memberNo);

	int checkPassFlag(Integer memberNo);

	int addLecture(RegisterDTO register);

	int addLectureFile(LectureFile lectureFile);

	int addLectureAddress(RegisterDTO register);

	int addLectureMap(RegisterDTO register);

	void addRestNumberPerDate(Map<String, Object> paramMap);

	RegisterMessage selectMessage(int registeredMemberNo);

	int insertMessage(RegisterMessage message);

	int updateMessage(RegisterMessage message);

	int deleteMessage(int messageNo);

	List<RegisterMessage> selectMessageList();

	int messageCount(Integer memberNo);

	RegisterMessage showMessage();

	RegisterMessage showMessage(Integer memberNo);

	int updateCheckMessage(Integer memberNo);

	List<RegisterMessage> showMessageList(Integer memberNo);

	RegisterMessage showMessageHref(int messageNo);

	int updateShowMessage(int messageNo);


	
	
}

