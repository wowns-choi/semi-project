package com.project.semi.common.interceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.semi.member.model.dto.Member;
import com.project.semi.register.model.dto.RegisterMessage;
import com.project.semi.register.model.service.LectureRegisterService;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageExistInterceptor implements HandlerInterceptor {

	@Autowired
	private LectureRegisterService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		ServletContext application = request.getServletContext();
		
		if(application.getAttribute("messageList") == null) {
			log.info("MessageExistInterceptor - preHandle(전처리) 동작 실행");

			List<RegisterMessage> messageList = service.selectMessageList();
	
			application.setAttribute("messageList", messageList);
		}
		
//		HttpSession session = request.getSession();
//		
//		if(session.getAttribute("loginMember") != null) {
//			Member member = (Member)session.getAttribute("loginMember");
//			
//			
//		}

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
	
}
