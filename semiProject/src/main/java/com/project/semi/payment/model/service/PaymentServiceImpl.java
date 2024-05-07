package com.project.semi.payment.model.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.semi.exception.JsonParsingException;
import com.project.semi.exception.PaymentVerificationException;
import com.project.semi.payment.model.dto.Order;
import com.project.semi.payment.model.dto.Payment;
import com.project.semi.payment.model.dto.PaymentCancelAnnotation;
import com.project.semi.payment.model.dto.TokenResponse;
import com.project.semi.payment.model.mapper.PaymentMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@PropertySource("classpath:/config.properties")
public class PaymentServiceImpl implements PaymentService{
	
	private final PaymentMapper paymentMapper;
	private final RestTemplate restTemplate;
	
	@Value("${portone.rest-api-key}")
	private String apiKey;
	
	@Value("${portone.rest-api-secret}")
	private String apiSecret;
	
	
	@Override
	public int minusRestNum(String lectureNo, String selectDate, String quantity) {
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("selectDate", selectDate);
		paramMap.put("quantity", quantity);
		
		log.debug("quantity={}", quantity);
		
		Integer result = paymentMapper.minusRestNum(paramMap);
		return result;
	}

	@Override
	public Map<String, String> addOrder(Integer lectureNo, Integer totalPrice, Integer memberNo, Integer quantity) {
		
		Order order = new Order();
		order.setMemberNo(memberNo);
		order.setLectureNo(lectureNo);
		order.setAmount(totalPrice);
		order.setQuantity(quantity);
		order.setStatus("PENDING");
		
		//merchantUid 생성
        long nano = System.currentTimeMillis();
        String merchantUid = "pid-" + nano;
        
        order.setMerchantUid(merchantUid);
        
        int result = paymentMapper.addOrder(order);		
		
        Map<String, String> returnMap = new HashMap<>();
        returnMap.put("merchantUid", merchantUid);
        returnMap.put("result", String.valueOf(result));
        
		return returnMap;
	}

	@Override
	public TokenResponse getAccessToken() {
		// RestTemplate 을 이용해서 HTTP 통신할 것. 
		// 누구에게 보낼건지. 
		String url = "https://api.iamport.kr/users/getToken";
		
		// HTTP요청메세지의 본문을 application/x-www-form-urlencoded 형식으로 지정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		// body 채우기
		// application/x-www-form-urlencoded 형식으로 지정 했으면, 반드시 MultiValueMap 을 써야 함. 
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("imp_key", apiKey);
		map.add("imp_secret", apiSecret);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		
		TokenResponse responseJson = restTemplate.postForObject(url, entity, TokenResponse.class);
		
		return responseJson;
		
	}

	@Override
	public void preValidation(TokenResponse token, String merchantUid, Integer totalPrice) {
		// 사전 검증 : 포트원에게 "이 merchantUid(주문번호) 는 totalPrice 만큼 결제되어야 해" 라고 말하는 것 
		
		String url = "https://api.iamport.kr/payments/prepare";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON); // 본문에 json 형식의 데이터가 갈 것. 
		headers.set("Authorization", token.getResponse().getAccess_token()); // 헤더에 token 넣어줌.
		
		Map<String, Object> map = new HashMap<>();
		map.put("merchant_uid", merchantUid);
		map.put("amount", totalPrice);
		
		HttpEntity<Map<String,Object>> entity = new HttpEntity<Map<String,Object>>(map,headers);
		
