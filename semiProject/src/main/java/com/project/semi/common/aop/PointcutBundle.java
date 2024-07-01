package com.project.semi.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class PointcutBundle {

	@Pointcut("execution(* com.project.semi..*Controller*.*(..))")
	public void controllerPointCut() {}
	
	@Pointcut("execution(* com.project.semi..*ServiceImpl*.*(..))")
	public void serviceImplPointCut() {}
}
