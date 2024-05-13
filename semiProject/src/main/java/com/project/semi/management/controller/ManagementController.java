package com.project.semi.management.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.semi.main.model.dto.Lecture;
import com.project.semi.management.model.service.ManagementService;
import com.project.semi.member.model.dto.Member;
import com.project.semi.payment.model.service.PaymentService;
import com.project.semi.register.model.dto.RegisterDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("manage")
public class ManagementController {

	private final ManagementService managementService;
	private final PaymentService paymentService;
	
	@GetMapping("showMyLectures")
	public String showMyLectures(@SessionAttribute("loginMember") Member loginMember, Model model,
															@RequestParam(value="page", required=false, defaultValue="1") String page
			) {
		// 1. loginMember 에서 memberNo 를 꺼내서, DB 에서 관련된 강의들을 가져와야지. 
		// 이때, 가져올때에 페이지네이션을 진행해야함. 
		int memberNo = loginMember.getMemberNo();
		
		managementService.showMyLectures(memberNo, page, model);
		


		return "/management/myLectures";
	}
	
	@GetMapping("updateLecture")
	public String updateLecture(@RequestParam("lectureNo") Integer lectureNo,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra, Model model
			) {
		
		// 1. 요청한 사용자가 해당 강의의 주인이 맞는지 확인하는 코드
		int result = managementService.findOwner(lectureNo, loginMember.getMemberNo());
		
		if(result == 0) {
			ra.addFlashAttribute("message", "잘못된 접근입니다.");
			return "redirect:/";
		}
		
		// 2. 강의에 대한 정보를 가져와서 수정폼에 뿌려줄건데, 그걸 위해 강의에 대한 정보를 조회해온다.
		Lecture findLecture = managementService.findLectureAllData(lectureNo);
		
		log.debug("aaaaaaaaaaaaaaaaaaa={}",findLecture.toString());
		
		model.addAttribute("findLecture", findLecture);		
				
		String startTime = findLecture.getStartTime();
		
		
        String firstTwoChars = startTime.substring(0, 2); // 첫 번째에서 두 번째 문자까지 추출
        String lastTwoChars = startTime.substring(startTime.length() - 2); // 마지막 두 문자 추출

        model.addAttribute("firstTwoChars", firstTwoChars);
        model.addAttribute("lastTwoChars", lastTwoChars);
		
		return "/management/updateForm";
	}
	
	@PostMapping("updateLecture")
	public String updateLecture(@ModelAttribute RegisterDTO register,
								@RequestParam(value="mainFlag", required=false) String mainFlag,
								@RequestParam(value="sub1Flag", required=false) String sub1Flag,
								@RequestParam(value="sub2Flag", required=false) String sub2Flag,
								@RequestParam(value="sub3Flag", required=false) String sub3Flag,
								@RequestParam(value="sub4Flag", required=false) String sub4Flag,
								RedirectAttributes ra
								
			) throws IllegalStateException, IOException{
		
		log.debug("register==={}aaaaaaaaaaaaaaaaaaaa", register.getMain().getOriginalFilename());
		log.debug("mainFlag=={}",mainFlag );
		log.debug("mainFlag=={}",sub1Flag );
		log.debug("mainFlag=={}",sub2Flag );
		log.debug("mainFlag=={}",sub3Flag );
		log.debug("mainFlag=={}",sub4Flag );
		
		// 주소는 입력해도 입력하지 않아도 그냥 온 걸 그대로 업데이트 해주면 됨.		
		// 위도경도는 null 이 아닌 경우에만 update 해주면 됨. 
		// 파일은? mainFlag 가 0이면 기존 것 유지. 1이면 업데이트. -1이면 기존 것 삭제 
		register.setStartTime(		register.getStartHour() + register.getStartMin()  );
		
		
		log.debug("register=={}", register); //lectureNo 을 hidden 타입 input 으로 전달받음. 
		int result = managementService.updateLecture(register, 
													 mainFlag,
													 sub1Flag,
													 sub2Flag,
													 sub3Flag,
													 sub4Flag);
		
		if(result == 0) {
			// 업데이트 중 오류 발생
			ra.addFlashAttribute("message", "수정 중 오류가 발생하였습니다");
			return "redirect:/manage/updateLecture?lectureNo=" + register.getLectureNo();
		}
		
	
			// 업데이트가 제대로 됬다. 
			ra.addFlashAttribute("message","수정이 완료되었습니다.");
			return "redirect:/manage/showMyLectures";
	}
	
	
	@GetMapping("manageRegisteredMember")
	public String manageRegisteredMember(@RequestParam("lectureNo") String lectureNo, Model model) {
		
		managementService.findRegisteredMemberData(lectureNo, model);
		
		return "/management/registeredMember";
	}
	