		// 사전 검증 요청 
		String response = restTemplate.postForObject(url, entity, String.class);		
		
	}
	
	@Override
	public void updateStatus(String merchantUid, String updateMessage) {
		
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("merchantUid", merchantUid);
		paramMap.put("updateMessage", updateMessage);
		
		paymentMapper.updateStatus(paramMap);
		
	}
	

	@Override
	public ResponseEntity<String> singlePaymentQuery(String impUid, TokenResponse token) {
		
		String url = "https://api.iamport.kr/payments/" + impUid;
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", token.getResponse().getAccess_token());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		return response;
	}

	@Override
	public void addPayment(Integer memberNo, String responseBody) {
		
		// 1. 포트원이 준 데이터를 분해해서 필요한 정보를 빼내올 것. 
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode responseNode = rootNode.path("response");

            String impUid = responseNode.path("imp_uid").asText(); //포트원 거래 고유번호 : imp_088xxxx
            String merchantUid = responseNode.path("merchant_uid").asText(); //가맹점 주문번호. 내 프로젝트에서 결제건마다 부여한 고유번호로서 랜덤한 숫자로 구성함 : 57008833-33010
            int amount = responseNode.path("amount").asInt(); //결제건의 결제금액 : 1
            String currency = responseNode.path("currency").asText(); //결제통화 구분코드 (KRW..등) : KRW

            String status = responseNode.path("status").asText(); //결제건의 결제상태 : paid(결제완료), ready(브라우저 창 이탈, 가상계좌 발급완료 미결제 상태), failed(신용카드 한도초과, 체크카드 잔액부족, 브라우저 창 종료, 취소버튼 클릭 등 결제실패상태)

            String failReason = responseNode.path("fail_reason").asText(); // 결제실패 사유로, 결제상태가 결제실패(failed)가 아닐 경우 null로 표시됨 : null
            Long failedAtLong = responseNode.path("failed_at").asLong(); //결제실패시각. 결제상태가 결제실패(failed)가 아닌 경우 0으로 표시됨. : 0
            LocalDateTime failedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(failedAtLong), ZoneId.systemDefault());

            String payMethod = responseNode.path("pay_method").asText(); 
            //결제수단 구분코드 :
            // card 외
            // trans(실시간 계좌이체),
            // vbank(가상계좌),
            // phone(휴대폰소액결제),
            // cultureland(컬쳐랜드상품권  (구)문화상품권),
            // smatculture(스마트문상(게임 문화 상품권)),
            // happymoney(해피머니),
            // booknlif(도서문화상품권),
            // culturegift(문화상품권)
            // samsung(삼성페이),
            // kakaopay(카카오페이)
            // naverpay(네이버페이)
            // payco(페이코)
            // lpay(LPAY),
            // ssgpay(SSG페이),
            // tosspay(토스페이),
            // applepay(애플페이),
            // pinpay (핀페이)
            // skpay(11pay(구.SKPay))
            // wechat(위쳇페이),
            // alipay(알리페이),
            // unionpay(유니온페이),
            // tenpay(텐페이)
            // paysbuy(페이스바이)
            // econtext(편의점 결제)
            // molpay(MOL페이)
            // point(베네피아 포인트 등 포인트 결제),
            // paypal(페이팔)
            // toss_brandpay(토스페이먼츠 브랜드페이)
            // naverpay_card(네이버페이 - 카드)
            // naverpay_point(네이버페이 - 포인트)

            // 결제와 관련된 정보
            String name = responseNode.path("name").asText(); // 결제건의 제품명 
            Long paidAtLong = responseNode.path("paid_at").asLong(); //결제된 시각. 결제상태가 결제완료(paid)가 아닌 경우 0으로 표시됨. : 1709722069
            LocalDateTime paidAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(paidAtLong), ZoneId.systemDefault());

            String receiptUrl = responseNode.path("receipt_url").asText(); //결제건의 매출전표 URL로 PG사 또는 결제수단에 따라 매출전표가 없을 수 있다. 

            Long startedAtLong = responseNode.path("started_at").asLong(); //결제건의 결제요청 시각 UNIX timestamp : 1709722022
            LocalDateTime startedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(startedAtLong), ZoneId.systemDefault()); // 2024-03-06 10:47:02

            String userAgent = responseNode.path("user_agent").asText(); //구매자가 결제시 사용한 단말기의 UserAgent 문자열이라고 함. 

            //구매자에 대한 정보- buyer 테이블 (확)
            String buyerName = responseNode.path("buyer_name").asText(); //주문자 이름 
            String buyerTel = responseNode.path("buyer_tel").asText(); //주문자 전화번호 : 010-1234-5678
            String buyerAddr = responseNode.path("buyer_addr").asText(); //주문자 주소 
            String buyerPostcode = responseNode.path("buyer_postcode").asText(); //주문자 우편번호 : 123-456
            String buyerEmail = responseNode.path("buyer_email").asText(); //주문자 이메일 

            //카드 결제
            String applyNum = responseNode.path("apply_num").asText(); //결제건의 신용카드 승인번호 : 31491375
            String cardCode = responseNode.path("card_code").asText(); //결제건의 카드사 코드번호(금융결제원 표준코드번호) - 카드 결제건의 경우 : 366
            String cardName = responseNode.path("card_name").asText(); //결제건의 카드사명 - 카드 결제건의 경우 
            String cardNumber = responseNode.path("card_number").asText(); //카드번호 : 559410*********4
            Integer cardQuota = responseNode.path("card-quota").asInt(); //할부개월 수 : 0
            Integer cardType = responseNode.path("card-type").asInt(); //0이면 신용카드, 1이면 체크카드. 해당 정보를 제공하지 않는 일부 PG사의 경우 null로 응답됨.(ex : JTNet, 이니시스-빌링) : 1 내가 체크카드로 결제해서 그래

            //실시간 계좌이체
            String bankCode = responseNode.path("bank_code").asText(); //결제건의 은행 표준코드(금융결제원기준) - 실시간계좌이체 결제건의 경우 : null
            String bankName = responseNode.path("bank_name").asText(); //결제건의 은행명 - 실시간계좌이체 결제건의 경우 : null

            //가상계좌(virtual bank)
            String vbankCode = responseNode.path("vbank_code").asText(); //결제건의 가상계좌 은행 표준코드(금융결제원기준) - 가상계좌 결제건의 경우 : null
            Integer vbankDate = responseNode.path("vbank_date").asInt(); //결제건의 가상계좌 입금기한 - 가상계좌 결제 건의 경우 : 0

            String vbankHolder = responseNode.path("vbank_holder").asText(); //결제건의 입금받을 가상계좌 입금주 - 가상계좌 결제건의 경우 : null
            Long vbankIssuedAtLong = responseNode.path("vbank_issued_at").asLong(); //결제건의 가상계좌 생성시각 UNIX timestamp - 가상계좌 결제건의 경우 : 0
            LocalDateTime vbankIssuedAt = LocalDateTime.ofInstant(Instant.ofEpochSecond(vbankIssuedAtLong), ZoneId.systemDefault());

            String vbankNum = responseNode.path("vbank_num").asText(); //결제건의 입금받을 가상계좌 계좌번호 - 가상계좌 결제건의 경우 : null
            String vbankName = responseNode.path("vbank_name").asText(); //결제건의 입금받을 가상계좌 은행명 - 가상계좌 결제건의 경우 : null

            //기타 다양한 정보들
            String customData = responseNode.path("custom_data").asText(); //결제 요청시 가맹점에서 전달한 추가정보(JSON string으로 전달) : null
            String customerUid = responseNode.path("customer_uid").asText(); //결제건에 사용된 빌링키와 매핑되며 가맹점에서 채번하는 구매자의 결제수단 식별 고유번호 : null
            String customerUidUsage = responseNode.path("customer_uid_usage").asText(); //결제처리에 사용된 구매자의 결제수단 식별 고유번호의 사용 구분코드 : null
            String channel = responseNode.path("channel").asText(); //결제환경 구분코드 : pc
            Boolean cashReceiptIssuedBoolean = responseNode.path("cash_receipt_issued").asBoolean(); //결제건의 현금영수증 발급 여부 : false
            String cashReceiptIssued = String.valueOf(cashReceiptIssuedBoolean);
            boolean escrowBoolean = responseNode.path("escrow").asBoolean(); //에스크로결제 여부 : false
            String escrow = String.valueOf(escrowBoolean);

            //PG사
            String pgId = responseNode.path("pg_id").asText(); //PG사 상점아이디 : INIBillTst
            String pgProvider = responseNode.path("pg_provider").asText(); //PG사 구분코드 : html5_inicis
            String embPgProvider = responseNode.path("emb_pg_provider").asText(); //허브형 결제 PG사 구분코드 : null
            String pgTid = responseNode.path("pg_tid").asText(); //PG사 거래번호 : StdpayCARDINIBillTst20240306194748374762

            //결제취소와 관련 정보 (결제 성공했다가 취소되는 경우. 구매자의 요청에 의한 취소, 상품의 결함으로 인한 취소 등.)
            int cancelAmount = responseNode.path("cancel_amount").asInt(); //결제건의 누적 취소금액 : 0
            String cancelReason = responseNode.path("cancel_reason").asText(); //결제취소된 사유. 결제상태가 결제취소(cancelled)가 아닐경우 null 로 표시됨. : null
            String cancelledAt = responseNode.path("cancelled_at").asText(); //결제취소된 시각. 결제상태가 결제취소(cancelled)가 아닐 경우 null 로 표시됨. : 0 ? 이거 왜 null 로 표시 안되어있지? 뭐가 들어올지 모르므로 String 으로 받아오자.
            // "cancel_history":[], -- 결제건의 취소/부분취소 내역 : PaymentCancelAnnotation[]
            // "cancel_receipt_urls":[], -- 더이상 사용하지 않는다고 한다. cancel_history 를 쓰도록 권장하고 있음.

            JsonNode cancelHistoryNode = responseNode.path("cancel_history");
            PaymentCancelAnnotation[] paymentCancelArr = objectMapper.treeToValue(cancelHistoryNode, PaymentCancelAnnotation[].class);
            for (PaymentCancelAnnotation anno : paymentCancelArr) {
                String pgTid2 = anno.getPgTid();
                Integer amount1 = anno.getAmount();
                Integer cancelledAt1 = anno.getCancelledAt();
                String reason = anno.getReason();
                String cancellationId = anno.getCancellationId();
                String receiptUrl1 = anno.getReceiptUrl();
                //cancel_history 테이블에 insert
            }

    		// 2. responseBody 에 있는 merchant_uid 를 이용해서, LECTURE_ORDERS 테이블의 행을 조회할 것. 
    		// 왜? LECTURE_PAYMENTS 테이블에 행을 삽입할건데, 컬럼 중 LECTURE_ORDERS_NO 이 있거든. 
            Integer lectureOrdersNo = paymentMapper.selectLectureOrdersNo(merchantUid);
            
            Payment payment = new Payment(
            		memberNo, lectureOrdersNo, impUid, merchantUid, amount, currency, status, failReason, failedAt, payMethod, name, paidAt, receiptUrl, startedAt, userAgent, buyerName, buyerTel, buyerAddr, buyerPostcode, buyerEmail, applyNum, cardCode, cardName,cardNumber ,cardQuota ,cardType , bankCode, bankName, vbankCode, vbankDate, vbankHolder, vbankIssuedAt, vbankNum, vbankName, customData, customerUid, customerUidUsage, channel, cashReceiptIssued, escrow, pgId, pgProvider, embPgProvider, pgTid, cancelAmount, cancelReason, cancelledAt
            		);
            
            
            paymentMapper.addPayment(payment);

        } catch (JsonProcessingException e) {
            log.error("SinglePaymentQueryService : JSON 파싱 중 오류 발생", e);
            throw new JsonParsingException("InsertPaymentRowService : JSON 파싱 중 오류 발생");
        }



	}

	@Override
	public void postValidation(String responseBody) throws JsonProcessingException{

        // 사후검증 계획
            // 1. 포트원으로부터 온 데이터 중, "merchant_uid":"57008833-33010" 이거를 꺼내서,
            // 내 DB의 orders 테이블에 있는 merchant_uid 컬럼의 값이 57008833-33010 인 행을 조회한다.
            // 2. 조회된 행의 amount 컬럼의 값은 해당 주문이 결제했어야 하는 금액을 넣어두었다.
            // 3. 따라서, 포트원으로부터 온 데이터 중 amount 와 방금 조회된 행의 amount 컬럼값이 일치하는지 검사한다.
            // 4. 같다면, 사후검증 통과.

        int amount = 0;
        Integer findAmount = 0;
        String merchantUid = "";

        // 사후검증 시작.
        // 1-1. responseBody 에서 "merchant_uid" + "amount" 꺼내오기.
        //   그러기 위해서, 포트원으로부터 온 HTTP응답을 보면, 크게 code, message, response 이렇게 3가지로 구성되어 있는데, 이 중 response 를 파헤친다.
        try {

                // jackson 라이브러리의 ObjectMapper 인스턴스 생성.
                // 왜? ObjectMapper 는 유연해서, 'JSON 형식'의 '문자열'을 파싱해서 JsonNode 타입 객체로 변환해줄 수 있다.
                // JsonNode 타입 객체를 알면, .path 메서드를 통해 쉽게 JSON 데이터에 접근할 수 있게된다.
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);
                JsonNode responseNode = rootNode.path("response");
                // 포트원이 나에게 보내준 HTTP응답메세지 중 얻고 싶었던 데이터인 amount 와 merchantUid 를 얻었다. 얻어오는 과정을 보니, JsonNode 타입 객체는 JSON 데이터를 계층적으로 구분해 놓는다고 예상됨.
                amount = responseNode.path("amount").asInt();
                merchantUid = responseNode.path("merchant_uid").asText();

            // 1-2. 얻어온 merchantUid 값과 동일한 MERCHANT_UID 컬럼값을 지닌 행의 amount(가격)을 
            //    LECTURE_ORDERS 테이블에서 꺼내온다.
            findAmount = paymentMapper.findAmountByMerchantUid(merchantUid);

            // 1-3. 결제되었어야 할 금액과 포트원이 결제되었다고 한 금액이 일치하는지 확인 
            if (amount == findAmount) {
                // 1-3-1) 일치하는 경우
                // ORDERS 테이블의 status 컬럼값을 Paid 로 바꾸어주면 된다. 
            	// PAID : 결제가 성공적으로 처리되었으며, 사후검증도 통과했다는 의미로 정의한다.
            	Map<String, String> paramMap = new HashMap<>();
        		paramMap.put("merchantUid", merchantUid);
        		paramMap.put("updateMessage", "PAID");                
        		paymentMapper.updateStatus(paramMap);
                
                //  PAYMENTS 테이블의 status 컬럼값을 바꾸어주어야 하는지에 대하여 생각해봤다. 결론 : 건들지 않는다.
                // 왜? payments 테이블의 status 컬럼값과 fail_reason 컬럼값을 포함한 모든 컬럼값은 결제단건조회한 결과 그대로를 저장해두는 것이 옳다고 생각한다. 즉, payments 테이블 = 포트원 결제단건조회 결과

            } else {
                // 1-3-2) 일치하지 않는 경우
                // 예외를 터뜨린다. 내가 만든 RuntimeException 인 PaymentVerificationException 을 터뜨려 줄 것이다.
                throw new PaymentVerificationException("SinglePaymentQueryService : 결제 검증 실패 - 결제금액 불일치");
            }
        } catch (PaymentVerificationException e) {
            // (사후검증에 통과하지 못한 경우) == (결제됬어야 하는 금액과 실제 결제된 금액이 불일치 하는 경우)
            log.error("SinglePaymentQueryService : 사후검증 중 결제금액 불일치", e);

            Integer priceDifference = findAmount - amount;

            if(priceDifference >0){
                // 사용자가 덜 지불한 경우
            	// LECTURE_ORDERRS 의 status 업데이트.
            	Map<String, String> paramMap = new HashMap<>();
        		paramMap.put("merchantUid", merchantUid);
        		paramMap.put("updateMessage", "POST_VALIDATION_FAILED_MINUS"); 
                paymentMapper.updateStatus(paramMap);
                
                Map<String, String> paramMap2 = new HashMap<>();
                paramMap2.put("merchantUid", merchantUid);
                paramMap2.put("failReason", "고객님이 " + priceDifference + "원 더 결제하셔야 합니다.");
                paymentMapper.updateFailReason(paramMap2);
              

                throw new PaymentVerificationException(amount, findAmount, priceDifference);
            } else if (priceDifference < 0) {
                // 사용자가 더 지불한 경우
            	Map<String, String> paramMap = new HashMap<>();
        		paramMap.put("merchantUid", merchantUid);
        		paramMap.put("updateMessage", "POST_VALIDATION_FAILED_PLUS"); 
                paymentMapper.updateStatus(paramMap);
                
                Map<String, String> paramMap2 = new HashMap<>();
                paramMap2.put("merchantUid", merchantUid);
                paramMap2.put("failReason", "고객님이" + priceDifference + "원 더 결제하셨습니다.");
                paymentMapper.updateFailReason(paramMap2);
            	
                throw new PaymentVerificationException(amount, findAmount, priceDifference);
            }

        } catch (JsonProcessingException e) {
            // JsonNode rootNode = objectMapper.readTree(responseBody); 이거 하다가 발생할 수 있는 예외를 처리해줘야 함.
            // 컨트롤러로 다시 예외를 발생시키도록 했음. 왜? 이렇게 잡고 다시 던지면, 로그를 남길 수 있잖아. 그래서, throws 로 던질수도 있었는데, 그렇게 안함.
            log.error("SinglePaymentQueryService : JSON 파싱 중 오류 발생", e);
            throw e;
        }

		
	}

	@Override
	public void plusRestNum(String selectDate, String lectureNo, String quantity) {
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("selectDate", selectDate);
		paramMap.put("quantity", Integer.parseInt(quantity));
		
		Integer result = paymentMapper.plusRestNum(paramMap);
	}

	@Override
	public void addRegisteredMember(Integer memberNo, Integer lectureNo, String lectureDate, String merchantUid) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("memberNo", memberNo);
		paramMap.put("lectureNo", lectureNo);
		paramMap.put("lectureDate", lectureDate);
		paramMap.put("merchantUid", merchantUid);
		
		paymentMapper.addRegisteredMember(paramMap);
		
	}

	@Override
	public void plusFeeSettlement(Integer lectureNo, Integer totalPrice) {
		// 1. 해당 강의를 하는 강사의 MEMBER 테이블 memberNo 값 가져오기 
		Integer lecturerMemberNo = paymentMapper.findLecturerMemberNo(lectureNo);
		
		// 2. 정산 테이블(FEE_SETTLEMENT) 에 해당 강사의 memberNo 로 업데이트를 시도. 
		// 만약, 업데이트 된 게 없어서 result 가 0 이라면, 새로운 행 삽입 시도. 
		Map<String, Integer> paramMap = new HashMap<>();
		paramMap.put("lecturerMemberNo", lecturerMemberNo);
		paramMap.put("totalPrice", totalPrice);
		
		
		int result = paymentMapper.updateFeeSettlement(paramMap);
		
		if(result == 0) {
			
			result = paymentMapper.addFeeSettlement(paramMap);
			
		}
		
		
		
	}



	
	
	
	
}
