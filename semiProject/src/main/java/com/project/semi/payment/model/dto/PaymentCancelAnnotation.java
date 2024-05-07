package com.project.semi.payment.model.dto;

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
public class PaymentCancelAnnotation {
    private String pgTid; // 결제건의 PG사 승인취소번호
    private Integer amount; //결제건의 취소금액
    private Integer cancelledAt; //결제건의 결제취소된 시각 UNIX timestamp
    private String reason; //결제건의 결제취소 사유
    private String cancellationId; //결제건의 취소 아이디
    private String receiptUrl; //결제건의 취소 매출전표 확인 URL로 PG사, 결제 수단에 따라 제공되지 않을 수 있음.
}
