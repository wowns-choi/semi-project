package com.project.semi.register.model.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.common.util.Utility;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.register.model.dto.RegisterDTO;
import com.project.semi.register.model.dto.RegisterMessage;
import com.project.semi.register.model.mapper.LectureRegisterMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class LectureRegisterServiceImpl implements LectureRegisterService{

	private final LectureRegisterMapper lectureRegisterMapper;
	
	@Value("${my.profile.web-path}")
	private String webPath;
	
	@Value("${my.profile.folder-path}")
	private String folderPath;
	

	@Override
	public int addAuth(int memberNo, int randomNum) {
		
		int result = lectureRegisterMapper.countRow(memberNo);
		
		if(result > 0) {
			lectureRegisterMapper.deleteRow(memberNo);			
		}
		
		
		Map<String, Integer> map = new HashMap<>();
		map.put("memberNo", memberNo);
		map.put("randomNum", randomNum);
		
		
		return lectureRegisterMapper.addAuth(map);
	}

	@Override
	public int verifyAuth(Integer memberNo, String authKey) {
		
		String findAuthKey= lectureRegisterMapper.findAuthKey(memberNo);
		
		log.debug("findAuthKey=={}", findAuthKey);
		log.debug("authKey=={}", authKey);
		
		int result = 0;
		
		if(findAuthKey.equals(authKey)) {
			
			lectureRegisterMapper.updatePassFlag(memberNo);
			result = 1;
		}
		
		
		return result;
	}

	@Override
	public int checkPassFlag(Integer memberNo) {
		return lectureRegisterMapper.checkPassFlag(memberNo);
	}


	
	@Override
	public int registerForm(RegisterDTO register) throws IllegalStateException, IOException {
		
		/* main=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@15faf8e4, 
		 * sub1=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@23e15338, 
		 * sub2=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@fac355, 
		 * sub3=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@4c810aa0, 
		 * sub4=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@efef60c, 
		 * 
		 * --- 위에까지가 LECTURE_FILE 테이블에 삽입해야 하는 것들. 
		 * 
		 * lectureHomeTitle=홈 제목11, 
		 * lectureHomeContent=홈 내용11, 
		 * lectureTitle=상세 제목11, 
		 * lectureContent=상세 내용11, 
		 * instructor=강사 최재준 나를 소개하지, 
		 * field=3, 
		 * level=2, 
		 * startHour=09, 
		 * startMin=10, 
		 * howLong=6, 
		 * acceptableNumber=15, 
		 * startDate=2024-06-12, 
		 * endDate=2024-07-11, 
		 * 
		 * -- 위에까지가 LECTURE 테이블에 넣어야 할 것들.
		 * 
		 * lecturePostCode=01076, 
		 * lectureRoadAddress=서울 강북구 노해로13길 40, 
		 * lectureJibunAddress=서울 강북구 수유동 31-67, 
		 * lectureDetailAddress=정면 왼쪽 초록색 대문
		 * 
		 * -- 위에까지가 LECTURE_ADDRESS 테이블에 넣어야 할 것들 
		 * 
		 * latitude=37.6395131290992,
		 * hardness=127.020326608826,
		 * 
		 * -- 위에까지가 LECTURE_LOCATION
		 * 
		 * */
		
		register.setStartTime(register.getStartHour() + register.getStartMin());

		
		// 1. 일단, lecture 테이블에 행을 삽입하고 그 행의 기본키로 쓰인 시퀀스값을 가져올거임.
		int result = lectureRegisterMapper.addLecture(register);
		int lectureNo = register.getLectureNo(); // sequence 값(lectureNo값) 얻어옴. 
		
		// 2. 파일 넣기
		if(!register.getMain().isEmpty()) {
			
			String fileRename = Utility.fileRename(register.getMain().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setLectureNo(lectureNo);
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getMain().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result2 = lectureRegisterMapper.addLectureFile(lectureFile);
			
			register.getMain().transferTo(new File(folderPath + fileRename));
			
		}
		if(!register.getSub1().isEmpty()) {
			
			String fileRename = Utility.fileRename(register.getSub1().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setLectureNo(lectureNo);
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub1().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result2 = lectureRegisterMapper.addLectureFile(lectureFile);
			
			register.getSub1().transferTo(new File(folderPath + fileRename));
			
		}
		if(!register.getSub2().isEmpty()) {
			
			String fileRename = Utility.fileRename(register.getSub2().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setLectureNo(lectureNo);
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub2().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result2 = lectureRegisterMapper.addLectureFile(lectureFile);
			
			register.getSub2().transferTo(new File(folderPath + fileRename));
			
		}
		if(!register.getSub3().isEmpty()) {
			
			String fileRename = Utility.fileRename(register.getSub3().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setLectureNo(lectureNo);
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub3().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result2 = lectureRegisterMapper.addLectureFile(lectureFile);
			
			register.getSub3().transferTo(new File(folderPath + fileRename));
			
		}
		if(!register.getSub4().isEmpty()) {
			
			String fileRename = Utility.fileRename(register.getSub4().getOriginalFilename());
			LectureFile lectureFile = new LectureFile();
			lectureFile.setLectureNo(lectureNo);
			lectureFile.setFilePath(webPath);
			lectureFile.setOriginalName(register.getSub4().getOriginalFilename());
			lectureFile.setFileRename(fileRename);
			
			int result2 = lectureRegisterMapper.addLectureFile(lectureFile);
			
			register.getSub4().transferTo(new File(folderPath + fileRename));
			
		}
		/*
		 * lecturePostCode=01076, 
		 * lectureRoadAddress=서울 강북구 노해로13길 40, 
		 * lectureJibunAddress=서울 강북구 수유동 31-67, 
		 * lectureDetailAddress=정면 왼쪽 초록색 대문
		 * 
		 * -- 위에까지가 LECTURE_ADDRESS 테이블에 넣어야 할 것들 
			이 부분 넣어야 함. 
		*/
		
		int result10 = lectureRegisterMapper.addLectureAddress(register);
		
		
		/*
		 * latitude=37.6395131290992,
		 * hardness=127.020326608826,
		 * 
		 * -- 위에까지가 LECTURE_LOCATION
		 * 
		 * */
		
		int result11 = lectureRegisterMapper.addLectureMap(register);
		
		
		
		/* LECTURE_
		 * */
		log.debug("register.get=={}", register.getStartDate()); //2024-05-05
		log.debug("register.get=={}", register.getEndDate()); //2024-05-23
		
		// 몇일간 하는 강의인지 계산 
		LocalDate startDate = LocalDate.parse(register.getStartDate());
		LocalDate endDate = LocalDate.parse(register.getEndDate());
		long daysDifference = ChronoUnit.DAYS.between(startDate, endDate);
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lectureNo", register.getLectureNo());
		paramMap.put("restNum", register.getAcceptableNumber());
		paramMap.put("lectureDate", startDate);		
		
		for(int i=0; i<=daysDifference; i++) {

			lectureRegisterMapper.addRestNumberPerDate(paramMap);			
			startDate = startDate.plusDays(1);
			paramMap.put("lectureDate", startDate);		
		}
		

		
		
		
		
		
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RegisterMessage selectMessage(int registeredMemberNo) {
		return lectureRegisterMapper.selectMessage(registeredMemberNo);
	}

	@Override
	public RegisterMessage updateMessage(RegisterMessage message) {
		RegisterMessage temp = new RegisterMessage();
		
		int messageNo = message.getMessageNo();
		
		int result = -1;
		
		if(messageNo == 0) {
			
			result = lectureRegisterMapper.insertMessage(message);
			
		} else {
			
			result = lectureRegisterMapper.updateMessage(message);
			
		}
		
		if(result > 0) {
			
			return lectureRegisterMapper.selectMessage(message.getRegisteredMemberNo());
			
		} else {
			
			return temp;
			
		}
				
	}

	@Override
	public int deleteMessage(int messageNo) {
		return lectureRegisterMapper.deleteMessage(messageNo);
	}
	

		


	
		
		
	
	
	
	
}
