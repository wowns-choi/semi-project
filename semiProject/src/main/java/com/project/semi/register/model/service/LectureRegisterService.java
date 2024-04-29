package com.project.semi.register.model.service;

import java.io.IOException;

import com.project.semi.register.model.dto.RegisterDTO;

public interface LectureRegisterService {

	int addAuth(int memberNo, int randomNum);

	int verifyAuth(Integer memberNo, String authKey);

	int checkPassFlag(Integer memberNo);

	int registerForm(RegisterDTO register)throws IllegalStateException, IOException;

}