package com.project.semi.member.model.service;

import java.io.File;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.project.semi.common.util.Utility;
import com.project.semi.member.model.dto.Member;
import com.project.semi.member.model.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
@PropertySource("classpath:/config.properties")
public class MemberServiceImpl implements MemberService{

	private final MemberMapper memberMapper;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${my.profile.web-path}")
	private String webPath; // 앞에 붙이는 조각
	
	@Value("${my.profile.folder-path}")
	private String folderPath; //찐 저장소
	
	/** 
	 * 회원가입 시, 중복된 이메일이 있는지 확인하는 이메일.
	 * int 값이 1 이면, 중복된 이메일이 있다. 0이면 중복된 이메일이 없다.
	 */
	@Override
	public int checkEmail(String memberEmail) {
		return memberMapper.checkEmail(memberEmail);
	}

	
	/**
	 * 회원가입해주는 메서드
	 */
	@Override
	public int signUp(Member member, String[] memberAddress) {
		// log.debug(Arrays.toString(memberAddress));
		// 사용자가 주소를 입력하지 않았을 경우 : 정확히 [, , , ] 로 뜸. 
		// 주소를 입력했을 경우 : [01076, 서울 강북구 노해로13길 40, 서울 강북구 수유동 31-67, ㅁㅁㅁ] <- 이렇게 문자열 배열로 들어와 있네.
		
		// log.debug(member.getMemberAddress());
		// 사용자가 주소를 입력하지 않았을 경우 : 정확히,,,로 뜸.
		// 주소를 입력했을 경우 : 01076,서울 강북구 노해로13길 40,서울 강북구 수유동 31-67,ㅁㅁㅁ <- 이렇게 문자열로 들어와 있네. 
		
		if(!member.getMemberAddress().equals(",,,")) {
			// 주소를 입력하지 않았을 경우가 아니라면 == 주소를 입력했다면
			
			String address = String.join("^^^", memberAddress);
			//log.debug(address); => 01076^^^서울 강북구 노해로13길 40^^^서울 강북구 수유동 31-67^^^ㅁㅁㅁ
			member.setMemberAddress(address);
		} else {
			// 사용자가 주소입력 하지 않은 경우
			// 현재에는 정확히,,,가 들어가 있겠지. 그렇다면, null 로 바꿔줌. 
			// 이대로 db 에 넣을건데, 그럼 db 에 null 로 들어가게 되는데, 이게 가능한 이유는
			// mybatis.config 의 <setting name="jdbcTypeForNull" value="NULL" /> 이라는 코드 덕분인 것.
			member.setMemberAddress(null);
		}
		
		// 비밀번호 암호화
		String encPw = bCryptPasswordEncoder.encode(member.getMemberPw());
		member.setMemberPw(encPw);
		
		// insert 쿼리 실행
		return memberMapper.signUp(member);		
	}


	// 로그인 기능
	@Override
	public Member login(String memberEmail, String memberPw) {
		// MEMBER 테이블에서 사용자가 입력한 이메일과 동일한 이메일을 가진 행을 조회.
		Member loginMember = memberMapper.login(memberEmail);
		
		// 만약 일치하는 이메일이 없어서 null 인 경우
		if(loginMember == null) {
			return null;
		}
		
		// 만약 일치하는 이메일이 있다면~
		if(!bCryptPasswordEncoder.matches(memberPw, loginMember.getMemberPw())) {
			// 비밀번호가 일치하지 않는 경우
			return null;			
		}
		
		// 여기까지 왔다는 건, 일치하는 이메일이 있다는 뜻.
		// 세션객체에 loginMember 라는 키로 현재 조회된 loginMember 를 담아줄 건데, 
		// 최대한의 안전을 위해 비밀번호는 삭제한다. 
		loginMember.setMemberPw(null);
		
		return loginMember;
	}


	@Override
	public int updateInfo(Member inputMember, int memberNo, String[] memberAddress) {
		if(inputMember.getMemberAddress().equals(",,,")) {
			//공백이라면? 주소입력안했다는 것.
			inputMember.setMemberAddress(null);
		} else {
			// 공백이 아닌 경우, ^^^ 를 넣어서 가공해서 db 에 넣자.
			String address = String.join("^^^", memberAddress);
			inputMember.setMemberAddress(address);
		}
		
		inputMember.setMemberNo(memberNo);
		
		return memberMapper.updateInfo(inputMember);
	}


	@Override
	public Member findMemberByMemberNo(Integer memberNo) {
		return memberMapper.findMemberByMemberNo(memberNo);
		
	}

//
//	@Override
//	public int updateImg(MultipartFile profileImg, Member loginMember) throws Exception {
//		// TODO Auto-generated method stub
//		return 0;
//	}


	// 프로필 이미지 변경
	  @Override 
	  public int updateImg(MultipartFile profileImg, Member loginMember)
	  throws Exception{
	 
	 log.debug("profileImg={}", profileImg);
	 
	 String updatePath = null; 
	 String rename = null;
	 
	 if(!profileImg.isEmpty()) { rename =
	 Utility.fileRename(profileImg.getOriginalFilename()); 
	 log.debug("rename={}", rename);
	 
	 updatePath = webPath + rename; // 고유키 앞에 조각을 붙임.
	 log.debug("updatePath={}",updatePath); }
	 
	 Member member = Member.builder()
			 							.memberNo(loginMember.getMemberNo())
			 							.profileImg(updatePath)
			 							.build();
	 
	 int result = memberMapper.updateImg(member);
	 
	 if(result > 0) { // 업데이트 성공시 
		 if(!profileImg.isEmpty()) {  // 사용자가 이미지 제출시 
			 profileImg.transferTo(new File(folderPath + rename));  // 찐 저장소에 이미지 저장
			 }
	 }
	 
	 loginMember.setProfileImg(updatePath); // 세션객체 안에 있는 이미지 경로도 바꿔줌.
	 // @SessionAttribute 를 쓰면 // 거기서 쓴 객체를 이렇게 바꾸면, 반영이 된다고 함. 
	 return result;
	 } 




}