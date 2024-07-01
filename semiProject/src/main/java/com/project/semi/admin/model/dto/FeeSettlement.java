package com.project.semi.admin.model.dto;

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
public class FeeSettlement {	
	private String memberNickname;
	private Integer feeSettlementNo;
	private Integer settlementAmount;
	private String settlementStatus;
}
