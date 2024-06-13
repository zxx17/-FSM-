package org.zxx17.logistics.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.annotation.OnTransition;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;
import org.zxx17.logistics.aspect.EventListenerResult;
import org.zxx17.logistics.common.enums.LogisticsEventEnum;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/

@Component
@Slf4j
@WithStateMachine(name = "logisticsStateMachine")
public class LogisticsStateMachineListener {


  /**
   * COLLECT待揽收--->揽收.
   */
  @OnTransition(source = "PENDING", target = "COLLECTED")
  @EventListenerResult(key = "COLLECT")
  public void collected(Message<LogisticsEventEnum> messageIn) {
    // 获取workflowId
    Long workflowId = (Long) messageIn.getHeaders().get("workflowId");
    log.info("COLLECT待揽收--->揽收.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....
  }

  /**
   * AUTO已揽收 -> 待支付.
   */
  @OnTransition(source = "COLLECTED", target = "PAYING")
  @EventListenerResult(key = "AUTO")
  public void waitPay(Message<LogisticsEventEnum> messageIn) {
    // 获取workflowId
    Long workflowId = (Long) messageIn.getHeaders().get("workflowId");
    log.info("AUTO揽收--->待支付.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * PAY待支付--->已支付.
   */
  @OnTransition(source = "PAYING", target = "PAID")
  @EventListenerResult(key = "PAY")
  public void pay(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("PAY待支付--->已支付.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * CANCEL待支付 --> 已取消.
   */
  @OnTransition(source = "PAYING", target = "CANCELLED")
  @EventListenerResult(key = "CANCEL")
  public void cancel(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("待支付 --> 已取消.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * AUTO已取消 --> 已完成.
   */
  @OnTransition(source = "CANCELLED", target = "COMPLETED")
  @EventListenerResult(key = "AUTO")
  public void cancelToCompleted(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("AUTO已取消 --> 已完成.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }


  /**
   * SHIP已支付--->运输中.
   */
  @OnTransition(source = "PAID", target = "TRANSITING")
  @EventListenerResult(key = "SHIP")
  public void transit(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("SHIP已支付--->运输中.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }


  /**
   * DELIVERY运输中--->派送中.
   */
  @OnTransition(source = "TRANSITING", target = "DELIVERY")
  @EventListenerResult(key = "DELIVERY")
  public void deliver(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("DELIVERY运输中--->派送中.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * DELIVERY派送中--->已签收.
   */
  @OnTransition(source = "DELIVERY", target = "DELIVERED")
  @EventListenerResult(key = "DELIVERY")
  public void delivered(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("DELIVERY派送中--->已签收.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * DELIVERY派送中--->已拒收.
   */
  @OnTransition(source = "DELIVERY", target = "REFUSED")
  @EventListenerResult(key = "CANCEL")
  public void refused(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("DELIVERY派送中--->已拒收.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * DELIVERY派送中--->异常件.
   */
  @OnTransition(source = "DELIVERY", target = "EXCEPTION")
  @EventListenerResult(key = "EXCEPTION")
  public void exception(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("DELIVERY派送中--->异常件.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * AUTO 异常件--->已完成.
   */
  @OnTransition(source = "EXCEPTION", target = "COMPLETED")
  @EventListenerResult(key = "AUTO")
  public void exceptionToCompleted(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info(" AUTO 异常件--->已完成.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * AUTO 已签收--->已完成.
   */
  @OnTransition(source = "DELIVERED", target = "COMPLETED")
  @EventListenerResult(key = "AUTO")
  public void deliveredToCompleted(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("AUTO 已签收--->已完成.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }

  /**
   * AUTO 已拒收--->已完成.
   */
  @OnTransition(source = "REFUSED", target = "COMPLETED")
  @EventListenerResult(key = "AUTO")
  public void refusedToCompleted(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    log.info("AUTO 已拒收--->已完成.{}===================", workflowId);
    // 更新对应workflow实例状态 相当于入库操作 .....

  }


}
