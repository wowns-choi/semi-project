package com.project.semi.admin.model.service;

import org.springframework.ui.Model;

import com.project.semi.payment.model.dto.Payment;

public interface AdminService {

	int findAuthority(Integer memberNo);

	void findRefundList(int page, Model model);

	void findOrderList(int page, Model model);

	void findOrderList(int int1, Model model, String findOption, String option);

	Payment payment(String merchantUid);


}
