package com.project.semi.management.model.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.project.semi.common.util.Utility;
import com.project.semi.main.model.dto.Lecture;
import com.project.semi.main.model.dto.LectureFile;
import com.project.semi.main.model.dto.LectureRestnum;
import com.project.semi.main.model.mapper.LectureMapper;
import com.project.semi.management.model.dto.NewBorn;
import com.project.semi.management.model.dto.NewBornBaby;
import com.project.semi.management.model.dto.RegisteredMember;
import com.project.semi.management.model.mapper.ManagementMapper;
import com.project.semi.register.model.dto.RegisterDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Slf4j
@RequiredArgsConstructor
@Service
@PropertySource("classpath:/config.properties")
public class ManagementServiceImpl implements ManagementService {
	
	private final ManagementMapper managementMapper;
	private final LectureMapper lectureMapper;
	
	@Value("${my.profile.web-path}")
	private String webPath;
	
	@Value("${my.profile.folder-path}")
	private String folderPath;
	
	@Override
	public void showMyLectures(int memberNo, String page, Model model) {
		
		// 여기서 6가지 챙겨놔야함. 
		
		// 2. 현재 페이지
		Integer currentPage = Integer.parseInt(page);
		
		// 3. 총 페이지 수 
		// 몇 개의 강의가 있는지 확인 
		 int totalLectures = managementMapper.countMyLecture(memberNo);
		 // 한 페이지당 12개의 게시물이 보여지도록 할 것 
		 int pageSize = 12;
		 // 총 페이지수 
		 int totalPages = (int) Math.ceil((double)totalLectures/pageSize);
		 
		 // 6. 그룹당 페이지의 개수 
		 Integer pageGroupSize = 5;
		 
		 // 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		 // 현재 페이지가 속한 페이지그룹부터 계산해야함 
		 Integer currentGroup = (int) Math.ceil((double) currentPage/pageGroupSize);
		 Integer currentGroupFirstPage = (currentGroup-1) * pageGroupSize + 1;
		 
		 // 5. 현재 페이지가 속한 페이지그룹의 마지막 페이지
		 Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		 
		 // 1. 현재 페이지에 보여질 게시글(PostsDTO) 을 담은 List 자료구조
		 Integer startRow = (currentPage - 1)*pageSize;		
		 
		 log.debug("memberNo=={}", memberNo);
		 log.debug("startRow=={}", startRow);
		 log.debug("pageSize=={}", pageSize);
		 
		 Map<String, Integer> paramMap = new HashMap<>();
		 paramMap.put("memberNo", memberNo);
		 paramMap.put("startRow", startRow);
		 paramMap.put("pageSize", pageSize);
		 
		 List<Lecture> lectureList = managementMapper.showMyLectures(paramMap);
			log.debug("재준아!!!!!!!!!!!!!={}", lectureList.size());

		 
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalLectures", totalLectures);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("lectureList", lectureList);
		
		log.debug("재준아!!!!!!!!!!!!!={}", lectureList.size());
		
	}

	@Override
	public int findOwner(Integer lectureNo, Integer memberNo) {
		
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("memberNo", memberNo);
		
		
		return managementMapper.findOwner(paramMap);
	}

	@Override
	public Lecture findLectureAllData(Integer lectureNo) {

		Lecture lecture = managementMapper.findLectureAllData(lectureNo);
		
		log.debug("lecture여기임!!=={}", lecture.toString());
		
		return lecture;
	}

