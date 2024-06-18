package org.zxx17.logistics.aspect;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.common.enums.LogisticsEventEnum;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class EventListenerResultAspect {

  private final StateMachine<LogisticsStatusEnum, LogisticsEventEnum> logisticsStateMachine;

  /**
   * 切面.
   */
  @Around("@annotation(org.zxx17.logistics.aspect.EventListenerResult)")
  public Object eventListenerResult(ProceedingJoinPoint pjp) throws Throwable {
    //获取参数
    Object[] args = pjp.getArgs();
    log.info("执行流程流转方法->参数args:{}", args);
    Message message = (Message) args[0];
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    //获取方法
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    // 获取EventListenerResult注解
    EventListenerResult demoEventListenerResult = method.getAnnotation(
        EventListenerResult.class);
    String key = demoEventListenerResult.key();
    Object returnVal = null;
    try {
      //执行方法
      log.info("执行监听器代理方法");
      // 执行 LogisticsStateMachineListener
      returnVal = pjp.proceed();
      //如果业务执行正常，则保存信息
      //成功 则为1
      log.info(">>>>>>>监听器代理方法执行完毕。保存ExtendedState状态为正常<<<<<<<");
      logisticsStateMachine.getExtendedState().getVariables().put(key + workflowId, 1);
    } catch (Throwable e) {
      log.error("e:{}", e.getMessage());
      //如果业务执行异常，则保存信息
      //将异常信息变量信息中，失败则为0
      logisticsStateMachine.getExtendedState().getVariables().put(key + workflowId, 0);
      throw e;
    }
    return returnVal;
  }

}
