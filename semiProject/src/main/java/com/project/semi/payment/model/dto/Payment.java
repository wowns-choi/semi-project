package com.project.semi.payment.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder // 인스턴스 쉽게 만들게해줌.
public class Payment {
	private Integer memberNo;
	private Integer lectureOrdersNo; 
	private String impUid;
	private String merchantUid; 
	private int amount;
	
	private String currency; 
	private String status;
	private String failReason;
	private LocalDateTime failedAt; 
	private String payMethod; 
	
	private String name;
	private LocalDateTime paidAt;
	private String receiptUrl;
	private LocalDateTime startedAt; 
	private String userAgent; 
	
	private String buyerName;
	private String buyerTel;
	private String buyerAddr;
	private String buyerPostcode; 
	private String buyerEmail; 
	
	private String applyNum;
	private String cardCode; 
	private String cardName;
	private String cardNumber; 
	private Integer cardQuota; 
	
	private Integer cardType; 
	private String bankCode;
	private String bankName;
	private String vbankCode;
	private Integer vbankDate;
	
	private String vbankHolder;
	private LocalDateTime vbankIssuedAt;
	private String vbankNum; 
	private String vbankName;
	private String customData;
	
	private String customerUid;
	private String customerUidUsage;
	private String channel;
	private String cashReceiptIssued; 
	private String escrow; 
	
	private String pgId;
	private String pgProvider;
	private String embPgProvider; 
	private String pgTid;
	private int cancelAmount;
	
	private String cancelReason; 
	private String cancelledAt;

}