	@Override
	public int updateLecture(RegisterDTO register, 
			String mainFlag,
			String sub1Flag,
			String sub2Flag,
			String sub3Flag,
			String sub4Flag
			) throws IllegalStateException, IOException {
		// 업데이트될 테이블 목록
		// 1. lecture
		// 2. lecture_file
		// 3. lecture_address
		// 4. lecture_map 이렇게 4가지임. 
		
		// 순서대로 1. lecture
		int result1 = managementMapper.updateLecture(register);
		List<Integer> lectureFileLectureNoList = managementMapper.findLectureNos(register.getLectureNo());
		
		// 현재 lectureFileLectureNoList 라는 List자료구조에는 기존에 데이터베이스에 저장되어있던 해당 강의와
		// 관련된 이미지가 저장되어있는 테이블인 Lecture_FILE 테이블의 LECTURE_FILE_NO 이 0인덱스부터4인덱스까지 순서대로 들어있음. 
		// 그래서, 따져줄거임. 
		// 현재, 사용자가 이미지를 바꾸지 않겠다고 한 경우, NULL 이 들어와있고, 바꾸겠다고 한 경우 새로운 파일이 들어와있을것.

		// 파일은 mainFlag 값이 0 인 경우 main 은 건들지 않아도 됨. 
		// 1인 경우 기존에 있었다면 업데이트. 없었다면 추가
		// -1인 경우 기존에 있었든 없었든 지워버려라 
		
		boolean flag = true;
		
		// ------------------------------------------------------------------------------
		// main
		if(mainFlag.equals("1")) {
			if(lectureFileLectureNoList.size() > 0) {
				// 기존에 있었다. 
				String fileRename = Utility.fileRename(register.getMain().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getMain().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureFileNo(lectureFileLectureNoList.get(0));
				
				int result2 = managementMapper.updateLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getMain().transferTo(new File(folderPath + fileRename));	
				
			} else {
				// 기존에 존재하지 않는 경우.
				// 새로운 행을 추가해줘야 함.
				String fileRename = Utility.fileRename(register.getMain().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getMain().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureNo(register.getLectureNo());
				
				int result2 = managementMapper.addLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getMain().transferTo(new File(folderPath + fileRename));
			}
		}
		
		if(mainFlag.equals("-1")) {
			// -1 인 경우 삭제하라는 뜻임.
			if(lectureFileLectureNoList.size() > 0) {
				// 기존에 존재했던 경우 
				int result23 = managementMapper.deleteLectureFile(lectureFileLectureNoList.get(0));
			} 
				// 기존에 존재하지 않았던 경우 그냥 냅두면 됨.
		}
		
		//------------------------------------------------------------------------------
		//sub1
		if(sub1Flag.equals("1")) {
			if(lectureFileLectureNoList.size() > 1) {
				// 기존에 있었다. 
				String fileRename = Utility.fileRename(register.getSub1().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub1().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureFileNo(lectureFileLectureNoList.get(1));
				
				int result2 = managementMapper.updateLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub1().transferTo(new File(folderPath + fileRename));	
				
			} else {
				// 기존에 존재하지 않는 경우.
				// 새로운 행을 추가해줘야 함.
				String fileRename = Utility.fileRename(register.getSub1().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub1().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureNo(register.getLectureNo());
				
				int result2 = managementMapper.addLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub1().transferTo(new File(folderPath + fileRename));
			}
		}
		
		
		
		if(sub1Flag.equals("-1")) {
			// -1 인 경우 삭제하라는 뜻임.
			if(lectureFileLectureNoList.size() > 1) {
				// 기존에 존재했던 경우 
				int result23 = managementMapper.deleteLectureFile(lectureFileLectureNoList.get(1));
			} 
				// 기존에 존재하지 않았던 경우 그냥 냅두면 됨.
		}
		
		//------------------------------------------------------------------------------
		//sub2--
		
		if(sub2Flag.equals("1")) {
			if(lectureFileLectureNoList.size() > 2) {
				// 기존에 있었다. 
				String fileRename = Utility.fileRename(register.getSub2().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub2().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureFileNo(lectureFileLectureNoList.get(2));
				
				int result2 = managementMapper.updateLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub2().transferTo(new File(folderPath + fileRename));	
				
			} else {
				// 기존에 존재하지 않는 경우.
				// 새로운 행을 추가해줘야 함.
				String fileRename = Utility.fileRename(register.getSub2().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub2().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureNo(register.getLectureNo());
				
				int result2 = managementMapper.addLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub2().transferTo(new File(folderPath + fileRename));
			}
		}
		
	
		
		if(sub2Flag.equals("-1")) {
			// -1 인 경우 삭제하라는 뜻임.
			if(lectureFileLectureNoList.size() > 2) {
				// 기존에 존재했던 경우 
				int result23 = managementMapper.deleteLectureFile(lectureFileLectureNoList.get(2));
			} 
				// 기존에 존재하지 않았던 경우 그냥 냅두면 됨.
		}
		
		//------------------------------------------------------------------------------
		//sub3
		
		if(sub3Flag.equals("1")) {
			if(lectureFileLectureNoList.size() > 3) {
				// 기존에 있었다. 
				String fileRename = Utility.fileRename(register.getSub3().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub3().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureFileNo(lectureFileLectureNoList.get(3));
				
				int result2 = managementMapper.updateLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub3().transferTo(new File(folderPath + fileRename));	
				
			} else {
				// 기존에 존재하지 않는 경우.
				// 새로운 행을 추가해줘야 함.
				String fileRename = Utility.fileRename(register.getSub3().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub3().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureNo(register.getLectureNo());
				
				int result2 = managementMapper.addLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub3().transferTo(new File(folderPath + fileRename));
			}
		}
		
	
		
		if(sub3Flag.equals("-1")) {
			// -1 인 경우 삭제하라는 뜻임.
			if(lectureFileLectureNoList.size() > 3) {
				// 기존에 존재했던 경우 
				int result23 = managementMapper.deleteLectureFile(lectureFileLectureNoList.get(3));
			} 
				// 기존에 존재하지 않았던 경우 그냥 냅두면 됨.
		}
		
		
		
		
		
		//------------------------------------------------------------------------------
		//sub4
		
		if(sub4Flag.equals("1")) {
			if(lectureFileLectureNoList.size() > 4) {
				// 기존에 있었다. 
				String fileRename = Utility.fileRename(register.getSub4().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub4().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureFileNo(lectureFileLectureNoList.get(4));
				
				int result2 = managementMapper.updateLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub4().transferTo(new File(folderPath + fileRename));	
				
			} else {
				// 기존에 존재하지 않는 경우.
				// 새로운 행을 추가해줘야 함.
				String fileRename = Utility.fileRename(register.getSub4().getOriginalFilename());
				LectureFile lectureFile = new LectureFile();
				lectureFile.setFilePath(webPath);
				lectureFile.setOriginalName(register.getSub4().getOriginalFilename());
				lectureFile.setFileRename(fileRename);
				lectureFile.setLectureNo(register.getLectureNo());
				
				int result2 = managementMapper.addLectureFile(lectureFile);
				if(result2 == 0) {
					flag = false;
				}
				register.getSub4().transferTo(new File(folderPath + fileRename));
			}
		}
		
	
		
		if(sub4Flag.equals("-1")) {
			// -1 인 경우 삭제하라는 뜻임.
			if(lectureFileLectureNoList.size() > 4) {
				// 기존에 존재했던 경우 
				int result23 = managementMapper.deleteLectureFile(lectureFileLectureNoList.get(4));
			} 
				// 기존에 존재하지 않았던 경우 그냥 냅두면 됨.
		}
		
		//-----------------------------------------------------------------------------------
		// lecture_address
		// 무조건 업데이트. 업데이트 됬고.
		
		managementMapper.updateLectureAddress(register);
		
		// 다음은 lecture_map 의 위도 경도 바꿔줘야함.
		// 근데, 새로운 주소를 선택하지 않았다면, null 이 들어와 있을 것.
		boolean flag2 = true;
				
		if( !register.getLatitude().equals("") && !register.getHardness().equals("")) {
			int result = managementMapper.updateLectureMap(register);
			if(result == 0) {
				flag2 = false;
			}
		}
		log.debug("result1=={}", result1);
		log.debug("flag=={}", flag);
		log.debug("flag2=={}", flag2);
		
		if(result1 == 0 || !flag || !flag2) {
			// 업데이트가 제대로 안됬다는 것.
			return 0;
		}
		// 업데이트가 제대로 됬다는 것.
		return 1;
	}

	@Override
	public void findRegisteredMemberData(String lectureNo, Model model) {
		
		Lecture lecture = lectureMapper.findLectureDetail(lectureNo);
		model.addAttribute("lecture", lecture);
		
		
		// 1. REGISTERED MEMBER 테이블에서 해당 강의와 관련된 행들을 전부 조회할것. 
		// 근데, 이때, MEMBER 테이블과 조인해서 프로필사진과 닉네임도 함꼐 조회한다. 
		List<RegisteredMember> registeredMemberList = managementMapper.findRegisteredMembers(lectureNo);
		
		// 그러면 조회된 게 List<RegisteredMember> 에 담길 거 아니야? 
		
		// 2. 그 다음에 LECTURE_RESTNUM 테이블에서 LECTURE_NO 로 행들을 조회해서 List<LectureNum>  에넣겠지.
		List<LectureRestnum> lectureRestnumList = managementMapper.findLectureRestNums(lectureNo);
		
		
		// 그 다음에, 2. 에서 조회한 행들(List<LectureNum>) 과 1. 에서 조회된 행들을 조합하기 시작할거임. 
		List<NewBorn> newBornList = new ArrayList<>();
		
		for (LectureRestnum restNum : lectureRestnumList) {
			
			NewBorn newBorn = new NewBorn();
			newBorn.setLectureRestnumNo(restNum.getLectureRestnumNo());
			newBorn.setLectureNo(restNum.getLectureNo());
			newBorn.setLectureDateStr(restNum.getLectureDateStr());
			newBorn.setRestNum(restNum.getRestNum());
			
			for(RegisteredMember member : registeredMemberList) {
				if(restNum.getLectureDateStr().equals(member.getLectureDateStr())) {
					NewBornBaby baby = new NewBornBaby();
					
					baby.setRegisteredMemberNo(member.getRegisteredMemberNo());
					baby.setMemberNo(member.getMemberNo());
					baby.setRegisteredMemberFl(member.getRegisteredMemberFl());
					baby.setMemberNickname(member.getMemberNickname());
					baby.setProfileImg(member.getProfileImg());
					baby.setQuantity(member.getQuantity());
					
					newBorn.getBabyList().add(baby);
				} 
			}
			
			newBornList.add(newBorn);			
		}
		
		
		for(NewBorn newborn : newBornList) {
			log.debug("newBorn==========================={}", newborn);
		}
		
		// 이때, 새로운 List 타입을 만들거고, 그 List 타입은 NewBorn 이라고 하자. 
		// 그 다음에, 조건을 거는거야. 2. 에서 조회된 행들을 하나씩 꺼내와서 넣을 거임. 
		// 근데, 만약에 2. 에서 조회된 행들의 LECTURE_DATE 와 1 에서 조회된 행들의 LECTURE_DATE 가 같다면, 
		// 그러면 NewBorn 객체의 1. 에서 조회된 행들을 담는 필드에 값이 넣어질 것이고, 
		// 그러지 일치하는 게 없다면, 그냥 2. 에서 조회된 것들만 NewBorn 에 담기게 되는 구조가 될 것. 
		
		model.addAttribute("newBornList", newBornList);
		
		
		
	}

	@Override
	public Integer findMemberNo(String lectureNo) {
		return managementMapper.findMemberNo(lectureNo);		

	}

	@Override
	public Integer updateFlag(String lectureNo, String lectureDate, String memberNo, String quantity) {
		

		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("lectureDate", lectureDate);
		paramMap.put("memberNo", memberNo);
		paramMap.put("quantity", quantity);

		// 같은 날짜에 같은 사용자가 동일한 개수를 여러번에 걸쳐 나눠서 구매할수도 있잖아. 
		// 그걸 고려해서, 하나만 지우도록 해야함.
		// 같은 강의를, 같은 날짜에, 같은 사람이, 동일한 수량을 여러 번에 걸쳐서 구매한 경우 
//		// 어떠한 것을 취소하더라도 상관없잖아. 
//		// 근데, 다른 수량을 취소해버리면 안되잖아. 따라서, 현재 파라미터로 받은 quantity 와 일치하는 데이터를 지워야함.
		List<Integer> registeredMemberNoList = managementMapper.findRegisteredMember(paramMap);
		
		Integer registeredMemberNo = registeredMemberNoList.get(0);
		paramMap.put("registeredMemberNo", registeredMemberNo);
		
		managementMapper.updateFlagSpecific(paramMap);
		
		return registeredMemberNo;
		
	}

	@Override
	public int checkLecturerMemberNo(String lectureNo, Integer memberNo) {
		Integer findMemberNo = managementMapper.checkLecturerMemberNo(lectureNo);
		if(findMemberNo == memberNo) {
			return 1;
		}
		return 0;
	}

	@Override
	public int deleteLecture(String lectureNo) {
		return managementMapper.deleteLecture(lectureNo);
	}

	@Override
	public Integer minusFeeSettlement(String lectureNo, String quantityStr) {
		
		Lecture lecture = managementMapper.findLectureAllData(Integer.parseInt(lectureNo));
		
		Integer lecturerMemberNo = lecture.getMemberNo();
		Integer price = lecture.getPrice();
		Integer quantity = Integer.parseInt(quantityStr);
		
		Integer minusAmount = price * quantity;
		
		// FEE_SETTLEMENT 테이블에서 해당강사의 정산금액 감소시키기 
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("lecturerMemberNo", lecturerMemberNo);
		paramMap.put("minusAmount", minusAmount);
		
		managementMapper.minusFeeSettlement(paramMap);
		
		return minusAmount;
	}

	@Override
	public void addRefundCustomer(String lectureNo, String memberNo, Integer minusAmount, Integer registeredMemberNo) {
		// lectureNo : 사용자를 강퇴한 그 강의의 LECTURE 테이블 기본키
		// memberNo : 강퇴당한 사용자의 MEMBER 테이블 MEMBER_NO
		// quantity : 강퇴당한 사용자가 결제했을 때 구매한 수량 
		// registeredMemberNo : 사용자가 강퇴되었기 때문에 REGISTERED_MEMBER 테이블에서 행의 REGISTERED_MEMBER_FL 컬럼값을
		// 						Y 로 바꾸었는데, 그 행에 MERCHANT_UID(주문고유번호) 가 들어있거든. 
		//						따라서, registeredMemberNo 로 행을 조회해서 merchantUid 를 얻을 수 있는거임. 
		
		String merchantUid = managementMapper.findMerchantUid(registeredMemberNo);
		// 이걸 왜 REFUND_CUSTOMER 테이블에 넣으려 하냐? 모르겠어. 
		// 이 값이 있으면, 환불해줄 그 사용자가 한 주문 내역을 볼 수 있음.
		// 뿐만 아니라 LECTURE_PAYMENTS 테이블에서 포트원이 준 결제내역도 볼 수 있음. 
		// 애플리케이션 입장에서 돈이 나가는 상황인데, 환불해줄 사용자가 어떤 주문을 했고, 어떤 결제를 했는지 그 결제는 성공적이었는지 등을
		// 파악하기 위함이 아닐까?
		
		// 재료 다 모음. insert 하자. 
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("memberNo", memberNo);
		paramMap.put("minusAmount", minusAmount);
		paramMap.put("merchantUid", merchantUid);
		managementMapper.addRefundCustomer(paramMap);
		
	}
	
	@Override
	public void addMessage(String memberNo, Integer registeredMemberNo, Integer loginMemberNo) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberNo", memberNo);
		paramMap.put("registeredMemberNo", registeredMemberNo);
		paramMap.put("loginMemberNo", loginMemberNo);
		

		managementMapper.addMessage(paramMap);
	
	}

	
	
	
	
	
}
