package org.zxx17.logistics.config;

import java.util.EnumSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.StateMachineConfig;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigBuilder;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.config.common.annotation.AnnotationBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.zxx17.logistics.common.enums.LogisticsEventEnum;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;

/**
 * 物流状态机配置类，用于定义物流流程中的状态转换、初始状态、配置监听器等。 本类通过继承 `EnumStateMachineConfigurerAdapter` 并指定物流状态和事件枚举.
 * 来实现状态机的配置逻辑.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024-06-11
 */
@Slf4j
@Configuration
@EnableStateMachine(name = "logisticsStateMachine")
public class LogisticsStateMachineConfig
    extends StateMachineConfigurerAdapter<LogisticsStatusEnum, LogisticsEventEnum> {

  /**
   * 配置状态机的基本设置。在此方法中调用超类方法以确保基础配置得以应用.
   *
   * @param config 状态机配置构建器
   * @throws Exception 配置过程中可能抛出的异常
   */
  @Override
  public void configure(StateMachineConfigBuilder<LogisticsStatusEnum, LogisticsEventEnum> config)
      throws Exception {
    super.configure(config);
  }

  /**
   * 配置状态机模型。此方法通常用于扩展状态机模型的自定义配置，但此处直接调用超类方法.
   *
   * @param model 状态机模型配置器
   * @throws Exception 配置过程中可能抛出的异常
   */
  @Override
  public void configure(StateMachineModelConfigurer<LogisticsStatusEnum, LogisticsEventEnum> model)
      throws Exception {
    super.configure(model);
  }

  /**
   * 配置状态机的全局设置，如自动启动和监听器。此例中配置了自动启动并注册了一个状态改变监听器.
   *
   * @param config 状态机配置配置器
   * @throws Exception 配置过程中可能抛出的异常
   */
  @Override
  public void configure(
      StateMachineConfigurationConfigurer<LogisticsStatusEnum, LogisticsEventEnum> config)
      throws Exception {
    config
        .withConfiguration()
        .autoStartup(true)
        .listener(listener());
  }

  /**
   * 定义状态机的所有状态，包括初始状态。此处使用物流状态枚举的所有值作为状态集合.
   *
   * @param states 状态配置器
   * @throws Exception 配置过程中可能抛出的异常
   */
  @Override
  public void configure(StateMachineStateConfigurer<LogisticsStatusEnum, LogisticsEventEnum> states)
      throws Exception {
    states
        .withStates()
        .initial(LogisticsStatusEnum.PENDING)
        .states(EnumSet.allOf(LogisticsStatusEnum.class));
  }

  /**
   * 配置状态间的转换逻辑。当前示例中转换逻辑被注释，需根据实际情况实现状态转换.
   *
   * @param transitions 状态转换配置器
   * @throws Exception 配置过程中可能抛出的异常
   */
  @Override
  public void configure(
      StateMachineTransitionConfigurer<LogisticsStatusEnum, LogisticsEventEnum> transitions)
      throws Exception {
    transitions
        // 待揽收 -> 已揽收
        .withExternal()
        .source(LogisticsStatusEnum.PENDING).target(LogisticsStatusEnum.COLLECTED)
        .event(LogisticsEventEnum.COLLECT)
        // 已揽收 -> 待支付
        .and().withExternal().source(LogisticsStatusEnum.COLLECTED)
        .target(LogisticsStatusEnum.PAYING)
        .event(LogisticsEventEnum.AUTO)
        // 待支付 -> 已支付
        .and().withExternal().source(LogisticsStatusEnum.PAYING).target(LogisticsStatusEnum.PAID)
        .event(LogisticsEventEnum.PAY)
        // 已支付 -> 运输中
        .and().withExternal().source(LogisticsStatusEnum.PAID)
        .target(LogisticsStatusEnum.TRANSITING)
        .event(LogisticsEventEnum.SHIP)
        // 运输中 -> 派送中
        .and().withExternal().source(LogisticsStatusEnum.TRANSITING)
        .target(LogisticsStatusEnum.DELIVERY)
        .event(LogisticsEventEnum.DELIVERY)
        // 派送中 -> 已签收
        .and().withExternal().source(LogisticsStatusEnum.DELIVERY)
        .target(LogisticsStatusEnum.DELIVERED)
        .event(LogisticsEventEnum.DELIVERY)
        // 派送中 -> 已拒收
        .and().withExternal().source(LogisticsStatusEnum.DELIVERY)
        .target(LogisticsStatusEnum.REFUSED)
        .event(LogisticsEventEnum.CANCEL)
        // 派送中 -> 异常件
        .and().withExternal().source(LogisticsStatusEnum.DELIVERY)
        .target(LogisticsStatusEnum.EXCEPTION)
        .event(LogisticsEventEnum.EXCEPTION)
        // 待揽收 -> 已取消
        .and().withExternal().source(LogisticsStatusEnum.PENDING)
        .target(LogisticsStatusEnum.CANCELLED)
        .event(LogisticsEventEnum.CANCEL)
        // 待支付 -> 已取消
        .and().withExternal().source(LogisticsStatusEnum.PAYING)
        .target(LogisticsStatusEnum.CANCELLED)
        .event(LogisticsEventEnum.CANCEL)
        // 自动完成相关状态转换
        // 已签收 -> 已完成
        .and().withExternal().source(LogisticsStatusEnum.DELIVERED)
        .target(LogisticsStatusEnum.COMPLETED)
        .event(LogisticsEventEnum.AUTO)
        // 已取消 -> 已完成
        .and().withExternal().source(LogisticsStatusEnum.CANCELLED)
        .target(LogisticsStatusEnum.COMPLETED)
        .event(LogisticsEventEnum.AUTO)
        // 已拒收 -> 已完成
        .and().withExternal().source(LogisticsStatusEnum.REFUSED)
        .target(LogisticsStatusEnum.COMPLETED)
        .event(LogisticsEventEnum.AUTO)
        // 异常件 -> 已完成
        .and().withExternal().source(LogisticsStatusEnum.EXCEPTION)
        .target(LogisticsStatusEnum.COMPLETED)
        .event(LogisticsEventEnum.AUTO);
  }


  /**
   * 检查当前配置类是否可分配给指定的注解构建器。此方法直接委托给超类实现.
   *
   * @param builder 注解构建器，用于构建特定的 `StateMachineConfig` 注解
   * @return 是否可分配的布尔值
   */
  @Override
  public boolean isAssignable(
      AnnotationBuilder<StateMachineConfig<LogisticsStatusEnum, LogisticsEventEnum>> builder) {
    return super.isAssignable(builder);
  }

  /**
   * 定义并返回一个状态机监听器 Bean，用于监听状态变更事件并打印日志.
   *
   * @return 状态机监听器实例
   */
  @Bean
  public StateMachineListener<LogisticsStatusEnum, LogisticsEventEnum>  listener() {
    return new StateMachineListenerAdapter<LogisticsStatusEnum, LogisticsEventEnum>() {
      /**
       * 状态改变时触发的方法，打印状态变更信息.
       *
       * @param from 起始状态
       * @param to 目标状态
       */
      @Override
      public void stateChanged(
          State<LogisticsStatusEnum, LogisticsEventEnum> from,
          State<LogisticsStatusEnum, LogisticsEventEnum> to
      ) {
        System.out.println(">>>>状态机状态改变>>>>>" + to.getId());
      }

      @Override
      public void stateMachineStarted(
          StateMachine<LogisticsStatusEnum, LogisticsEventEnum> stateMachine) {
        log.info(">>>>>物流状态机已启动");
      }

      @Override
      public void stateMachineStopped(
          StateMachine<LogisticsStatusEnum, LogisticsEventEnum> stateMachine) {
        log.info("<<<<<<物流状态机已停止");

      }
    };
  }


}

