package com.project.semi.admin.model.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.project.semi.admin.model.dto.RefundCustomer;
import com.project.semi.payment.model.dto.Order;
import com.project.semi.payment.model.dto.Payment;

@Mapper
public interface AdminMapper {

	int findAuthority(Integer memberNo);

	int findAllRefundCount();

	List<RefundCustomer> findCurrentPageRefunds(Map<String, Object> paramMap);

	Integer findAllOrderCount();

	List<Order> findCurrentPageOrders(Map<String, Object> paramMap);

	List<Order> findCurrentPageOrdersByNickname(Map<String, Object> paramMap);

	List<Order> findCurrentPageOrdersByTel(Map<String, Object> paramMap);

	Integer findAllOrderCountByNickname(String option);

	Integer findAllOrderCountByTel(String option);

	Payment payment(String merchantUid);


}
