package com.project.semi.register.model.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.project.semi.register.model.dto.RegisterDTO;
import com.project.semi.register.model.dto.RegisterMessage;

public interface LectureRegisterService {

	int addAuth(int memberNo, int randomNum);

	int verifyAuth(Integer memberNo, String authKey);

	int checkPassFlag(Integer memberNo);

	int registerForm(RegisterDTO register)throws IllegalStateException, IOException;

	RegisterMessage selectMessage(int registeredMemberNo);

	RegisterMessage updateMessage(RegisterMessage message, int lectureMemberNo);

	int deleteMessage(int messageNo);

	List<RegisterMessage> selectMessageList();

	int messageCount(Integer memberNo);

	RegisterMessage showMessage(Integer memberNo);

	List<RegisterMessage> showMessageList(Integer memberNo);

	RegisterMessage showMessageHref(int messageNo);

	int onesideDelete(int messageNo);

	int deleteStudent(int messageNo);

}