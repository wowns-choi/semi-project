package com.project.semi.member.model.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.member.model.dto.Member;

@Mapper
public interface MemberMapper {


	int checkEmail(String memberEmail);

	int signUp(Member member);

	Member findMemberByPw(String memberPw);

	Member findMemberByEmail(String memberEamil);

	Member login(String memberEmail);

	int updateInfo(Member inputMember);

	Member findMemberByMemberNo(Integer memberNo);

	int updateImg(Member member);

	String findPw(int memberNo);

	void withdrawal(int memberNo);

	int newPw(Map<String, Object> paramMap);

	Member foundId(Member member);

	int foundIdCount(Member member);
	
	int getAuth(Member member);

	Map<String, String> checkAuth();

	int checkAuth(Map<String, String> map);

	int rePw(Map<String, String> map);

	

	

}
