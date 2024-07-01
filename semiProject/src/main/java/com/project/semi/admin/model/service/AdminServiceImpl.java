package com.project.semi.admin.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import com.project.semi.admin.model.dto.FeeSettlement;
import com.project.semi.admin.model.dto.RefundCustomer;
import com.project.semi.admin.model.mapper.AdminMapper;
import com.project.semi.payment.model.dto.Order;
import com.project.semi.payment.model.dto.Payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService{

	private final AdminMapper adminMapper;

	@Override
	public int findAuthority(Integer memberNo) {
		return adminMapper.findAuthority(memberNo);
		
	}
	

	@Override
	public void findOrderList(int page, Model model) {
		// 2. 현재 페이지
		Integer currentPage = page;
		
		// 3. 총 페이지 수 
		Integer totalRow = adminMapper.findAllOrderCount();
		Integer pageSize = 10; // 페이지당 보여질 게시글 수 
		Integer totalPages = (int)Math.ceil((double)totalRow/pageSize);
		
		// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
		Integer pageGroupSize = 9;
		
		// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
		Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;

		// 5. 현재 페이지 그룹의 마지막 페이지 
		Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		
		// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
		int startRow = (currentPage - 1) * pageSize;
		
		// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startRow", startRow);
		paramMap.put("pageSize", pageSize);
		
		List<Order> currentPageOrdersList= adminMapper.findCurrentPageOrders(paramMap);
		
		for(Order refund : currentPageOrdersList) {
			log.debug("REFUND!!!!!!aaaaaaaaaa=={}", refund);
		}
		
		//----------------------------------------------------------------------
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("currentPageOrdersList", currentPageOrdersList);
		
	}
	


	@Override
	public void findOrderList(int page, Model model, String findOption, String option) {
		
		// 2. 현재 페이지
		Integer currentPage = page;
		
		Integer totalRow = 0;
		
		// 3. 총 페이지 수 
		if(findOption.equals("memberNickName")) {
			log.debug("별별별이 보이네요");
			totalRow = adminMapper.findAllOrderCountByNickname(option);
		}else {
			log.debug("aasdfqwer=={}", option.equals("0106701111"));
			totalRow = adminMapper.findAllOrderCountByTel(option);
		}
		
		Integer pageSize = 10; // 페이지당 보여질 게시글 수 
		Integer totalPages = (int)Math.ceil((double)totalRow/pageSize);
		
		// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
		Integer pageGroupSize = 9;
		
		// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
		Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;

		// 5. 현재 페이지 그룹의 마지막 페이지 
		Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		
		// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
		int startRow = (currentPage - 1) * pageSize;
		
		// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startRow", startRow);
		paramMap.put("pageSize", pageSize);
		paramMap.put("option", option);
		List<Order> currentPageOrdersList = new ArrayList<>();
		
		if(findOption.equals("memberNickName")) {
			currentPageOrdersList = adminMapper.findCurrentPageOrdersByNickname(paramMap);
		} else {
			currentPageOrdersList = adminMapper.findCurrentPageOrdersByTel(paramMap);
		}
		
		
		for(Order refund : currentPageOrdersList) {
			log.debug("REFUND!!!!!!aaaaaaaaaa=={}", refund);
		}
		
		//----------------------------------------------------------------------
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("currentPageOrdersList", currentPageOrdersList);
		
	}




	@Override
	public void findRefundList(int page, Model model) {
		
//		1. 현재 페이지에 보여질 게시글(PostsDTO)을 담은 List자료구조.
//		2. 현재 boardHome.jsp 파일에서 렌더링하고 있는 페이지가 "몇 페이지" 인지.
//		3. 총 페이지 수
//		4. 현재 페이지 그룹의 첫번째 페이지
//		5. 현재 페이지 그룹의 마지막 페이지
//		6. 몇개의 페이지를 하나의 그룹으로 묶었는지.
		
		// 2. 현재 페이지
		Integer currentPage = page;
		
		// 3. 총 페이지 수 
		Integer totalRow = adminMapper.findAllRefundCount();
		Integer pageSize = 10; // 페이지당 보여질 게시글 수 
		Integer totalPages = (int)Math.ceil((double)totalRow/pageSize);
		
		// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
		Integer pageGroupSize = 9;
		
		// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
		Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;

		// 5. 현재 페이지 그룹의 마지막 페이지 
		Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		
		// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
		int startRow = (currentPage - 1) * pageSize;
		
		// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("startRow", startRow);
		paramMap.put("pageSize", pageSize);
		
		List<RefundCustomer> currentPageRefundsList= adminMapper.findCurrentPageRefunds(paramMap);
		
		for(RefundCustomer refund : currentPageRefundsList) {
			log.debug("REFUND!!!!!!aaaaaaaaaa=={}", refund);
		}
		
		//----------------------------------------------------------------------
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("currentPageRefundsList", currentPageRefundsList);

	}

	
	@Override
	public void findRefundList2(int page, Model model, String findOption, String option) {
				// 2. 현재 페이지
				Integer currentPage = page;
				
				// 3. 총 페이지 수 
				Integer totalRow = 0 ;
				if(findOption.equals("memberNickName")) {
					// 이름으로 조회한 경우 행의 총 개수 구하기
					 totalRow = adminMapper.findAllRefundCountByNickname(option);
				}
				if(findOption.equals("memberTel")) {
					// 전화번호로 조회한 경우
					 totalRow = adminMapper.findAllRefundCountByTel(option);
				}
				if(findOption.equals("merchantUid")) {
					// 주문번호로 조회한 경우
					 totalRow = adminMapper.findAllRefundCountByMerchantUid(option);
				}
				
				Integer pageSize = 10; // 페이지당 보여질 게시글 수 
				Integer totalPages = (int)Math.ceil((double)totalRow/pageSize);
				
				// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
				Integer pageGroupSize = 9;
				
				// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
				Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
				Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;

				// 5. 현재 페이지 그룹의 마지막 페이지 
				Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
				
				// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
				int startRow = (currentPage - 1) * pageSize;
				
				// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
				Map<String, Object> paramMap = new HashMap<>();
				paramMap.put("startRow", startRow);
				paramMap.put("pageSize", pageSize);
				
				if(findOption.equals("memberNickName")) {
					paramMap.put("memberNickName", option);
				}
				if(findOption.equals("memberTel")) {
					paramMap.put("memberTel", option);
				}
				if(findOption.equals("merchantUid")) {
					paramMap.put("merchantUid", option);
				}
				
				List<RefundCustomer> currentPageRefundsList= adminMapper.findCurrentPageRefunds(paramMap);
				
				for(RefundCustomer refund : currentPageRefundsList) {
					log.debug("REFUND!!!!!!aaaaaaaaaa=={}", refund);
				}
				
				//----------------------------------------------------------------------
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("pageGroupSize", pageGroupSize);
				model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
				model.addAttribute("currentGroupLastPage", currentGroupLastPage);
				model.addAttribute("currentPageRefundsList", currentPageRefundsList);
				
				
		
	}


	@Override
	public Payment payment(String merchantUid) {
		
		return adminMapper.payment(merchantUid);
		

	}
	
	

	@Override
	public void settlement(Integer page, Model model) {
		// 2. 현재 페이지
				Integer currentPage = page;
				
				// 3. 총 페이지 수 
				Integer totalRow = 0 ;
				// 총 개수 
			
				// 조회된 행의 개수. 조건에 따라 동적으로 정해짐 
				

				List<Integer> ct= adminMapper.settlement1(); 
				Integer totalRows = ct.size();
				
				Integer pageSize = 10; // 페이지당 보여질 게시글 수 
				Integer totalPages = (int)Math.ceil((double)totalRows/pageSize);
				
				log.debug("totalRows ={}", totalRows);
				log.debug("pageSize ={}", pageSize);
				log.debug("totalPages ={}", totalPages);
				
				
				// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
				Integer pageGroupSize = 9;		
				
				// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
				Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
				Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;
				
				// 5. 현재 페이지 그룹의 마지막 페이지 
				Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
				
				// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
				int startRow = (currentPage - 1) * pageSize;
				
				// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
				Map<String, Object> paramMap2 = new HashMap<>();
				paramMap2.put("startRow", startRow);
				paramMap2.put("pageSize", pageSize);				
				List<FeeSettlement> currentPageSettlementList = adminMapper.findCurrentPageSettlementList1(paramMap2);
				
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("totalPages", totalPages);
				model.addAttribute("pageGroupSize", pageGroupSize);
				model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
				model.addAttribute("currentGroupLastPage", currentGroupLastPage);
				model.addAttribute("currentPageSettlementList", currentPageSettlementList);
				
				log.debug("currentPage={}", currentPage);
				log.debug("totalPages={}", totalPages);
				log.debug("pageGroupSize={}", pageGroupSize);
				log.debug("currentGroupFirstPage={}", currentGroupFirstPage);
				log.debug("currentGroupLastPage={}", currentGroupLastPage);
				log.debug("currentPageSettlementList={}", currentPageSettlementList);
				
		
		
	}



	@Override
	public void settlementOption(String lecturer, Integer count, int page, Model model) {
		// 2. 현재 페이지
		Integer currentPage = page;
		
		// 3. 총 페이지 수 
		Integer totalRow = 0 ;
		// 총 개수 
		
		log.debug("lecturer 는 뭘까?=={}", lecturer);
		
		if(lecturer.equals("")) {
			
			log.debug("널이 됨");
			lecturer = null;
		}
		
		String status = "";
		
		if(count == 0) {
			count = null;
		} else if (count > 0) {
			status= "SETTLED";
		} else if (count < 0) {
			status ="UNSETTLED";
		}

		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("lecturer", lecturer);
		paramMap.put("count", count);
		paramMap.put("status", status);
		
		// 조회된 행의 개수. 조건에 따라 동적으로 정해짐 
		 
		List<Integer>	ct = 	adminMapper.settlement2(paramMap); 
		Integer totalRows = ct.size();
		
		Integer pageSize = 10; // 페이지당 보여질 게시글 수 
		Integer totalPages = (int)Math.ceil((double)totalRows/pageSize);
		
		// 6. 몇 개의 페이지를 하나의 그룹으로 묶을 건지. 
		Integer pageGroupSize = 9;		
		
		// 4. 현재 페이지가 속한 페이지그룹의 첫번째 페이지 
		Integer currentGroup = (int) Math.ceil((double)currentPage/pageGroupSize);
		Integer currentGroupFirstPage = (currentGroup - 1) * pageGroupSize + 1;
		
		// 5. 현재 페이지 그룹의 마지막 페이지 
		Integer currentGroupLastPage = Math.min(currentGroupFirstPage + pageGroupSize - 1, totalPages);
		
		// 1. 현재 페이지에 보여질 게시글을 담은 List 자료구조
		int startRow = (currentPage - 1) * pageSize;
		
		// startRow 부터 시작해서 pageSize 만큼 행을 가져올 것 . 
		Map<String, Object> paramMap2 = new HashMap<>();
		paramMap2.put("startRow", startRow);
		paramMap2.put("pageSize", pageSize);
		paramMap2.put("lecturer", lecturer);
		paramMap2.put("count", count);
		paramMap2.put("status", status);
		
		log.debug("검증==={}", lecturer == null);
		
		// 어떻게 해야 조건에 따른 보여질 전체 행의 리스트를 얻을 수 있을까?
		List<FeeSettlement> currentPageSettlementList = adminMapper.findCurrentPageSettlementList2(paramMap2);
		
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageGroupSize", pageGroupSize);
		model.addAttribute("currentGroupFirstPage", currentGroupFirstPage);
		model.addAttribute("currentGroupLastPage", currentGroupLastPage);
		model.addAttribute("currentPageSettlementList", currentPageSettlementList);
		
		log.debug("currentPageSettlementList!!!!!!!!!!!!!!={}", currentPageSettlementList);
		
		log.debug("currentPage={}", currentPage);
		log.debug("totalPages={}", totalPages);
		log.debug("pageGroupSize={}", pageGroupSize);
		log.debug("currentGroupFirstPage={}", currentGroupFirstPage);
		log.debug("currentGroupLastPage={}", currentGroupLastPage);
		log.debug("currentPageSettlementList={}", currentPageSettlementList);


		

	}





}
