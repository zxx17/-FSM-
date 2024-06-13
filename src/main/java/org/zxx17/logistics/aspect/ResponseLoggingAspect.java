package org.zxx17.logistics.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/13
 **/
@Slf4j
@Aspect
@Component
public class ResponseLoggingAspect {

  /**
   * 拦截controller包下的所有类的方法（测试使用，未完善）.
   */
  @Around("execution(* org.zxx17.logistics.controller..*(..))")
  public Object logControllerMethodResponses(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    Object[] pointArgs = joinPoint.getArgs();
    // 执行原方法
    Object result = joinPoint.proceed();
    // 打印返回值
    log.info(">>>>>>>>>>>>>>>>>>>{}() 方法返回值:  {}", methodName, result);
    return result;
  }


}