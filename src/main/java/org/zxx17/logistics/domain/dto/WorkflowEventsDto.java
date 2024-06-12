package org.zxx17.logistics.domain.dto;

import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Data
public class WorkflowEventsDto {
  /**
   * 事件名称.
   */
  private String name;
  /**
   * 起始状态编码.
   */
  private String fromState;
  /**
   * 目标状态编码.
   */
  private String toState;
  /**
   * 执行角色.
   */
  private String role;
}
