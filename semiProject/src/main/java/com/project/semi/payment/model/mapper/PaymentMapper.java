package com.project.semi.payment.model.mapper;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.payment.model.dto.Order;
import com.project.semi.payment.model.dto.Payment;
@Mapper
public interface PaymentMapper {

	Integer checkRestNum(Map<String, Object> paramMap);

	int addOrder(Order order);

	Integer minusRestNum(Map<String, Object> paramMap);

	void updateStatus(Map<String, String> paramMap);

	Integer selectLectureOrdersNo(String merchantUid);

	void addPayment(
			Payment payment
			);

	Integer findAmountByMerchantUid(String merchantUid);

	void updateFailReason(Map<String, String> paramMap2);

	Integer plusRestNum(Map<String, Object> paramMap);

	void addRegisteredMember(Map<String, Object> paramMap);

	Integer findLecturerMemberNo(Integer lectureNo);

	int updateFeeSettlement(Map<String, Integer> paramMap);

	int addFeeSettlement(Map<String, Integer> paramMap);


}
