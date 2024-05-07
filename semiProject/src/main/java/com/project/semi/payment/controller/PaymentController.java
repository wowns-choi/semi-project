package com.project.semi.payment.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.semi.exception.PaymentVerificationException;
import com.project.semi.main.model.service.LectureService;
import com.project.semi.member.model.dto.Member;
import com.project.semi.payment.model.dto.TokenResponse;
import com.project.semi.payment.model.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("payment")
public class PaymentController {
	
	private final PaymentService paymentService;
	private final LectureService lectureService;

	@GetMapping("checkRestNumber")
	@ResponseBody
	public int checkRestNumber(@RequestParam("lectureNo") String lectureNo,
								@RequestParam("quantity") String quantity,
								@RequestParam("selectDate") String selectDate,
								HttpServletRequest request
			) {
		
		log.debug("lectureNo==={}", lectureNo);
		log.debug("quantity==={}", quantity);
		log.debug("selectDate===={}", selectDate);
		
		HttpSession session = request.getSession();
		Member loginMember = (Member) session.getAttribute("loginMember");
		
		// 로그인 되지 않은 사용자 
		if(loginMember == null) {
			return 0;
		}
		
		
		// 아직 해당 강의를 수강할 자리가 남아있는지 확인 (재고확인)
		// 자리 있으면 1 , 없으면 2 리턴 
		int restNum = lectureService.checkRestNum(lectureNo, selectDate);
		
		if(restNum == 0) {
			return 2;
		}
		
		// 여기까지 왔다는 건, 남은자리가 있었다는 것. 
		return 1;
		
	}
	
	@PostMapping("addOrder")
	@ResponseBody
	public Map<String, String> addOrder(
				@RequestBody Map<String, Object> map,
				@SessionAttribute("loginMember") Member loginMember
			) {
		
		Integer lectureNo = (Integer)map.get("lectureNo");

		
		
		String totalPriceStr = String.valueOf(map.get("totalPrice"));
		Integer totalPrice = Integer.parseInt(totalPriceStr);

		String quantityStr = (String) map.get("quantity");
		Integer quantity = Integer.parseInt(quantityStr);
		
		log.debug("-----------=={}", lectureNo);
		log.debug("-----------=={}", totalPrice);
		
		// LECTURE_ORDERS 테이블에 행 삽입
		Map<String, String> returnMap = paymentService.addOrder(lectureNo, totalPrice, loginMember.getMemberNo(), quantity);
		
		return returnMap;
	}
	
	@PostMapping("preValidation")
	@ResponseBody
	public Map<String,String> preValidation(
			@RequestBody Map<String, Object> map
			) {
		Integer lectureNoInt = (Integer)map.get("lectureNo");
		String lectureNo = String.valueOf(lectureNoInt);
		
		String quantity = String.valueOf(map.get("quantity"));
		
		String selectDate = (String) map.get("selectDate");
		
		String totalPriceStr = String.valueOf(map.get("totalPrice"));
		Integer totalPrice = Integer.parseInt(totalPriceStr);


		
		String merchantUid = (String) map.get("merchantUid");
		
		
		log.debug("quantity===!!!!!!!!!!!!!!!!{}", quantity);
		
		// 남은 자리가 있는지 확인
		int restNum = lectureService.checkRestNum(lectureNo, selectDate);
		
		Map<String, String> returnMap = new HashMap<>();
		
		if(restNum == 0) {
			// 남은 자리가 없는 경우. 
			returnMap.put("outOfStock", "남은자리가 부족합니다.");
		}
		
		// 남은 자리가 있는 경우. 
		// 남은자리를 미리 하나 제거해둠. 왜? 결제했는데, 남은자리가 없는 경우를 방지 
		// 손해를 좀 보더라도 사용자가 화나게 하지 않기 위함. 
		// 나중에 결제가 문제가 생긴다면 그 때 다시 남은 자리를 +1 해주는 걸로. 		
		
		log.debug("여기왔나?????????????????????");
		
		paymentService.minusRestNum(lectureNo, selectDate, quantity);
		
		// Access Token 얻어오기 
		TokenResponse token = paymentService.getAccessToken();
		
		paymentService.preValidation(token, merchantUid, totalPrice);
		
		return returnMap;
		
	}
	
