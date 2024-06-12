package org.zxx17.logistics.listener;

import java.util.Objects;
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
   * 揽收.
   */
  @OnTransition(source = "PENDING", target = "COLLECTED")
  @EventListenerResult(key = "COLLECT")
  public void  collected(Message<LogisticsEventEnum> message) {
    // 获取workflowId
    Long workflowId = (Long) message.getHeaders().get("workflowId");
    System.out.println();
    // 更新对应workflow实例状态
  }


}
