package org.zxx17.logistics.config;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 **/
@Configuration
@Slf4j
public class LogisticsStateMachinePersistConfig<E, S> {

  /**
   * 持久化到内存map中.
   */
  @Bean(name = "stateMachineMemoryPersisted")
  public static StateMachinePersister getPersistent() {
    return new DefaultStateMachinePersister(new StateMachinePersist() {
      @Override
      public void write(StateMachineContext context, Object contextObj) throws Exception {
        log.info("持久化状态机,context:{},contextObj:{}", JSON.toJSONString(context),
            JSON.toJSONString(contextObj));
        map.put(contextObj, context);
      }

      @Override
      public StateMachineContext read(Object contextObj) throws Exception {
        log.info("获取状态机,contextObj:{}", JSON.toJSONString(contextObj));
        StateMachineContext stateMachineContext = (StateMachineContext) map.get(contextObj);
        log.info("获取状态机结果,stateMachineContext:{}", JSON.toJSONString(stateMachineContext));
        return stateMachineContext;
      }

      private Map map = new HashMap();
    });
  }


}
