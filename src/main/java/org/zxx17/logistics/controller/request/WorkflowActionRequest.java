package org.zxx17.logistics.controller.request;

import lombok.Data;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/11
 */
@Data
public class WorkflowActionRequest {
  // 流程实例id
  private Long id;
  // 动作
  private String action;
  // 角色
  private String role;
}