	@GetMapping("cancelRegister")
	public String cancelRegister(@RequestParam("lectureNo")String lectureNo, 
							@RequestParam("lectureDate") String lectureDate,
							@RequestParam("memberNo") String memberNo,
							@RequestParam("quantity") String quantity,
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra
						) {

		log.debug("lectureNo== {}", lectureNo);
		log.debug("lectureDate== {}", lectureDate);
		log.debug("memberNo== {}", memberNo);
		log.debug("quantity=={}", quantity);


		Integer loginMemberNo = loginMember.getMemberNo();      
		// 들어가기 전에, 지금 요청을 보낸 사용자가 해당 강의를 포스팅한 강사가 맞는지 확인 
		Integer findMemberNo = managementService.findMemberNo(lectureNo);

		if(loginMemberNo != findMemberNo) {
			ra.addFlashAttribute("message", "잘못된 접근입니다.");
			return "redirect:/";
		}
	        
		// 1. REGISTERED_MEMBER 테이블에서 해당 회원을 삭제한다. 
		Integer registeredMemberNo = managementService.updateFlag(lectureNo, lectureDate, memberNo, quantity);
		// 현재 registeredMemberNo 에는 REGISTERED_MEMBER 테이블에서 위 코드로 REGISTERED_MEMBER_FL 컬럼값을
		// 'Y' 로 바꾼 그 행의 기본키가 들어있다. 아래에서 쓰려고 함. 
	
	
		// 2. LECTURE_RESTNUM 테이블에서 해당 강의의 해당 날짜의 REST_NUM 컬럼값을 얼마큼 증가시켜야해? 
		// 해당 사용자가 결제한 수량만큼 증가시켜야함. 
		// 그러기 위해서는, 뭐가 필요하냐면, 해당 주문에 대한 정보가필요함. 근데, 어떻게 그 정보를 가져올 수 있을까? 
		// REGISTERED_MEMBER 테이블에 merchant_uid 컬럼을추가시켜놓음. 
		// 현재 이 강의에(lectureNo) 특정 날짜에(lectureDate) 에 강의신청한 memberNo를 조회한다. 
		// 아직 없는 정보는 수량에 대한 정보인데, 이 수량에 대한 정보로 같은 날짜에 여러번 다른 수량 혹은 같은 수량으로
		// 주문한 사용자에 대한 주문 취소처리를 할 수 있을 것이라 예상한다.       
		paymentService.plusRestNum(lectureDate, lectureNo, quantity);

		// 3. 정산테이블에서 해당 강사의 amount 감소시키기.
		// minusAmount : 정산에서 제외한 금액 == 환불해줄 금액이다. 환불 테이블에 삽입할 때 쓰려고 리턴받아옴. 
		Integer minusAmount = managementService.minusFeeSettlement(lectureNo, quantity);
	
		// 4. 테이블을 하나 만든다. 그 테이블은 환불할 대상자들의 명단을 담은 테이블로 한다. 
		// 관리자페이지에서 해당 대상자들의 명단을 보여주도록 한다. merchant_uid
		managementService.addRefundCustomer(lectureNo, memberNo, minusAmount, registeredMemberNo);

		ra.addFlashAttribute("message", "해당 참여자가 강의에서 제외되었습니다");
	
		// 메세지 취소되었습니다 를 전달하면 됨. 
		// 필요한 재료들
		// 1. MEMBER_NO : 받을 놈. 현재 memberNo에 들어와 있음 
		// 2. MESSAGE_TITLE 은 '결제 취소 안내' 라고 써주면 되고
		// 3. MESSAGE_CONTENT 는 '강사님의 취소로 결제가 취소되었습니다' 라고 써주면 됨. 
		// 4. REGISTERED_MEMBER_NO 는 registeredMemberNo
		// 5. LECTURER_MEMBER_NO 는 loginMemberNo 로.
	  
		managementService.addMessage(memberNo, registeredMemberNo, loginMemberNo);

		return "redirect:/manage/manageRegisteredMember?lectureNo=" + lectureNo;
	}
	
	@GetMapping("deleteLecture")
	public String deleteLecture(@RequestParam("lectureNo") String lectureNo, 
							@SessionAttribute("loginMember") Member loginMember,
							RedirectAttributes ra 
			) {
		// 지금 요청이 강의 주인(강사) 인지 확인
		Integer memberNo = loginMember.getMemberNo();
		int result = managementService.checkLecturerMemberNo(lectureNo, memberNo);
		
		if(result == 0) {
			ra.addFlashAttribute("message", "잘못된 접근입니다");
			return "redirect:/";
		}
		
		// 삭제 (FL 만 업데이트)
		int result2 = managementService.deleteLecture(lectureNo);
		
		if(result2 == 0) {
			ra.addFlashAttribute("message", "강의 삭제 중 오류 발생");
		}else {
			ra.addFlashAttribute("message", "강의가 삭제되었습니다");
		}
		
		
		return "redirect:/manage/showMyLectures";
	}
	
	
	
}

