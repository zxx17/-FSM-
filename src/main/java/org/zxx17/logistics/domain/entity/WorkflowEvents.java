package org.zxx17.logistics.domain.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 业务流程事件表(WorkflowEvents)实体类
 *
 * @author makejava
 * @since 2024-06-11 20:29:28
 */
@Data
public class WorkflowEvents implements Serializable {

  private static final long serialVersionUID = -35495915879292854L;
  /**
   * 事件ID
   */
  private Long id;
  /**
   * 流程ID
   */
  private Long workflowId;
  /**
   * 事件名称
   */
  private String eventName;
  /**
   * 起始状态编码
   */
  private String fromState;
  /**
   * 目标状态编码
   */
  private String toState;
  /**
   * 执行角色
   */
  private String role;
  /**
   * 创建时间
   */
  private Date createdTime;
  /**
   * 更新时间
   */
  private Date updatedTime;

}