	@PostMapping("getPortOneResponse")
	@ResponseBody
	public String getPortOneResponse (@RequestBody Map<String, Object> paramMap,
									@SessionAttribute("loginMember") Member loginMember
			) {
		
		String impUid= (String) paramMap.get("imp_uid");
		String merchantUid = (String) paramMap.get("merchant_uid");
		Integer lectureNo = (Integer) paramMap.get("lectureNo");
		String lectureDate = (String) paramMap.get("lectureDate");		
		Integer totalPrice = (Integer) paramMap.get("totalPrice");

		
		// 일단, LECTURE_ORDERS 테이블의 해당 주문 행의 status 컬럼을 Processing 으로 update 
		// Processing : 결제 처리 중 
		paymentService.updateStatus(merchantUid, "PROCESSING");
		
		// Access Token 얻어오기 
		TokenResponse token = paymentService.getAccessToken();

		// 결제 단건 조회 
		ResponseEntity<String> response = paymentService.singlePaymentQuery(impUid, token);
		
		try {
			
			// 일단 LECTURE_PAYMENTS 테이블에 행을 삽입. 사후검증여부와는 무관하게 일단 결제가 됬으니까. 
			String responseBody = response.getBody(); // 포트원 HTTP 응답메세지의 body 가져오기 
			Integer memberNo = loginMember.getMemberNo();
			paymentService.addPayment(memberNo, responseBody);
			
	        // 2-5) 사후검증
	        paymentService.postValidation(responseBody);
	        
	        // 여기서, REGISTERED_MEMBER 테이블에 행을 삽입. 
	        // 만약 위 코드에서 예외가 터졌다면 여기로 안오겠지. 
	        paymentService.addRegisteredMember(memberNo, lectureNo, lectureDate, merchantUid);
	        
	        // 정산 테이블에 해당 강사의 정산금액을 증가시키기 
	        paymentService.plusFeeSettlement(lectureNo, totalPrice);
	        
	        
	        return "success";

		} catch (PaymentVerificationException e) {
	    //사후검증에 실패한 경우 :
	        // orders 테이블의 status, fail_reason 컬럼을 실패했다고 업데이트 해주는 것은 SinglePaymentQueryService 에서 해줬기 때문에 그 과정 생략.
	
	        Integer portOneAmount = e.getPortOneAmount();
	        Integer myDbAmount = e.getMyDbAmount();
	        Integer priceDifference = e.getPriceDifference();
	
	        if (priceDifference > 0) {
	            //덜 결제된 경우
	            return "결제했어야 할 금액 : " + myDbAmount + ", 결제된 금액 : " + portOneAmount + ", " + priceDifference + "가 더 결제되어야 합니다. 최대한 빠른 시일 내에 당사 직원이 연락드리겠습니다. " ;
	        } else{
	            //더 결제된 경우 => 정상처리로 보고 정상처리를 해줘야지.
	            return "결제했어야 할 금액 : " + myDbAmount + ", 결제된 금액 : " + portOneAmount + ", " + priceDifference + "가 더 결제되었습니다. 최대한 빠른 시일 내에 당사 직원이 연락드리겠습니다." ;
	        }
	    } catch (JsonProcessingException e) {
	        //JSON 파싱에 실패한 경우
	        return "결제 중 오류가 발생되었습니다. JsonParsingException";
	    } catch(Exception e){
	    	
	    	log.debug("eeeeeeeeeee={}", e);
	    	e.printStackTrace();
	        return "결제 중 오류가 발생되었습니다. Exception";
	    }
			
			
		}
	
	
	@RequestMapping("/paymentFail")
	public String paymentFail(@RequestParam("merchantUid") String merchantUid,
							@RequestParam("impUid") String impUid,
							@RequestParam("selectDate") String selectDate,
							@RequestParam("lectureNo") String lectureNo,
							@RequestParam("quantity") String quantity,
							@SessionAttribute("loginMember") Member loginMember
							
			) {
		
		// LECTURE_RESTNUM 테이블의 REST_NUM 다시 증가시켜줘야 함. 
		paymentService.plusRestNum(selectDate, lectureNo, quantity);
		
		// LECTURE_ORDERS 테이블의 status 를 PAYMENT_FAIL 로 처리 
		// PAYMENT_FAIL : 결제자체가 실패함. 
		paymentService.updateStatus(merchantUid, "PAYMENT_FAIL");
		
		// 결제가 실패했어도, 포트원에게 받은 정보 그대로를 LECTURE_PAYMENTS 테이블에 저장한다. 
		TokenResponse token = paymentService.getAccessToken();
		ResponseEntity<String> response = paymentService.singlePaymentQuery(impUid, token);
		String responseBody = response.getBody();
		paymentService.addPayment(loginMember.getMemberNo(), responseBody);
		
		
		return "redirect:/lecture/showLectureDetail?lectureNo=" + lectureNo;
	}
	
	
	
	
	
	
}
