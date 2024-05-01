package com.project.semi.member.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.project.semi.member.model.dto.Member;

public interface MemberService {

	/** 회원가입 시, 중복된 이메일이 있는지 확인하는 이메일
	 * @param memberEmail
	 * @return int 값이 1 이면, 중복된 이메일이 있다. 0이면 중복된 이메일이 없다. 
	 */
	int checkEmail(String memberEmail);

	/** 회원가입해주는 메서
	 * @param member
	 * @param memberAddress
	 * @return
	 */
	int signUp(Member member, String[] memberAddress);

	Member login(String memberEmail, String memberPw);

	int updateInfo(Member inputMember, int memberNo, String[] memberAddress);

	Member findMemberByMemberNo(Integer memberNo);

	int updateImg(MultipartFile profileImg, Member loginMember) throws Exception;

	int withdrawal(Map<String, String> map, Member loginMember);

	
	/** 비밀번호 변경 , 입력한 값과 DB저장값 비교용
	 * @param inputPw
	 * @param memberNo
	 * @return
	 */
	int changePw(String inputPw, int memberNo);

	int newPw(String newPw, Member loginMember);

	/** 아이디 찾기
	 * @param member
	 * @return
	 */
	String foundId(Member member);

	


}